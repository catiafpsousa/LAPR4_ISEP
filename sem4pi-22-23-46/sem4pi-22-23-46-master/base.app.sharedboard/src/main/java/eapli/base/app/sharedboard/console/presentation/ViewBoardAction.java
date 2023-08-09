package eapli.base.app.sharedboard.console.presentation;

import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.framework.actions.Action;

public class ViewBoardAction implements Action {
    private TcpClient client;

    public ViewBoardAction(TcpClient client) {
        this.client = client;
    }

    @Override
    public boolean execute() {
        return new ViewBoardUI(client).doShow();
    }
}

