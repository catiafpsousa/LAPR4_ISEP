package eapli.base.app.common.console.presentation.sharedboard;

import eapli.base.boardmanagement.application.ViewBoardController;
import eapli.base.boardmanagement.application.ViewBoardHistoryController;
import eapli.base.boardmanagement.domain.BoardModification;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */

public class ViewBoardHistoryUI extends AbstractUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(eapli.base.app.common.console.presentation.sharedboard.ViewBoardUI.class);

    private final ViewBoardHistoryController ctrl = new ViewBoardHistoryController();


    @Override
    protected boolean doShow() {
        final List<SharedBoard> list = new ArrayList<>();
        final Iterable<SharedBoard> iterable = this.ctrl.sharedBoardsWithReadPermission();
        final Iterable<SharedBoard> allboards = this.ctrl.allBoards();
        if (!allboards.iterator().hasNext()) {
            System.out.println("There are no Boards");
        } else if (!iterable.iterator().hasNext()) {
            System.out.println("There are no Boards shared with you");
        } else {
            int cont = 1;
            System.out.println("SELECT Board to View Past Version\n");
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
                    Set<BoardModification> boardHistory = ctrl.boardHistory(boardSelected);
                    System.out.printf("%-6s%-15s%-50s%-25s%-60s%n", "No.", "PostIt", "Timestamp", "Type", "Description");
                    List<BoardModification> historyList = new ArrayList<>();
                    boardHistory.forEach(historyList::add);
                    Collections.sort(historyList);
                    for (BoardModification mod : historyList) {
                        System.out.printf("%-6s%-15s%-50s%-25s%-60s%n", mod.id(), mod.postIt().id(), mod.timestamp().getTime(), mod.type(), mod.description());
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
        return "VIEW SHAREDBOARD PAST VERSION";
    }
}

