package eapli.base.app.sharedboard.console.sharedboard.application;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;

import java.io.IOException;
import java.util.Calendar;

public class ViewBoardVersionControllerTCP {
    final TcpClient client;

    public ViewBoardVersionControllerTCP(TcpClient client) {
        this.client = client;
    }

    public Iterable <SharedBoard> listReadBoards() throws IOException, ClassNotFoundException {
        return this.client.listReadBoards();
    }

    public SBPMessage viewBoardHistory (SharedBoard board) throws IOException, ClassNotFoundException {
        return this.client.sendViewBoardHistoryMessage(board);
    }

}
