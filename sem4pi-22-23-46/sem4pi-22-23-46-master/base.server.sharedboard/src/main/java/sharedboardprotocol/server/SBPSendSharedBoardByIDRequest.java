package sharedboardprotocol.server;

import application.ArchiveBoardController;
import application.ViewBoardController;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.boardmanagement.domain.SharedBoard;

import java.io.*;
import java.nio.ByteBuffer;

public class SBPSendSharedBoardByIDRequest extends Thread{
    private SBPMessage message;
    private SBPMessage result;
    private ViewBoardController controller;

    public SBPSendSharedBoardByIDRequest(ViewBoardController controller, SBPMessage message) {
        this.controller=controller;
        this.message=message;
    }

    public void run() {
        try {
          byte [] data = message.getData();
          Long id = ByteBuffer.wrap(data).getLong();
          SharedBoard board = controller.getSharedBoardById(id);
            result = buildResponse(board);
        } catch (IOException e) {
            String error = e.getMessage();
            result = buildErrorMessage(error);
        }
    }

    private synchronized SBPMessage buildResponse(SharedBoard board) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(board);
        objectOutputStream.flush();
        byte[] data = byteArrayOutputStream.toByteArray();
        int dataLength = data.length;
        short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);
        return new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.BOARD_SELECTED, dLength1, dLength2, data);
    }

    private synchronized SBPMessage buildErrorMessage(String errorMessage) {
        byte [] responseData = errorMessage.getBytes();
        short dLength1 = (short) (responseData.length % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (responseData.length / SBPMessage.DATA_LENGHT_CONSTANT);
        return new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.ERR, dLength1, dLength2, responseData);
    }

    public SBPMessage sendSharedBoard(){
        return this.result;
    }


}
