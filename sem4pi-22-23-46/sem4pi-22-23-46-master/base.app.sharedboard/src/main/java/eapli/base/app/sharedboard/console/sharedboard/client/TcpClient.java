package eapli.base.app.sharedboard.console.sharedboard.client;

import eapli.base.app.sharedboard.console.sharedboard.application.UserSessions;
import eapli.base.boardmanagement.domain.*;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;

public class TcpClient {
    static InetAddress serverIP;
    static Socket sock;
    public static final int PORT = 9999;
    public static final String SERVER_IP = "127.0.0.1";
    public static BufferedReader in;
    public static DataOutputStream sOut;
    public static DataInputStream sIn;
    public SystemUser userSession;


    public void runTcpClient() throws Exception {
        try {
            serverIP = InetAddress.getByName(SERVER_IP);
        } catch (UnknownHostException ex) {
            System.out.println("Invalid server specified");
            System.exit(1);
        }

        try {
            sock = new Socket(serverIP, PORT);
        } catch (IOException ex) {
            System.out.println("Failed to establish TCP connection");
            System.exit(1);
        }

        in = new BufferedReader(new InputStreamReader(System.in));
        sOut = new DataOutputStream(sock.getOutputStream());
        sIn = new DataInputStream(sock.getInputStream());
        sendComTestMessage();
    }

    public void sendComTestMessage() throws IOException {
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.COMMTEST, 0, 0, null);
        SBPMessage.sendSBPMessage(message, sOut);
    }

    public void sendAuthMessage(String username, String password) throws IOException {
        byte[] usernameAscii = (username + "\0").getBytes();
        byte[] passwordAscii = (password + "\0").getBytes();
        byte[] result = new byte[usernameAscii.length + passwordAscii.length];

        System.arraycopy(usernameAscii, 0, result, 0, usernameAscii.length);
        System.arraycopy(passwordAscii, 0, result, usernameAscii.length, passwordAscii.length);

        byte dLength1 = 0;
        byte dLength2 = 0;

        dLength1 = (byte) (result.length % SBPMessage.DATA_LENGHT_CONSTANT);
        dLength2 = (byte) (result.length / SBPMessage.DATA_LENGHT_CONSTANT);
        SBPMessage response = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.AUTH, dLength1, dLength2, result);
        SBPMessage.sendSBPMessage(response, sOut);
    }

    public boolean receiveACkMessage() {
        try {
            DataInputStream sIn = new DataInputStream(sock.getInputStream());
            SBPMessage message = SBPMessage.readFromStream(sIn);
            return message.getCode().equals(SBPMessage.Code.ACK);
        } catch (IOException ex) {
            System.out.println("Error receiving message");
            return false;
        }
    }

    public void sendDisconnectMessage() throws IOException {
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.DISCONN, 0, 0, null);
        SBPMessage.sendSBPMessage(message, sOut);
    }

    public SBPMessage receiveMessage() throws IOException {
        return SBPMessage.readFromStream(sIn);
    }

    public void sendMessage(SBPMessage message) throws IOException {
        SBPMessage.sendSBPMessage(message, sOut);
    }

    public void sendCreateBoardMessage(String title, int numberOfrows, int numberOfColumns, List<String> rowTitles, List<String> columnTitles) throws IOException {
        // Serialize the data into a byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(title);
        objectOutputStream.writeInt(numberOfrows);
        objectOutputStream.writeInt(numberOfColumns);
        objectOutputStream.writeObject(rowTitles);
        objectOutputStream.writeObject(columnTitles);
        objectOutputStream.close();
        byte[] data = byteArrayOutputStream.toByteArray();
        int dataLength = data.length;
        short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);

        // Create the SBPMessage instance
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.CREATE_BOARD, dLength1, dLength2, data);

        // Send the SBPMessage
        SBPMessage.sendSBPMessage(message, sOut);
    }

    public void sendListBoardsMessage() throws IOException {
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.LIST_BOARDS, 0, 0, null);
        SBPMessage.sendSBPMessage(message, sOut);
    }

    public void sendListUsersMessage() throws IOException {
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.LIST_USERS, 0, 0, null);
        SBPMessage.sendSBPMessage(message, sOut);
    }

    public void sendGetUserSessionMessage() throws IOException {
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.GET_SESSION, 0, 0, null);
        SBPMessage.sendSBPMessage(message, sOut);
    }


    public Iterable<SharedBoard> listBoardsThatOwns() throws IOException, ClassNotFoundException {
        sendListBoardsMessage();
        SBPMessage response = SBPMessage.readFromStream(sIn);
        byte[] serializedData = response.getData();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        List<SharedBoard> sharedBoardList = (List<SharedBoard>) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return sharedBoardList;
    }

    public Iterable<SystemUser> listSystemUsersToShareBoard() throws IOException, ClassNotFoundException {
        sendListUsersMessage();
        SBPMessage response = SBPMessage.readFromStream(sIn);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(response.getData());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        // Read the serialized object (list of shared boards)
        Iterable<SystemUser> userList = (Iterable<SystemUser>) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return userList;
    }

    public SystemUser sessionUser() throws IOException, ClassNotFoundException {
        sendGetUserSessionMessage();
        SBPMessage response = SBPMessage.readFromStream(sIn);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(response.getData());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        // Read the serialized object (list of shared boards)
        SystemUser user = (SystemUser) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        this.userSession =user;
        return user;
    }

    public SBPMessage saveSharedList(SharedBoard board) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(board);
        objectOutputStream.close();
        byte[] boardData = outputStream.toByteArray();
        outputStream.close();
        int dataLength = boardData.length;
        short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);

        SBPMessage sbpMessage = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.SAVE_SHARED_LIST, dLength1, dLength2, boardData);
        SBPMessage.sendSBPMessage(sbpMessage, sOut);
        return SBPMessage.readFromStream(sIn);
    }


    public void sendListWriteBoardsMessage() throws IOException {
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.LIST_WRITE_BOARDS, 0, 0, null);
        SBPMessage.sendSBPMessage(message, sOut);
    }

    public void sendListReadBoardsMessage() throws IOException {
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.LIST_READ_BOARDS, 0, 0, null);
        SBPMessage.sendSBPMessage(message, sOut);
    }

    public void sendListUserCellsMessage() throws IOException {
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.LIST_OWNED_POSTITS, 0, 0, null);
        SBPMessage.sendSBPMessage(message, sOut);
    }

    public SBPMessage sendCreatePostItMessage(SharedBoard board, Cell cell, Content content) throws IOException, ClassNotFoundException {
        // Serialize the data into a byte array
        // Serialize the objects
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(board);
        objectStream.writeObject(cell);
        objectStream.writeObject(content);
// Get the serialized data
        byte[] data = byteStream.toByteArray();
        objectStream.close();
        int dataLength = data.length;
        short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);

