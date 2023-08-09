package eapli.base.app.sharedboard.console.sharedboard.application;

import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.*;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

import java.io.IOException;

public class UpdatePostItControllerTCP {
    final TcpClient client;

    public UpdatePostItControllerTCP(TcpClient client) {
        this.client = client;
    }

    public Iterable <SharedBoard> listBoards() throws IOException, ClassNotFoundException {
        return this.client.listWriteBoards();
    }

    public SystemUser currentUser() throws IOException, ClassNotFoundException {
        return this.client.userSession;
    }

    public SBPMessage updatePostItContent(SharedBoard board, Cell cell, PostIt postIt, Content newContent) throws IOException, ClassNotFoundException{
        return this.client.sendUpdatePostItMessage(board, cell, postIt, newContent);
    }

    public SBPMessage movePostIt(SharedBoard board, Cell oldCell, PostIt postIt,  Cell newCell) throws IOException,ClassNotFoundException {
        return this.client.sendMovePostItMessage(board, oldCell,postIt, newCell);

    }

    public SBPMessage deletePostIt(SharedBoard board, Cell cell, PostIt postIt) throws IOException, ClassNotFoundException{
        return this.client.sendDeletePostItMessage(board, cell, postIt);
    }


}
