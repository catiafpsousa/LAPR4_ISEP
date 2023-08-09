package sharedboardprotocol.server;

import application.ShareABoardController;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;





public class ListBoardsShareBoardRequest extends Thread {
    private SBPMessage result;
    private ShareABoardController controller;
    private SystemUser user;

    public ListBoardsShareBoardRequest(ShareABoardController controller, SystemUser user) {
        this.controller=controller;
        this.user = user;

    }

    public void run() {
        try {
            List<SharedBoard> sharedBoardList = (List) controller.listBoards(user);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(sharedBoardList);
            objectOutputStream.close();

            byte[] serializedData = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();

            int dataLength = serializedData.length;
            short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
            short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);
            result = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.SEND_BOARDS, dLength1, dLength2, serializedData);
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

    public SBPMessage sendBoardsListMessage(){
        return this.result;
    }

}
