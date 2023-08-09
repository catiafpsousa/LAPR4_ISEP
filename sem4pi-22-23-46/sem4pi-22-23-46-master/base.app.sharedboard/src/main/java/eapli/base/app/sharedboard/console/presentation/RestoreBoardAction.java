package eapli.base.app.sharedboard.console.presentation;

import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.framework.actions.Action;

public class RestoreBoardAction implements Action {

private TcpClient client;

public RestoreBoardAction(TcpClient client){
    this.client=client;
}

@Override
public boolean execute() {
    return new RestoreBoardUI(client).doShow();
}

}
