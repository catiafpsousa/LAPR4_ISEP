package eapli.base.app.sharedboard.console.sharedboard.application;

import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.framework.application.UseCaseController;

import java.io.IOException;

@UseCaseController
public class RestoreBoardControllerTCP {

    final TcpClient client;

    public RestoreBoardControllerTCP(TcpClient client){
        this.client=client;
    }

    public SBPMessage restoreBoard(final SharedBoard board) throws IOException {
        return client.restoreBoard(board);
    }


    public Iterable<SharedBoard>allBoardsArchivedByUser() throws IOException, ClassNotFoundException {
        return client.listBoardsArchived();
    }


}
