package eapli.base.app.sharedboard.console.presentation;

import eapli.base.app.sharedboard.console.sharedboard.application.UndoPostItControllerTCP;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.application.UndoPostItLastChangeController;
import eapli.base.boardmanagement.domain.Cell;
import eapli.base.boardmanagement.domain.PostIt;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
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
    private TcpClient client;

    private final UndoPostItControllerTCP ctrl;
    private SharedBoard boardSelected;

    public UndoPostItLastChangeUI(TcpClient client) {
        this.client = client;
        this.ctrl = new UndoPostItControllerTCP(client);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UndoPostItLastChangeUI.class);


    @Override
    protected boolean doShow() {
        try {
            final List<SharedBoard> list = new ArrayList<>();
            final Iterable<SharedBoard> iterable = this.ctrl.listBoards();

            if (!iterable.iterator().hasNext()) {
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
                        Iterable<Cell> cells = boardSelected.cellsAlsoWithDeletedOfBoardAndUser(client.userSession);
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

                                SBPMessage success = ctrl.undoPostIt(boardSelected, postIt);
                                if (success.getCode().equals(SBPMessage.Code.SUCCESS_SAVE_BOARD)) {
                                    final List<SharedBoard> listAgain = new ArrayList<>();
                                    final Iterable<SharedBoard> boardsAgain = this.ctrl.listBoards();
                                    for (final SharedBoard board : boardsAgain) {
                                        listAgain.add(board);
                                    }
                                    SharedBoard modifiedBoard = listAgain.get(option - 1);
                                    modifiedBoard.printSharedBoard();
//                                    //boolean modification_success = SharedBoard.verifySharedBoardsEqual(boardSelected, modifiedBoard);
////                                    boolean modification_success = boardSelected.equals(modifiedBoard);
//                                    boolean modification_success = false;
//                                    Cell parentCell = null;
//                                    Cell actualCell = null;
//
//                                    for (Cell c : boardSelected.cells()) {
//                                        if (c.postItList().contains(postIt.parent())) {
//                                            parentCell = c;
//                                            break;
//                                        }
//                                    }
//
//                                    for (Cell c : boardSelected.cells()) {
//                                        if (c.postItList().contains(postIt)) {
//                                            actualCell = c;
//                                            break;
//                                        }
//                                    }
//
//                                    if (parentCell == null) {
//                                        modification_success = false;
//                                    } else if (!actualCell.equals(parentCell)) {
//                                        if (!boardSelected.availableCells().contains(parentCell)) {
//                                            modification_success = true;
//                                        }else{
//                                            modification_success = true;
//                                        }
//                                    }
//
//                                    if (modification_success) {
//                                        System.out.println("Undo Operation performed with success!!");
//                                    }else{
//                                        System.out.println("Undo Operation could not be performed -- ParentCell is in use OR PostIt has no previous version");
//                                    }
                                    System.out.println("Undo performed");
                                    System.out.println("Please, verify board changes");
                                    System.out.println("If there is no previous version or parent cell was already in use by other postIt, operation had no effects");
                                } else
                                    System.out.println("UNDO ERROR");
                            } catch (IntegrityViolationException | ConcurrencyException ex) {
                                LOGGER.error("Error performing the operation", ex);
                                System.out.println("Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");
                                return false;
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return false;
                }

            }
        }         catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    @Override
    public String headline() {
        return "UNDO LAST POSTIT CHANGE";
    }
}
