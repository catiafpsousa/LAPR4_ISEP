package sharedboardprotocol.server;

import application.ServerUndoPostItController;
import application.ServerUpdatePostItController;
import eapli.base.boardmanagement.domain.*;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SBPUpdatePostItRequest extends Thread{
    private final SBPMessage message;
    private  ServerUpdatePostItController controller;
    private SBPMessage result;
    private SystemUser user;

    public SBPUpdatePostItRequest(SBPMessage message, ServerUpdatePostItController controller, SystemUser user) {
        this.message = message;
        this.controller = controller;
        this.user = user;

    }

    public void run() {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            SharedBoard receivedBoard = (SharedBoard) objectInputStream.readObject();
            Cell receivedCell = (Cell) objectInputStream.readObject();
            PostIt postIt = (PostIt) objectInputStream.readObject();

            switch (message.getCode()) {
                case UPDATE_POSTIT:
                    Content receivedContent = (Content) objectInputStream.readObject();
                    controller.updatePostItContent(receivedBoard, receivedCell, postIt, receivedContent, user);
                    break;
                case MOVE_POSTIT:
                    Cell newCell = (Cell) objectInputStream.readObject();
                    controller.movePostIt(receivedBoard, receivedCell, postIt, newCell);
                    break;
                case DELETE_POSTIT:
                    controller.deletePostIt(receivedBoard, receivedCell, postIt);
                    break;
                default:
                    //Error
                    break;
            }
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

    public synchronized SBPMessage updatePostItMessageResponse(){
        return this.result;
    }

}
