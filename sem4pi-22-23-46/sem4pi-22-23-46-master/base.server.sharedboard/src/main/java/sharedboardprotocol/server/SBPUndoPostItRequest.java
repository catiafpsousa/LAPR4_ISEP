package sharedboardprotocol.server;

import application.ServerUndoPostItController;
import eapli.base.boardmanagement.domain.*;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SBPUndoPostItRequest extends Thread{
    private final SBPMessage message;
    private final ServerUndoPostItController controller = new ServerUndoPostItController();
    private SBPMessage result;

    public SBPUndoPostItRequest(SBPMessage message) {
        this.message = message;
    }

    public void run() {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            SharedBoard receivedBoard = (SharedBoard) objectInputStream.readObject();
            PostIt postIt = (PostIt) objectInputStream.readObject();
            controller.undoPostItLastChange(receivedBoard, postIt);
            objectInputStream.close();
            byteArrayInputStream.close();
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            String error = e.getMessage();
            result= buildErrorMessage(error);
        }
        result= buildResponse();
    }

    private synchronized SBPMessage buildResponse() {
        String responseMessage = "Undo Last Change in PostIt successfull";
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

    public synchronized SBPMessage undoPostItMessageResponse(){
        return this.result;
    }

}
