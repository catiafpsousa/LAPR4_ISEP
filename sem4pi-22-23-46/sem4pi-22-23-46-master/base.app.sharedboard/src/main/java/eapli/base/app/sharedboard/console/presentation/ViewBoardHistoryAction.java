package eapli.base.app.sharedboard.console.presentation;

import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.framework.actions.Action;

public class ViewBoardHistoryAction implements Action {
    private TcpClient client;

    public ViewBoardHistoryAction(TcpClient client){
        this.client=client;
    }
    @Override
    public boolean execute() {
        return new ViewBoardHistoryUI(client).doShow();
    }
}
