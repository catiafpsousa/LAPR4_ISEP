package sharedboardprotocol.server;

import application.ServerViewBoardHistoryController;
import application.ServerViewBoardVersionController;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.application.Authenticator;

import java.io.*;
import java.util.Calendar;

public class SBPViewBoardVersionRequest extends Thread {
    private final SBPMessage message;
    private final ServerViewBoardVersionController controller = new ServerViewBoardVersionController();
    private final Authenticator authenticationService;
    private SBPMessage result;


    public SBPViewBoardVersionRequest(SBPMessage message, Authenticator authenticationService) {
        this.message = message;
        this.authenticationService = authenticationService;

    }

    public void run() {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            SharedBoard receivedBoard = (SharedBoard) objectInputStream.readObject();
            Calendar receivedTimestamp = (Calendar)objectInputStream.readObject();

            SharedBoard boardVersion = controller.boardVersion(receivedBoard, receivedTimestamp);

            objectInputStream.close();
            byteArrayInputStream.close();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(boardVersion);

            objectOutputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            String error = e.getMessage();
            result = buildErrorMessage(error);
        }
        result = buildResponse();
    }

    private synchronized SBPMessage buildResponse() {
        String responseMessage = "Viewing Board Version";
        byte[] responseData = responseMessage.getBytes();
        short dLength1 = (short) (responseData.length % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (responseData.length / SBPMessage.DATA_LENGHT_CONSTANT);
        return new SBPMessage((byte) 1, SBPMessage.Code.SUCCESS_SAVE_BOARD, dLength1, dLength2, responseData);
    }

    private synchronized SBPMessage buildErrorMessage(String errorMessage) {
        byte[] responseData = errorMessage.getBytes();
        byte dLength1 = (byte) (responseData.length % SBPMessage.DATA_LENGHT_CONSTANT);
        byte dLength2 = (byte) (responseData.length / SBPMessage.DATA_LENGHT_CONSTANT);
        return new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.ERR, dLength1, dLength2, responseData);
    }

    public synchronized SBPMessage viewBoardVersionMessageResponse() {
        return this.result;
    }

}
