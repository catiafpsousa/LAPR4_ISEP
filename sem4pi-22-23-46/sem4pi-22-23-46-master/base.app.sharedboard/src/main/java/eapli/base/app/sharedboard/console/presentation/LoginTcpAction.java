package eapli.base.app.sharedboard.console.presentation;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.framework.actions.Action;

public class LoginTcpAction implements Action {
  private TcpClient client;

    public LoginTcpAction(TcpClient client){
        this.client=client;
    }
    @Override
    public boolean execute() {
        return new LoginTcpUI(client).doShow();

    }
}
