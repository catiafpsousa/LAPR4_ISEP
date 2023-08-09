package eapli.base.app.common.console.presentation.sharedboard;

import eapli.base.boardmanagement.application.UndoPostItLastChangeController;
import eapli.base.boardmanagement.application.UpdatePostItController;
import eapli.base.boardmanagement.domain.Cell;
import eapli.base.boardmanagement.domain.Content;
import eapli.base.boardmanagement.domain.PostIt;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */

public class UndoPostItLastChangeUI extends AbstractUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UndoPostItLastChangeUI.class);

    private final UndoPostItLastChangeController ctrl = new UndoPostItLastChangeController();


    @Override
    protected boolean doShow() {
        final List<SharedBoard> list = new ArrayList<>();
        final Iterable<SharedBoard> iterable = this.ctrl.sharedBoardsWithWritePermission();
        final Iterable<SharedBoard> allboards = this.ctrl.allBoards();
        if (!allboards.iterator().hasNext()) {
            System.out.println("There are no Boards");
        } else if (!iterable.iterator().hasNext()) {
            System.out.println("There are no Boards with Write Permission");
        } else {
            int cont = 1;
            System.out.println("SELECT Board to Undo PostIt Last Change\n");
            System.out.printf("%-6s%-15s%n", "No.", "Title");

            for (final SharedBoard board : iterable) {
                list.add(board);
                System.out.printf("%-6d%-15s%n", cont, board.title().title());
                cont++;
            }
            final int option = Console.readInteger("Enter Board or 0 to finish ");
            if (option == 0) {
                System.out.println("No Board selected");
            } else {
                try {
                    SharedBoard boardSelected = list.get(option - 1);
                    System.out.printf("Selected SharedBoard: %-20s\n\n", boardSelected.title());
                    ctrl.printBoardAsTable(boardSelected);
                    Iterable<Cell> cells = this.ctrl.cellsOfBoardAndUser(boardSelected);
                    List<Cell> cellsOrdered = new ArrayList<>();
                    cells.forEach(cellsOrdered::add);
                    Collections.sort(cellsOrdered);
                    List<PostIt> postItList = new ArrayList<>();
                    List<Cell> cellList = new ArrayList<>();
                    int cont2 = 1;
                    System.out.println("SELECT PostIt to Perform Undo Operation\n");
                    System.out.printf("%-6s%-6s%-10s%-20s%n", "No.", "Row", "Column", "Content");
                    for (final Cell cell : cellsOrdered) {
                        Set<PostIt> postIts = cell.postItList();
                        cellList.add(cell);
                        for (PostIt p : postIts) {
                            if (!(p.isDeleted() && p.hasNext())) {
                                postItList.add(p);
                                System.out.printf("%-6d%-6d%-10d%-20s%n", cont2, cell.row().number(), cell.column().number(), p.content().toString());
                                cont2++;
                            }
                        }


                    }
                    final int option2 = Console.readInteger("Enter PostIt or 0 to finish ");
                    if (option2 == 0) {
                        System.out.println("No PostIt selected - Undo Operation Canceled");
                    } else {
                        try {
                            PostIt postIt = postItList.get(option2 - 1);

                            boolean success = ctrl.undoPostItLastChange(boardSelected, postIt);
                            if (success) {
                                System.out.println("Undo Operation performed with success!!");
                                System.out.println("SharedBoard after PostIt Undo operation:\n");
                                ctrl.printBoardAsTable(boardSelected);
                            } else
                                System.out.println("Undo Operation could not be performed -- ParentCell is in use OR PostIt has no previous version");


                        } catch (IntegrityViolationException | ConcurrencyException ex) {
                            LOGGER.error("Error performing the operation", ex);
                            System.out.println("Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

    @Override
    public String headline() {
        return "UNDO LAST POSTIT CHANGE";
    }
}