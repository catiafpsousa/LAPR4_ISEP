package eapli.base.app.common.console.presentation.sharedboard;

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

import java.util.*;


/**
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */

public class UpdatePostItUI extends AbstractUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePostItUI.class);

    private final UpdatePostItController ctrl = new UpdatePostItController();


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
            System.out.println("SELECT Board to update PostIt\n");
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
                    if (!cells.iterator().hasNext()) {
                        System.out.println("There are no PostIts or you don't have PostIts in this board.\n");
                    } else {
                        List<Cell> cellsOrdered = new ArrayList<>();
                        cells.forEach(cellsOrdered::add);
                        Collections.sort(cellsOrdered);
                        List<PostIt> postItList = new ArrayList<>();
                        List<Cell> cellList = new ArrayList<>();
                        int cont2 = 1;
                        System.out.println("SELECT PostIt to Update\n");
                        System.out.printf("%-6s%-6s%-10s%-20s%n", "No.", "Row", "Column", "Content");
                        for (final Cell cell : cellsOrdered) {
                            PostIt latestPostIt = cell.latestActivePostIt();
                            if (latestPostIt!=null) {
                                if (!latestPostIt.isDeleted()) {
                                    cellList.add(cell);
                                    postItList.add(latestPostIt);
                                    System.out.printf("%-6d%-6d%-10d%-20s%n", cont2, cell.row().number(), cell.column().number(), latestPostIt.content().toString());
                                    cont2++;
                                }
                            }
                        }
                        final int option2 = Console.readInteger("Enter PostIt or 0 to finish ");
                        if (option2 == 0) {
                            System.out.println("No PostIt selected");
                        } else {
//                    final int option3 = Console.readInteger("Enter Cell or 0 to finish ");
//                    if (option3 == 0) {
//                        System.out.println("No Cell selected");
                            try {
                                PostIt postIt = postItList.get(option2 - 1);
                                Cell cell = cellList.get(option2 - 1);
                                final int updateField = Console.readInteger("1 - PostIt Content  ||  2 - Placement  ||  3 - Delete PostIt ");


                                if (updateField == 1) {
                                    String content = Console.readLine("Enter the PostIt update content:");
                                    boolean success = ctrl.updatePostItContent(boardSelected, cell, postIt, new Content(content));
                                    if (success) {
                                        System.out.println("PostIt content updated with success!!\n");
                                        System.out.println("SharedBoard after PostIt content update operation:\n");
                                        ctrl.printBoardAsTable(boardSelected);
                                    } else System.out.println("PostIt could not be updated");


                                } else if (updateField == 2) {
                                    Set<Cell> otherCells = ctrl.availableCells(boardSelected);
                                    List<Cell> availableCellsList = new ArrayList<>(otherCells);
                                    Collections.sort(availableCellsList);
                                    List<Cell> list2 = new ArrayList<>();
                                    int cont3 = 1;
                                    System.out.println("SELECT Cell to move PostIt\n");
                                    System.out.printf("%-6s%-6s%-6s%n", "No.", "Row", "Column");
                                    for (final Cell availableCell : availableCellsList) {
                                        list2.add(availableCell);
                                        System.out.printf("%-6d%-6d%-6d%n", cont3, availableCell.row().number(), availableCell.column().number());
                                        cont3++;
                                    }
                                    final int option3 = Console.readInteger("Enter Cell or 0 to finish ");
                                    if (option3 == 0) {
                                        System.out.println("No Cell selected");
                                    } else {
                                        Cell moveToCell = list2.get(option3 - 1);
                                        boolean success = ctrl.movePostIt(boardSelected, cell, postIt, moveToCell);
                                        if (success) {
                                            System.out.println("PostIt moved with success");
                                            System.out.println("SharedBoard after PostIt move operation:\n");
                                            ctrl.printBoardAsTable(boardSelected);
                                        } else System.out.println("Moving error");

                                    }


                                } else if (updateField == 3) {
                                    boolean success = ctrl.deletePostIt(boardSelected, cell, postIt);
                                    if (success) {
                                        System.out.println("PostIt deleted with success");
                                        System.out.println("SharedBoard after delete PostIt operation:\n");
                                        ctrl.printBoardAsTable(boardSelected);
                                    } else System.out.println("PostIt could not be deleted");


                                } else {
                                    System.out.println("Update Operation Canceled");
                                }
                            } catch (IntegrityViolationException | ConcurrencyException ex) {
                                LOGGER.error("Error performing the operation", ex);
                                System.out.println("Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");
                            }
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
        return "UPDATE POSTIT";
    }
}