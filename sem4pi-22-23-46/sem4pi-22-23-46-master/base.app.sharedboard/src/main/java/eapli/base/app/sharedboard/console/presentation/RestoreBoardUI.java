package eapli.base.app.sharedboard.console.presentation;

import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.base.app.sharedboard.console.sharedboard.application.RestoreBoardControllerTCP;

import java.util.ArrayList;
import java.util.List;

public class RestoreBoardUI extends AbstractUI {
    private TcpClient client;

    private RestoreBoardControllerTCP controllerTCP;

    public RestoreBoardUI(TcpClient client){
        this.client=client;
        this.controllerTCP=new RestoreBoardControllerTCP(client);
    }

    @Override
    protected boolean doShow(){
        try{
            final List<SharedBoard> boards = new ArrayList<>();
            final Iterable<SharedBoard> iterable;
            iterable = this.controllerTCP.allBoardsArchivedByUser();
            if(!iterable.iterator().hasNext()){
                System.out.println("There are no boards available");
            }else {
                int cont = 1;
                System.out.println("SELECT Board to Restore:\n");
                System.out.printf("%-6s%-30s%n", "No.:", "BOARD_TITLE");
                for (final SharedBoard board : iterable) {
                    boards.add(board);
                    System.out.printf("%-6d%-30s%n", cont, board.title().title());
                    cont++;
                }
            }
            final int option = Console.readInteger("Choose one board to restore or 0 to finish:");
            if(option == 0){
                System.out.println("No Board selected");
            }else{
                SharedBoard boardSelected = boards.get(option - 1);
                System.out.printf("Are you sure you want to restore this board %s? ", boardSelected.title().title().toUpperCase());
                String answer = Console.readLine("(S/N)");
                if(answer.equalsIgnoreCase("S")) {
                    SBPMessage message = controllerTCP.restoreBoard(boardSelected);
                    if (message.getCode().equals(SBPMessage.Code.ERR)) {
                        System.out.println("Board not restored.");
                    } else if (message.getCode().equals(SBPMessage.Code.SUCCESS_RESTORE_BOARD)) {
                        System.out.println("Board restored successfully.");
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
        return "Restore Board";
    }

}
