package eapli.base.app.common.console.presentation.sharedboard;

import eapli.base.boardmanagement.application.CreatePostItController;
import eapli.base.boardmanagement.domain.Cell;
import eapli.base.boardmanagement.domain.Content;
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
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */

public class CreatePostItUI extends AbstractUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePostItUI.class);

    private final CreatePostItController ctrl = new CreatePostItController();


    @Override
    protected boolean doShow() {
        final List<SharedBoard> list = new ArrayList<>();
        final Iterable<SharedBoard> allUserBoardsWithWritePermission = this.ctrl.sharedBoardsWithWritePermission();
        final Iterable<SharedBoard> allboards = this.ctrl.allBoards();
        if (!allboards.iterator().hasNext()) {
            System.out.println("There are no Boards");
        } else if (!allUserBoardsWithWritePermission.iterator().hasNext()) {
            System.out.println("There are no Boards with Write Permission");
        } else {
            int cont = 1;
            System.out.println("SELECT Board to create PostIt\n");
            System.out.printf("%-6s%-15s%n", "No.", "Title");

            for (final SharedBoard board : allUserBoardsWithWritePermission) {
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
                    Set<Cell> cells = boardSelected.availableCells();
                    List<Cell> cellList = new ArrayList<>(cells);
                    Collections.sort(cellList);
                    List<Cell> list2 = new ArrayList<>();
                    int cont2 = 1;
                    System.out.println("\nSELECT Cell to create PostIt\n");
                    System.out.printf("%-6s%-6s%-6s%n", "No.", "Row", "Column");
                    for (final Cell cell : cellList) {
                        list2.add(cell);
                        System.out.printf("%-6d%-6d%-6d%n", cont2, cell.row().number(), cell.column().number());
                        cont2++;
                    }
                    final int option2 = Console.readInteger("Enter Cell or 0 to finish ");
                    if (option2 == 0) {
                        System.out.println("No Cell selected");
                    } else {
                        try {
                            Cell cell = list2.get(option2 - 1);
                            String content = Console.readLine("Enter the PostIt content:");
                            boolean success = ctrl.createPostIt(boardSelected, cell, new Content(content));
                            if (success) {
                                System.out.println("PostIt created with success!!\n");
                                System.out.println("SharedBoard after PostIt creation:\n");
                                ctrl.printBoardAsTable(boardSelected);
                            } else System.out.println("PostIt could not be created - cell in use");
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
        return "CREATE POSTIT";
    }
}