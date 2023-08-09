package sharedboardprotocol.server;

import application.ServerUndoPostItController;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class CurrentUserRequest extends Thread {
    private SBPMessage result;
    private ServerUndoPostItController controller;
    private SystemUser user;

    public CurrentUserRequest(ServerUndoPostItController controller, SystemUser user) {
        this.controller=controller;
    }

    public void run() {
        try {
            SystemUser userSession =  this.user;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(user);
            objectOutputStream.close();

            byte[] serializedData = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();

            int dataLength = serializedData.length;
            short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
            short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);
            result = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.CURRENT_USER, dLength1, dLength2, serializedData);
        } catch (IOException e) {
            String error = "It was not possible to list boards";
            result=buildErrorMessage(error);
        }

    }

    private synchronized SBPMessage buildErrorMessage(String errorMessage) {
        byte [] responseData = errorMessage.getBytes();
        byte dLength1 = (byte) (responseData.length % SBPMessage.DATA_LENGHT_CONSTANT);
        byte dLength2 = (byte) (responseData.length / SBPMessage.DATA_LENGHT_CONSTANT);
        return new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.ERR, dLength1, dLength2, responseData);
    }

    public SBPMessage sendCurrentUserMessage(){
        return this.result;
    }

}
