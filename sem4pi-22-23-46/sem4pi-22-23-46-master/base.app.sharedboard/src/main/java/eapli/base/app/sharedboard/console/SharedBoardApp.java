package eapli.base.app.sharedboard.console;

import eapli.base.app.sharedboard.console.presentation.LoginTcpAction;
import eapli.base.app.common.console.BaseApplication;
import eapli.base.app.sharedboard.console.presentation.MainMenu;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.framework.infrastructure.pubsub.EventDispatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SharedBoardApp extends BaseApplication {
    private static final Logger LOGGER = LogManager.getLogger(SharedBoardApp.class);


    private SharedBoardApp (){
        //Empty constructor
    }

    public static void main(String[] args) {
        new SharedBoardApp().run(args);
    }

    @Override
    protected void doMain(String[] args) throws Exception {
        TcpClient client = new TcpClient();
        client.runTcpClient();
        boolean validConnection = client.receiveACkMessage();
        if(validConnection){
            System.out.printf("\n==========ACK MESSAGE RECEIVED: Valid connection to server==========\n\n\n");
            if(new LoginTcpAction(client).execute()){
                System.out.printf("\n==========ACK MESSAGE RECEIVED: Valid username and password==========\n\n\n");
                final MainMenu menu= new MainMenu(client);
                menu.mainLoop();
            } else {
                System.out.println("\n==========INVALID LOGIN: sending disconnect message to server==========\n\n");
                client.sendDisconnectMessage();
            }
        }
    }

    @Override
    protected String appTitle() {
        return "Shared Board Application";
    }

    @Override
    protected String appGoodbye() {
        return "Thank you for using 'Shared Board Application";
    }

    @Override
    protected void doSetupEventHandlers(EventDispatcher dispatcher) {

    }
}