// Create the SBP message
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.CREATE_POSTIT, dLength1, dLength2, data);

        // Send the SBPMessage
        SBPMessage.sendSBPMessage(message, sOut);
        return SBPMessage.readFromStream(sIn);
    }

    public SBPMessage sendUndoPostItMessage(SharedBoard board, PostIt postIt) throws IOException, ClassNotFoundException {
        // Serialize the data into a byte array
        // Serialize the objects
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(board);
        objectStream.writeObject(postIt);
// Get the serialized data
        byte[] data = byteStream.toByteArray();
        objectStream.close();
        int dataLength = data.length;
        short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);

// Create the SBP message
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.UNDO_POSTIT, dLength1, dLength2, data);

        // Send the SBPMessage
        SBPMessage.sendSBPMessage(message, sOut);
        return SBPMessage.readFromStream(sIn);
    }

    public Iterable<SharedBoard> listWriteBoards() throws IOException, ClassNotFoundException {
        sendListWriteBoardsMessage();
        SBPMessage response = SBPMessage.readFromStream(sIn);
        byte[] serializedData = response.getData();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        List<SharedBoard> sharedBoardList = (List<SharedBoard>) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return sharedBoardList;
    }

    public Iterable<SharedBoard> listReadBoards() throws IOException, ClassNotFoundException {
        sendListReadBoardsMessage();
        SBPMessage response = SBPMessage.readFromStream(sIn);
        byte[] serializedData = response.getData();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        List<SharedBoard> sharedBoardList = (List<SharedBoard>) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return sharedBoardList;
    }

    public SharedBoard viewBoardVersionMessage() throws IOException, ClassNotFoundException {
        sendViewBoardVersionMessage();
        SBPMessage response = SBPMessage.readFromStream(sIn);
        byte[] serializedData = response.getData();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        SharedBoard sharedBoard = (SharedBoard) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return sharedBoard;
    }

    private void sendViewBoardVersionMessage() throws IOException {
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.VIEW_BOARD_VERSION, 0, 0, null);
        SBPMessage.sendSBPMessage(message, sOut);
    }

    public void sendCurrentUserMessage() throws IOException{
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.CURRENT_USER, 0, 0, null);
        SBPMessage.sendSBPMessage(message, sOut);
    }

    public SystemUser currentUser() throws IOException, ClassNotFoundException {
        sendCurrentUserMessage();
        SBPMessage response = SBPMessage.readFromStream(sIn);
        byte[] serializedData = response.getData();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        SystemUser user = (SystemUser) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return user;
    }

    public SBPMessage sendViewBoardHistoryMessage(SharedBoard board) throws IOException, ClassNotFoundException {
        // Serialize the data into a byte array
        // Serialize the objects
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(board);
// Get the serialized data
        byte[] data = byteStream.toByteArray();
        objectStream.close();
        int dataLength = data.length;
        short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);

