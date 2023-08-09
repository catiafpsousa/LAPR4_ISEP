package eapli.base.app.sharedboard.console.sharedboard.application;

import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.Cell;
import eapli.base.boardmanagement.domain.Content;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;

import java.io.IOException;

public class CreatePostItControllerTCP {
    final TcpClient client;

    public CreatePostItControllerTCP(TcpClient client) {
        this.client = client;
    }

    public Iterable <SharedBoard> listBoards() throws IOException, ClassNotFoundException {
        return this.client.listWriteBoards();
    }

    public SBPMessage createPostIt (SharedBoard board, Cell cell, Content content) throws IOException, ClassNotFoundException {
        return this.client.sendCreatePostItMessage(board, cell, content);
    }

    public SBPMessage lockBoard (SharedBoard board) throws IOException {
        return this.client.sendLockBoardMessage(board);
    }


    public SBPMessage unLockBoard (SharedBoard board) throws IOException {
        return this.client.sendUnLockBoardMessage(board);
    }


}
