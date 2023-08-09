package eapli.base.app.sharedboard.console.sharedboard.application;

import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.framework.application.UseCaseController;

import java.io.IOException;

@UseCaseController
public class ArchiveBoardControllerTCP {

    final TcpClient client;

    public ArchiveBoardControllerTCP(TcpClient client){
        this.client=client;
    }

    public SBPMessage archiveBoard(final SharedBoard board) throws IOException {
        return client.archiveBoard(board);
    }

    public Iterable<SharedBoard> allBoardsOwnedByUser() throws IOException, ClassNotFoundException {
        return client.listBoardsThatOwns();
    }

    }
