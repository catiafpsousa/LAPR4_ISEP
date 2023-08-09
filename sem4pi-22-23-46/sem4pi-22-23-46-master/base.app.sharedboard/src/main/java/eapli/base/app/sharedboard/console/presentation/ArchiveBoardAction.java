package eapli.base.app.sharedboard.console.presentation;

import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.framework.actions.Action;

public class ArchiveBoardAction implements Action {

    private TcpClient client;

    public ArchiveBoardAction(TcpClient client){
        this.client=client;
    }

    @Override
    public boolean execute() {
        return new ArchiveBoardUI(client).doShow();
    }

}
