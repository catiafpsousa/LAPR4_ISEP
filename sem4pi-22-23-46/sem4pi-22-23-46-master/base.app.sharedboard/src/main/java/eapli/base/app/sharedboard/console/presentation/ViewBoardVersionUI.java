package eapli.base.app.sharedboard.console.presentation;

import eapli.base.app.sharedboard.console.sharedboard.application.ViewBoardVersionControllerTCP;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.BoardModification;
import eapli.base.boardmanagement.domain.SBPMessage;
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

    private TcpClient client;

    private static final Logger LOGGER = LoggerFactory.getLogger(eapli.base.app.common.console.presentation.sharedboard.ViewBoardUI.class);

    private ViewBoardVersionControllerTCP ctrl;

    public ViewBoardVersionUI(TcpClient client) {
        this.client = client;
        this.ctrl = new ViewBoardVersionControllerTCP(client);
    }


    @Override
    protected boolean doShow() {
        try {
            final List<SharedBoard> list = new ArrayList<>();
            final Iterable<SharedBoard> iterable = this.ctrl.listReadBoards();
            if (!iterable.iterator().hasNext()) {
                System.out.println("There are no Boards shared with you");
            } else {
                int cont = 1;
                System.out.println("SELECT Board to View a Past Version\n");
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
                        SBPMessage success1 = this.ctrl.viewBoardHistory(boardSelected);
                        Set<BoardModification> modifications = boardSelected.modifications();
                        System.out.printf("%-6s%-15s%-50s%-25s%-60s%n", "No.", "PostIt", "Timestamp", "Type", "Description");
                        List<BoardModification> historyList = new ArrayList<>();
                        modifications.forEach(historyList::add);
                        Collections.sort(historyList);
                        for (BoardModification mod : historyList) {
                            System.out.printf("%-6s%-15s%-50s%-25s%-60s%n", cont2, mod.postIt().id(), mod.timestamp().getTime(), mod.type(), mod.description());
                            cont2++;
                        }
                        final int option2 = Console.readInteger("\nSELECT Version to View or 0 to finish ");
                        if (option2 == 0) {
                            System.out.println("No Version selected");
                        } else {
                            BoardModification mod = historyList.get(option2 - 1);
                            SharedBoard version = boardSelected.boardVersion(boardSelected, mod.timestamp());

                            System.out.println("\nShowing Board: " + version.title() + " | Version timestamp: " + mod.timestamp().getTime() + "\n");
                            version.printSharedBoard();
                            return false;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return false;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String headline() {
        return "VIEW SHAREDBOARD PAST VERSION";
    }
}

