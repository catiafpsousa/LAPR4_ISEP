package eapli.base.app.common.console.presentation.sharedboard;

import eapli.base.boardmanagement.application.CreatePostItController;
import eapli.base.boardmanagement.application.ViewBoardController;
import eapli.base.boardmanagement.domain.Cell;
import eapli.base.boardmanagement.domain.Content;
import eapli.base.boardmanagement.domain.SharedBoard;
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
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */

public class ViewBoardUI extends AbstractUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewBoardUI.class);

    private final ViewBoardController ctrl = new ViewBoardController();


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
            System.out.println("SELECT Board to View\n");
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