// Create the SBP message
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.VIEW_BOARD_HISTORY, dLength1, dLength2, data);

        // Send the SBPMessage
        SBPMessage.sendSBPMessage(message, sOut);
        return SBPMessage.readFromStream(sIn);
    }





    public SBPMessage archiveBoard(SharedBoard board) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(board);
        objectOutputStream.close();
        byte[] boardData = outputStream.toByteArray();
        outputStream.close();
        int dataLength = boardData.length;
        short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);

        SBPMessage sbpMessage = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.ARCHIVE_BOARD, dLength1, dLength2, boardData);
        SBPMessage.sendSBPMessage(sbpMessage, sOut);
        return SBPMessage.readFromStream(sIn);
    }


    public SBPMessage restoreBoard(SharedBoard board) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(board);
        objectOutputStream.close();
        byte[] boardData = outputStream.toByteArray();
        outputStream.close();
        int dataLength = boardData.length;
        short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);

        SBPMessage sbpMessage = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.RESTORE_BOARD, dLength1, dLength2, boardData);
        SBPMessage.sendSBPMessage(sbpMessage, sOut);
        return SBPMessage.readFromStream(sIn);

    }

    public Iterable<SharedBoard> listBoardsArchived() throws IOException, ClassNotFoundException {
        sendListBoardsArchivedMessage();
        SBPMessage response = SBPMessage.readFromStream(sIn);
        byte[] serializedData = response.getData();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedData);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        List<SharedBoard> sharedBoardList = (List<SharedBoard>) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return sharedBoardList;
    }

    public void sendListBoardsArchivedMessage() throws IOException {
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.LIST_BOARDS_TO_RESTORE, 0, 0, null);
        SBPMessage.sendSBPMessage(message, sOut);
    }

    public SBPMessage sendUpdatePostItMessage(SharedBoard board, Cell cell, PostIt postIt, Content newContent) throws IOException {
        // Serialize the data into a byte array
        // Serialize the objects
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(board);
        objectStream.writeObject(cell);
        objectStream.writeObject(postIt);
        objectStream.writeObject(newContent);

// Get the serialized data
        byte[] data = byteStream.toByteArray();
        objectStream.close();
        int dataLength = data.length;
        short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);

// Create the SBP message
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.UPDATE_POSTIT, dLength1, dLength2, data);

        // Send the SBPMessage
        SBPMessage.sendSBPMessage(message, sOut);
        return SBPMessage.readFromStream(sIn);
    }

    public SBPMessage sendMovePostItMessage(SharedBoard board, Cell oldCell, PostIt postIt, Cell newCell) throws IOException {
        // Serialize the data into a byte array
        // Serialize the objects
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(board);
        objectStream.writeObject(oldCell);
        objectStream.writeObject(postIt);
        objectStream.writeObject(newCell);

// Get the serialized data
        byte[] data = byteStream.toByteArray();
        objectStream.close();
        int dataLength = data.length;
        short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);

// Create the SBP message
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.MOVE_POSTIT, dLength1, dLength2, data);

        // Send the SBPMessage
        SBPMessage.sendSBPMessage(message, sOut);
        return SBPMessage.readFromStream(sIn);
    }

    public SBPMessage sendDeletePostItMessage(SharedBoard board, Cell cell, PostIt postIt) throws IOException {
        // Serialize the data into a byte array
        // Serialize the objects
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(board);
        objectStream.writeObject(cell);
        objectStream.writeObject(postIt);

// Get the serialized data
        byte[] data = byteStream.toByteArray();
        objectStream.close();
        int dataLength = data.length;
        short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);

// Create the SBP message
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.DELETE_POSTIT, dLength1, dLength2, data);

        // Send the SBPMessage
        SBPMessage.sendSBPMessage(message, sOut);
        return SBPMessage.readFromStream(sIn);
    }

    public void sendBoardByIdMessage(Long id) throws IOException {
        byte[] idData = ByteBuffer.allocate(8).putLong(id).array();
        byte dLength1 = (byte) (idData.length % SBPMessage.DATA_LENGHT_CONSTANT);
        byte dLength2 = (byte) (idData.length / SBPMessage.DATA_LENGHT_CONSTANT);
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.BOARD_BY_ID, dLength1, dLength2, idData);
        SBPMessage.sendSBPMessage(message, sOut);
    }

    public SharedBoard selectedBoard(Long id) throws IOException, ClassNotFoundException {
        sendBoardByIdMessage(id);
        SBPMessage response = SBPMessage.readFromStream(sIn);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(response.getData());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (SharedBoard) objectInputStream.readObject();
    }

    public SBPMessage sendLockBoardMessage(SharedBoard board) throws IOException {
        // Serialize the data into a byte array
        // Serialize the objects
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(board);
// Get the serialized data
        byte[] data = byteStream.toByteArray();
        objectStream.close();
        int dataLength = data.length;
        short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);

// Create the SBP message
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.LOCK_BOARD, dLength1, dLength2, data);

        // Send the SBPMessage
        SBPMessage.sendSBPMessage(message, sOut);
        return SBPMessage.readFromStream(sIn);
    }


    public SBPMessage sendUnLockBoardMessage(SharedBoard board) throws IOException {
        // Serialize the data into a byte array
        // Serialize the objects
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(board);
// Get the serialized data
        byte[] data = byteStream.toByteArray();
        objectStream.close();
        int dataLength = data.length;
        short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
        short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);

// Create the SBP message
        SBPMessage message = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.UNLOCK_BOARD, dLength1, dLength2, data);

        // Send the SBPMessage
        SBPMessage.sendSBPMessage(message, sOut);
        return SBPMessage.readFromStream(sIn);
    }
}



