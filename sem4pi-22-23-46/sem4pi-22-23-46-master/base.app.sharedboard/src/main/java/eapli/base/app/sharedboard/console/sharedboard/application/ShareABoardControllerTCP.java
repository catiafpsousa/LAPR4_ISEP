package eapli.base.app.sharedboard.console.sharedboard.application;

import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.BoardPermission;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.application.UseCaseController;

import java.io.IOException;

@UseCaseController
public class ShareABoardControllerTCP {
    final TcpClient client;

    public ShareABoardControllerTCP(TcpClient client) {
        this.client = client;
    }

    public Iterable <SharedBoard> listBoards() throws IOException, ClassNotFoundException {
       return this.client.listBoardsThatOwns();
    }

    public Iterable <SystemUser> listUsers() throws IOException, ClassNotFoundException {
        return client.listSystemUsersToShareBoard();
    }

    public void shareABoard(final SharedBoard board, SystemUser user, BoardPermission permission){
        board.shareABoard(user, permission);
    }

    public SBPMessage saveSharedList(final SharedBoard board) throws IOException {
        return this.client.saveSharedList(board);
    }




}
