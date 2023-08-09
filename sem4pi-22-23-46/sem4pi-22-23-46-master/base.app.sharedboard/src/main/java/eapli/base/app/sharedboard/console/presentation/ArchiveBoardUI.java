package eapli.base.app.sharedboard.console.presentation;

import eapli.base.app.sharedboard.console.sharedboard.application.ArchiveBoardControllerTCP;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cátia Sousa 1210849@isep.pt
 */

public class ArchiveBoardUI extends AbstractUI {

    private TcpClient client;

    private ArchiveBoardControllerTCP controllerTCP;

    public ArchiveBoardUI(TcpClient client){
        this.client=client;
        this.controllerTCP = new ArchiveBoardControllerTCP(client);
    }


    @Override
    protected boolean doShow() {
        try{
            final List<SharedBoard> boards = new ArrayList<>();
            final Iterable<SharedBoard> iterable;
            iterable = this.controllerTCP.allBoardsOwnedByUser();
            if(!iterable.iterator().hasNext()){
            System.out.println("There are no boards available");
            }else {
                int cont = 1;
                System.out.println("SELECT Board to Archive:\n");
                System.out.printf("%-6s%-30s%n", "No.:", "BOARD_TITLE");
                for (final SharedBoard board : iterable) {
                    boards.add(board);
                    System.out.printf("%-6d%-30s%n", cont, board.title().title());
                    cont++;
                }
            }
            final int option = Console.readInteger("Choose one board to archive or 0 to finish:");
            if(option == 0){
                System.out.println("No Board selected");
            }else{
                    SharedBoard boardSelected = boards.get(option - 1);
                    System.out.printf("Are you sure you want to archive this board %s? ", boardSelected.title().title().toUpperCase());
                    String answer = Console.readLine("(S/N)");
                    if(answer.equalsIgnoreCase("S")) {
                        SBPMessage message = controllerTCP.archiveBoard(boardSelected);
                        if (message.getCode().equals(SBPMessage.Code.ERR)) {
                            System.out.println("Board not archived.");
                        } else if (message.getCode().equals(SBPMessage.Code.SUCCESS_ARCHIVE_BOARD)) {
                            System.out.println("Board archived successfully.");
                        }
                }
                }
            }catch (Exception ex){
                System.out.println(ex.getMessage());
                System.out.println("Something went wrong.");
        }
        return false;
    }

    @Override
    public String headline() {
        return "Archive Board";
    }
}
