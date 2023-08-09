package eapli.base.app.common.console.presentation.sharedboard;

import eapli.base.boardmanagement.application.ViewBoardVersionController;
import eapli.base.boardmanagement.domain.BoardModification;
import eapli.base.boardmanagement.domain.SharedBoard;
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

public class ViewBoardVersionUI extends AbstractUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewBoardVersionUI.class);

    private final ViewBoardVersionController ctrl = new ViewBoardVersionController();


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
            System.out.println("SELECT Board to View past version\n");
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
                    int cont2 = 1;
                    SharedBoard boardSelected = list.get(option - 1);
                    System.out.printf("Selected SharedBoard: %-20s\n\n", boardSelected.title());
                    Set<BoardModification> boardHistory = ctrl.boardHistory(boardSelected);
                    System.out.printf("%-6s%-15s%-50s%-25s%-60s%n", "Nº.", "PostIt", "Timestamp", "Type", "Description");
                    List<BoardModification> historyList = new ArrayList<>();
                    boardHistory.forEach(historyList::add);
                    Collections.sort(historyList);
                    for (BoardModification mod : historyList) {
                        System.out.printf("%-6s%-15s%-50s%-25s%-60s%n", cont2, mod.postIt().id(), mod.timestamp().getTime(), mod.type(), mod.description());
                        cont2++;
                    }
                    final int option2 = Console.readInteger("\nSELECT Version to View or 0 to finish ");
                    if (option2 == 0) {
                        System.out.println("No Version selected");
                    } else {
                        BoardModification mod = historyList.get(option2-1);
                        SharedBoard boardVersion = ctrl.boardVersion(boardSelected, mod.timestamp());
                        System.out.println("\nShowing Board: " + boardVersion.title() + " | Version timestamp: " + mod.timestamp().getTime() + "\n" );
                        ctrl.printBoardAsTable(boardVersion);
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
        return "VIEW SHAREDBOARD";
    }
}