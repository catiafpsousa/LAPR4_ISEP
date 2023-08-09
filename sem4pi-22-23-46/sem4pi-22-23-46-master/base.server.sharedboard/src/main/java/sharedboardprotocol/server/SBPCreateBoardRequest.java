package sharedboardprotocol.server;

import application.CreateBoardController;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.infrastructure.authz.application.Authenticator;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

public class SBPCreateBoardRequest extends Thread{
    private final SBPMessage message;
    private final CreateBoardController controller = new CreateBoardController();
    private SBPMessage result;
    private SystemUser user;


    public SBPCreateBoardRequest(SBPMessage message, SystemUser user) {
        this.message = message;
        this.user = user;
    }

    public void run() {
        try {
            int version = message.getVersion();
            String title;
            int numberOfRows;
            int numberOfColumns;
            List<String> rowTitles;
            List<String> columnTitles;
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            title = (String) objectInputStream.readObject();
            numberOfRows = objectInputStream.readInt();
            numberOfColumns = objectInputStream.readInt();
            rowTitles = (List<String>) objectInputStream.readObject();
            columnTitles = (List<String>) objectInputStream.readObject();
            controller.createSharedBoard(title, numberOfRows, numberOfColumns, rowTitles, columnTitles, user);
            objectInputStream.close();
            byteArrayInputStream.close();
        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            String error = e.getMessage();
            result= buildErrorMessage(error);
        }
        result= buildResponse();
    }

    private synchronized SBPMessage buildResponse() {
        String responseMessage = "Save board successfully";
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

    public synchronized SBPMessage createBoardMessageResponse(){
        return this.result;
    }

}
