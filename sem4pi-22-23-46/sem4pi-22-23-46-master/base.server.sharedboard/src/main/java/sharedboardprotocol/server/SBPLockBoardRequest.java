package sharedboardprotocol.server;

import application.ServerCreatePostItController;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SBPLockBoardRequest extends Thread{
    private final SBPMessage message;
    private final ServerCreatePostItController controller = new ServerCreatePostItController();
    private SBPMessage result;
    private SystemUser user;

    public SBPLockBoardRequest(SBPMessage message, SystemUser user) {
        this.message = message;
        this.user = user;

    }

    public void run() {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            SharedBoard receivedBoard = (SharedBoard) objectInputStream.readObject();
            controller.lockBoard(receivedBoard);
            objectInputStream.close();
            byteArrayInputStream.close();
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            String error = e.getMessage();
            result= buildErrorMessage(error);
        }
        result= buildResponse();
    }

    private synchronized SBPMessage buildResponse() {
        String responseMessage = "Board locked";
        byte[] responseData = responseMessage.getBytes();
        byte dLength1 = (byte) (responseData.length % SBPMessage.DATA_LENGHT_CONSTANT);
        byte dLength2 = (byte) (responseData.length / SBPMessage.DATA_LENGHT_CONSTANT);
        return new SBPMessage((byte) 1, SBPMessage.Code.SUCCESS_SAVE_BOARD, dLength1, dLength2, responseData);
    }

    private synchronized SBPMessage buildErrorMessage(String errorMessage) {
        byte [] responseData = errorMessage.getBytes();
        byte dLength1 = (byte) (responseData.length % SBPMessage.DATA_LENGHT_CONSTANT);
        byte dLength2 = (byte) (responseData.length / SBPMessage.DATA_LENGHT_CONSTANT);
        return new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.ERR, dLength1, dLength2, responseData);
    }

    public synchronized SBPMessage lockBoardMessageResponse(){
        return this.result;
    }

}
