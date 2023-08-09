package eapli.base.app.sharedboard.console.presentation;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.framework.actions.Action;

import java.io.IOException;

public class ReturnAction implements Action {
    private final TcpClient client;

    public ReturnAction(TcpClient client) {
        this.client = client;
    }

    @Override
    public boolean execute() {
        SBPMessage disconnMessage = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.DISCONN, 0, 0,null);
        try {
            System.out.println("\n" +
                    "=====================SENDING DISCONNECT MESSAGE=====================");
            client.sendMessage(disconnMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}