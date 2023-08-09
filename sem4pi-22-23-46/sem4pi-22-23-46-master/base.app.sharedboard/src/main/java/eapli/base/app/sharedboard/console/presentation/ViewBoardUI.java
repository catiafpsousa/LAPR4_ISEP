package eapli.base.app.sharedboard.console.presentation;

import eapli.base.app.sharedboard.console.sharedboard.application.ViewBoardControllerTCP;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.util.UiUtils;
import eapli.framework.presentation.console.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ViewBoardUI extends AbstractUI {

    private TcpClient client;

    private static final Logger LOGGER = LoggerFactory.getLogger(eapli.base.app.common.console.presentation.sharedboard.ViewBoardUI.class);

    private ViewBoardControllerTCP ctrl;

    public ViewBoardUI(TcpClient client) {
        this.client = client;
        this.ctrl = new ViewBoardControllerTCP(client);
    }

    @Override
    protected boolean doShow() {
        try {
            Iterable<SharedBoard> boards = ctrl.listBoards();
            if (!boards.iterator().hasNext()) {
                System.out.println("There are no boards available.");
            } else {
                final SharedBoard board = (SharedBoard) UiUtils.showAndSelectOne((List) boards, "Select the board you want to view");
                ctrl.selectSharedBoard(board.identity());
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");
        }

        return false;
    }

    @Override
    public String headline() {
        return "View Shared Board";
    }
}
