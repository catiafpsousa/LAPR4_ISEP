package sharedboardprotocol.server;

import application.ServerCreatePostItController;
import application.ServerViewBoardHistoryController;
import eapli.base.boardmanagement.domain.Cell;
import eapli.base.boardmanagement.domain.Content;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.application.Authenticator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SBPViewBoardHistoryRequest extends Thread {
    private final SBPMessage message;
    private final ServerViewBoardHistoryController controller = new ServerViewBoardHistoryController();
    private final Authenticator authenticationService;
    private SBPMessage result;

    public SBPViewBoardHistoryRequest(SBPMessage message, Authenticator authenticationService) {
        this.message = message;
        this.authenticationService = authenticationService;

    }

    public void run() {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            SharedBoard receivedBoard = (SharedBoard) objectInputStream.readObject();

            controller.boardHistory(receivedBoard);
            objectInputStream.close();
            byteArrayInputStream.close();
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            String error = e.getMessage();
            result = buildErrorMessage(error);
        }
        result = buildResponse();
    }

    private synchronized SBPMessage buildResponse() {
        String responseMessage = "Viewing Board History";
        byte[] responseData = responseMessage.getBytes();
        byte dLength1 = (byte) (responseData.length % SBPMessage.DATA_LENGHT_CONSTANT);
        byte dLength2 = (byte) (responseData.length / SBPMessage.DATA_LENGHT_CONSTANT);
        return new SBPMessage((byte) 1, SBPMessage.Code.SUCCESS_SAVE_BOARD, dLength1, dLength2, responseData);
    }

    private synchronized SBPMessage buildErrorMessage(String errorMessage) {
        byte[] responseData = errorMessage.getBytes();
        byte dLength1 = (byte) (responseData.length % SBPMessage.DATA_LENGHT_CONSTANT);
        byte dLength2 = (byte) (responseData.length / SBPMessage.DATA_LENGHT_CONSTANT);
        return new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.ERR, dLength1, dLength2, responseData);
    }

    public synchronized SBPMessage viewBoardHistoryMessageResponse() {
        return this.result;
    }

}
