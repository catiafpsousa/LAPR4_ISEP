package eapli.base.app.sharedboard.console.presentation;

import eapli.base.app.sharedboard.console.sharedboard.application.UpdatePostItControllerTCP;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.*;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.util.UiUtils;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */

public class UpdatePostItUI extends AbstractUI {
    private TcpClient client;

    private final UpdatePostItControllerTCP ctrl;

    public UpdatePostItUI(TcpClient client) {
        this.client = client;
        this.ctrl = new UpdatePostItControllerTCP(client);
    }

    private String openFileExplorer() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Image File");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        } else {
            System.out.println("No image file selected.");
            return null;
        }
    }

    private byte[] readImageFile(File imageFile) {
        // Implement the logic to read the image file and convert it to byte[]
        // Here's a sample implementation using Java's FileInputStream:

        try (FileInputStream fis = new FileInputStream(imageFile)) {
            byte[] imageContent = new byte[(int) imageFile.length()];
            fis.read(imageContent);
            return imageContent;
        } catch (IOException e) {
            System.out.println("Error reading image file.");
            return null;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePostItUI.class);


    @Override
    protected boolean doShow() {
        try {
            final SystemUser currentUser = ctrl.currentUser();
            final List<SharedBoard> list = new ArrayList<>();
            final Iterable<SharedBoard> iterable = this.ctrl.listBoards();
            if (!iterable.iterator().hasNext()) {
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

                        Iterable<Cell> cells = boardSelected.cellOfBoardAndUser(currentUser);
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
                                if (latestPostIt != null) {
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
                                        String textContent = null;
                                        byte[] imageContent = null;
                                        System.out.println("Replacing all current content...");
                                        boolean enterTextContent = UiUtils.confirm("Do you want to add text content? (S/N)");
                                        if (enterTextContent) {
                                            textContent = Console.readLine("Enter the PostIt content:");
                                        }
                                        boolean enterImageContent = UiUtils.confirm("Do you want to add image content? (S/N)");
                                        if (enterImageContent) {
                                            String imagePath = openFileExplorer();
                                            if (imagePath != null) {
                                                // Read the image file and convert it to byte[]
                                                File imageFile = new File(imagePath);
                                                imageContent = readImageFile(imageFile);
                                            } else {
                                                System.out.println("No image file selected.");
                                            }
                                        }

                                        SBPMessage success = null;

                                        if (enterTextContent && !enterImageContent){
                                            success = ctrl.updatePostItContent(boardSelected, cell, postIt, new Content(textContent));
                                        }
                                        else if (!enterTextContent && enterImageContent){
                                            success = ctrl.updatePostItContent(boardSelected, cell, postIt, new Content(imageContent));
                                        }else if (enterTextContent && enterImageContent){
                                            success = ctrl.updatePostItContent(boardSelected, cell, postIt, new Content(textContent, imageContent));
                                        }else{
                                            System.out.println("Operation Canceled");
                                            return false;
                                        }



                                        boardSelected.printSharedBoard();
                                        if (success.getCode().equals(SBPMessage.Code.SUCCESS_SAVE_BOARD)) {
                                            System.out.println("PostIt content updated with success!!\n");
                                            System.out.println("SharedBoard after PostIt content update operation:\n");
                                            final List<SharedBoard> listAgain = new ArrayList<>();
                                            final Iterable<SharedBoard> boardsAgain = this.ctrl.listBoards();
                                            for (final SharedBoard board : boardsAgain) {
                                                listAgain.add(board);
                                            }
                                            SharedBoard modifiedBoard = listAgain.get(option - 1);
                                            modifiedBoard.printSharedBoard();
                                        } else System.out.println("PostIt could not be updated");


                                    } else if (updateField == 2) {
                                        Set<Cell> otherCells = boardSelected.availableCells();
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
                                            SBPMessage success = ctrl.movePostIt(boardSelected, cell, postIt, moveToCell);
                                            boardSelected.printSharedBoard();
                                            if (success.getCode().equals(SBPMessage.Code.SUCCESS_SAVE_BOARD)) {
                                                System.out.println("PostIt moved with success");
                                                System.out.println("SharedBoard after PostIt move operation:\n");
                                                final List<SharedBoard> listAgain = new ArrayList<>();
                                                final Iterable<SharedBoard> boardsAgain = this.ctrl.listBoards();
                                                for (final SharedBoard board : boardsAgain) {
                                                    listAgain.add(board);
                                                }
                                                SharedBoard modifiedBoard = listAgain.get(option - 1);
                                                modifiedBoard.printSharedBoard();
                                            } else System.out.println("Moving error");

                                        }


                                    } else if (updateField == 3) {
                                        SBPMessage success = ctrl.deletePostIt(boardSelected, cell, postIt);
                                        boardSelected.printSharedBoard();
                                        if (success.getCode().equals(SBPMessage.Code.SUCCESS_SAVE_BOARD)) {
                                            System.out.println("PostIt deleted with success");
                                            System.out.println("SharedBoard after delete PostIt operation:\n");
                                            final List<SharedBoard> listAgain = new ArrayList<>();
                                            final Iterable<SharedBoard> boardsAgain = this.ctrl.listBoards();
                                            for (final SharedBoard board : boardsAgain) {
                                                listAgain.add(board);
                                            }
                                            SharedBoard modifiedBoard = listAgain.get(option - 1);
                                            modifiedBoard.printSharedBoard();
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public String headline () {
        return "UPDATE POSTIT";
    }
}
