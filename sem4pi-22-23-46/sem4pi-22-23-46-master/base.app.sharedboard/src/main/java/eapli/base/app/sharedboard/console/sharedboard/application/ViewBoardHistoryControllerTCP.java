package eapli.base.app.sharedboard.console.sharedboard.application;

import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;

import java.io.IOException;

public class ViewBoardHistoryControllerTCP {
    final TcpClient client;

    public ViewBoardHistoryControllerTCP(TcpClient client) {
        this.client = client;
    }

    public Iterable <SharedBoard> listReadBoards() throws IOException, ClassNotFoundException {
        return this.client.listReadBoards();
    }

    public SBPMessage viewBoardHistory (SharedBoard board) throws IOException, ClassNotFoundException {
        return this.client.sendViewBoardHistoryMessage(board);
    }


}
