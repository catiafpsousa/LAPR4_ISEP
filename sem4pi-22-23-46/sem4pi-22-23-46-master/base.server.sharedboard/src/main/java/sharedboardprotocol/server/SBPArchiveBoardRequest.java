package sharedboardprotocol.server;

import application.ArchiveBoardController;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;


import java.io.*;


public class SBPArchiveBoardRequest extends Thread{

    private SBPMessage message;
    private SBPMessage result;
    private ArchiveBoardController controller;


    public SBPArchiveBoardRequest(SBPMessage message, ArchiveBoardController controller){
        this.message = message;
        this.controller = controller;
    }

    public void run() {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message.getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            SharedBoard board = (SharedBoard) objectInputStream.readObject();
            objectInputStream.close();
            byteArrayInputStream.close();
            controller.archiveBoard(board);
            result = buildResponse();
        } catch (IOException | ClassNotFoundException e) {
            String error = e.getMessage();
            result = buildErrorMessage(error);
        }
    }

    private synchronized SBPMessage buildResponse() {
        String responseMessage = "Archive Board Successfully";
        byte[] responseData = responseMessage.getBytes();
        short dLength1 = (short) (responseData.length % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (responseData.length / SBPMessage.DATA_LENGHT_CONSTANT);
        return new SBPMessage((byte) 1, SBPMessage.Code.SUCCESS_ARCHIVE_BOARD, dLength1, dLength2, responseData);
    }

    private synchronized SBPMessage buildErrorMessage(String errorMessage) {
        byte [] responseData = errorMessage.getBytes();
        byte dLength1 = (byte) (responseData.length % SBPMessage.DATA_LENGHT_CONSTANT);
        byte dLength2 = (byte) (responseData.length / SBPMessage.DATA_LENGHT_CONSTANT);
        return new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.ERR, dLength1, dLength2, responseData);
    }

    public synchronized SBPMessage archiveBoardMessageResponse(){
        return this.result;
    }


}
