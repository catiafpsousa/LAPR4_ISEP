package eapli.base.app.sharedboard.console.presentation;

import eapli.base.app.sharedboard.console.sharedboard.application.CreatePostItControllerTCP;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.Cell;
import eapli.base.boardmanagement.domain.Content;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.util.UiUtils;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import java.io.File;
import java.io.IOException;

/**
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */

public class CreatePostItUI extends AbstractUI {
    private TcpClient client;

    private final CreatePostItControllerTCP ctrl;

    public CreatePostItUI(TcpClient client) {
        this.client = client;
        this.ctrl = new CreatePostItControllerTCP(client);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePostItUI.class);

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


    @Override
    protected boolean doShow() {
        try {
            List<SharedBoard> list = new ArrayList<>();
            Iterable<SharedBoard> allUserBoardsWithWritePermission = this.ctrl.listBoards();
            if (!allUserBoardsWithWritePermission.iterator().hasNext()) {
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

                    SharedBoard boardSelected = list.get(option - 1);
                    while(boardSelected.isLocked()){
                        list = new ArrayList<>();
                        allUserBoardsWithWritePermission = this.ctrl.listBoards();
                        for (final SharedBoard board : allUserBoardsWithWritePermission) {
                            list.add(board);
                            cont++;
                        }
                        boardSelected = list.get(option - 1);
                    }
                    ctrl.lockBoard(boardSelected);

                    System.out.printf("Selected SharedBoard: %-20s\n\n", boardSelected.title());
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
                            String textContent = null;
                            byte[] imageContent = null;
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

                            if (enterTextContent && !enterImageContent) {
                                success = ctrl.createPostIt(boardSelected, cell, new Content(textContent));
                            } else if (!enterTextContent && enterImageContent) {
                                success = ctrl.createPostIt(boardSelected, cell, new Content(imageContent));
                            } else if (enterTextContent && enterImageContent) {
                                success = ctrl.createPostIt(boardSelected, cell, new Content(textContent, imageContent));
                            } else {
                                System.out.println("Operation Canceled");
                                return false;
                            }

                            if (success.getCode().equals(SBPMessage.Code.SUCCESS_SAVE_BOARD)) {
//                                boardSelected.stopContinuousLoop();
                                ctrl.unLockBoard(boardSelected);
                                final List<SharedBoard> listAgain = new ArrayList<>();
                                final Iterable<SharedBoard> boardsAgain = this.ctrl.listBoards();
                                for (final SharedBoard board : boardsAgain) {
                                    listAgain.add(board);
                                }
                                SharedBoard modifiedBoard = listAgain.get(option - 1);
                                modifiedBoard.printSharedBoard();
                                return false;
                            } else if (success.getCode().equals(SBPMessage.Code.ERR)) {
                                System.out.println(success.getData());
                                System.out.println("PostIt could not be created - cell in use");
                                final List<SharedBoard> listAgain2 = new ArrayList<>();
                                final Iterable<SharedBoard> boardsAgain2 = this.ctrl.listBoards();
                                for (final SharedBoard board : boardsAgain2) {
                                    listAgain2.add(board);
                                }
                                SharedBoard modifiedBoard = listAgain2.get(option - 1);
                                modifiedBoard.printSharedBoard();
                                return false;
                            }
                        } catch (IntegrityViolationException | ConcurrencyException ex) {
                            LOGGER.error("Error performing the operation", ex);
                            System.out.println("Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");
                            return false;
                        }
                    }

                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public String headline() {
        return "CREATE POSTIT";
    }
}
