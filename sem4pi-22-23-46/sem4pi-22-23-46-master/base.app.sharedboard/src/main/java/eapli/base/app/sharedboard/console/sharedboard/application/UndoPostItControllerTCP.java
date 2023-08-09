package eapli.base.app.sharedboard.console.sharedboard.application;

import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.PostIt;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

import java.io.IOException;

public class UndoPostItControllerTCP {
    final TcpClient client;

    public UndoPostItControllerTCP(TcpClient client) {
        this.client = client;
    }

    public Iterable <SharedBoard> listBoards() throws IOException, ClassNotFoundException {
        return this.client.listWriteBoards();
    }


    public SBPMessage undoPostIt (SharedBoard board, PostIt postIt) throws IOException, ClassNotFoundException {
        return this.client.sendUndoPostItMessage(board, postIt);
    }

}
