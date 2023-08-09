package eapli.base.app.sharedboard.console.sharedboard.application;

import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.framework.application.UseCaseController;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;

import java.io.IOException;
import java.util.List;

@UseCaseController
public class CreateBoardControllerTCP {
    final TcpClient client;

    public CreateBoardControllerTCP(TcpClient client){
        this.client=client;
    }

    public SBPMessage createSharedBoard(final String title, final int numberOfrows, final int numberOfColumns, final List<String> rowTitles, final List <String> columnTitles) throws IOException {
        client.sendCreateBoardMessage(title,numberOfrows, numberOfColumns, rowTitles, columnTitles);
        return client.receiveMessage();
    }
}
