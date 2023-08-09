package eapli.base.app.sharedboard.console.presentation;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.framework.actions.Action;

public class ShareABoardAction implements Action {
    private TcpClient client;
    public ShareABoardAction (TcpClient client){
        this.client=client;
    }

    @Override
    public boolean execute() {
        return new ShareABoardUI(client).doShow();
    }
}
