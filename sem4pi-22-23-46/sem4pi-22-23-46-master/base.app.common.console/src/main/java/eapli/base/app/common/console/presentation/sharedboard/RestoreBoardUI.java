package eapli.base.app.common.console.presentation.sharedboard;

import eapli.base.app.common.console.presentation.meeting.ListParticipantsInMeetingUI;
import eapli.base.boardmanagement.application.ArchiveBoardController;
import eapli.base.boardmanagement.application.RestoreBoardController;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RestoreBoardUI extends AbstractUI {

    private RestoreBoardController controller = new RestoreBoardController();
    private static final Logger LOGGER = LoggerFactory.getLogger(ListParticipantsInMeetingUI.class);

    @Override
    protected boolean doShow() {
        final List<SharedBoard> boards = new ArrayList<>();
        final Iterable<SharedBoard> iterable = this.controller.allBoardsArchivedOwnedByUser();
        if(!iterable.iterator().hasNext()){
            System.out.println("There are no boards available");
        }else{
            int cont = 1;
            System.out.println("SELECT Board to Restore:\n");
            System.out.printf("%-6s%-30s%n", "No.:", "BOARD_TITLE");
            for(final SharedBoard board : iterable) {
                boards.add(board);
                System.out.printf("%-6d%-30s%n", cont, board.title().title());
                cont++;
            }
            final int option = Console.readInteger("Choose one board to restore or 0 to finish:");
            if(option == 0){
                System.out.println("No Board selected");
            }else{
                try {
                    SharedBoard boardSelected = boards.get(option - 1);
                    System.out.println("Are you sure you want to restore this board?");
                    System.out.printf("%-30s%n", boardSelected.title().title());
                    String answer = Console.readLine("(Y/N)");
                    if(answer.equalsIgnoreCase("Y")){
                        this.controller.restoreBoard(boardSelected);
                        System.out.println("Board restored successfully.");
                    }else{
                        System.out.println("Board not restored.");
                    }
                } catch(IntegrityViolationException | ConcurrencyException e){
                    LOGGER.error("Error performing the operation.", e);
                    System.out.println("Something went wrong.");
                }
            }
        }
        return false;
    }

    @Override
    public String headline() {
        return "Restore Board";
    }
}


