package eapli.base.boardmanagement.domain;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Represents a SBP (Some Binary Protocol) message.
 * This message includes version, code and data fields, among others.
 */
public class SBPMessage {

    public static final int VERSION= 1;
    public static final int DATA_LENGHT_CONSTANT = 256;

    private int version;
    private Code code;
    private int dataLength1;
    private int dataLength2;
    private byte[] data;



    /**
     * Constructs an SBPMessage with the provided parameters after validating them.
     *
     * @param version     The version of the SBP message
     * @param code        The code for the SBP message
     * @param dataLength1 The first data length byte
     * @param dataLength2 The second data length byte
     * @param data        The actual data of the SBP message
     */
    public SBPMessage(int version, Code code, int dataLength1, int dataLength2, byte[] data) {
        validateVersion(version);
        this.code=code;
        validateDataLength1(dataLength1);
        validateDataLength2(dataLength2);
        this.data = data;
    }

    /**
     * Reads an SBPMessage from the input stream.
     *
     * @param inputStream The input stream to read the SBPMessage from
     * @return The read SBPMessage
     * @throws IOException If an I/O error occurs reading from the stream
     */
    public static SBPMessage readFromStream(DataInputStream inputStream) throws IOException {
        int version = inputStream.readByte();
        Code code = Code.valueOfAscii(inputStream.readByte());
        int dLength1 = inputStream.readUnsignedByte();
        int dLength2 = inputStream.readUnsignedByte();
        int dataLength = dLength1 + DATA_LENGHT_CONSTANT * dLength2;
        byte[] data = new byte[dataLength];
        inputStream.readFully(data);
        return new SBPMessage(version,code,dLength1, dLength2, data);
    }

    /**
     * Sends an SBPMessage to the provided output stream.
     *
     * @param message      The SBPMessage to be sent
     * @param outputStream The output stream to write the SBPMessage to
     * @throws IOException If an I/O error occurs writing to the stream
     */
    public static void sendSBPMessage(SBPMessage message, OutputStream outputStream) throws IOException {
        // Write the SBP message fields to the output stream
        outputStream.write(message.getVersion());
        outputStream.write(message.getCode().value);
        outputStream.write(message.getDataLength1());
        outputStream.write(message.getDataLength2());

        // Write the data field if it exists
        byte[] data = message.getData();
        if (data != null && data.length > 0) {
            outputStream.write(data);
        }

        // Flush the output stream
        outputStream.flush();
    }


    /**
     * Returns the version of the SBPMessage.
     *
     * @return The version of the SBPMessage
     */
    public int getVersion() {
        return version;
    }

    /**
     * Returns the first data length byte of the SBPMessage.
     *
     * @return The first data length byte of the SBPMessage
     */
    public int getDataLength1() {
        return dataLength1;
    }

    /**
     * Returns the second data length byte of the SBPMessage.
     *
     * @return The second data length byte of the SBPMessage
     */
    public int getDataLength2() {
        return dataLength2;
    }

    /**
     * Validates the version of the SBPMessage.
     *
     * @param version The version to be validated
     * @throws IllegalArgumentException If the version is not equal to the expected VERSION
     */
    public void validateVersion(int version) {
        if (version != VERSION) {
            throw new IllegalArgumentException("Invalid version number");
        }
        this.version = version;
    }

    /**
     * Returns the code of the SBPMessage.
     *
     * @return The code of the SBPMessage
     */
    public Code getCode() {
        return code;
    }

    /**
     * Validates the first data length byte.
     *
     * @param dataLength1 The first data length byte to be validated
     * @throws IllegalArgumentException If dataLength1 is not in the valid range (0-255)
     */
    public void validateDataLength1(int dataLength1) {
        if (dataLength1 < 0 || dataLength1 > 255) {
            throw new IllegalArgumentException("Invalid data lenght 1");
        }
        this.dataLength1=dataLength1;
    }

    /**
     * Validates the second data length byte.
     *
     * @param dataLength2 The second data length byte to be validated
     * @throws IllegalArgumentException If dataLength2 is not in the valid range (0-255)
     */
    public void validateDataLength2(int dataLength2) {
        if (dataLength2< 0 || dataLength2 > 255) {
            throw new IllegalArgumentException("Invalid data lenght 2");
        }
        this.dataLength2=dataLength2;
    }


    /**
     * Returns the data of the SBPMessage.
     *
     * @return The data of the SBPMessage
     */
    public byte[] getData() {
        return data;
    }


    /**
     * Enum class representing the different codes that an SBPMessage can have.
     */
    public enum Code {
        COMMTEST(0),
        DISCONN(1),
        ACK(2),
        ERR(3),
        AUTH(4),
        CREATE_BOARD(5),
        SUCCESS_SAVE_BOARD(6),
        LIST_BOARDS(7),
        SEND_BOARDS(8),
        LIST_USERS(9),
        SEND_USERS(10),
        SAVE_SHARED_LIST(11),
        LIST_WRITE_BOARDS(12),
        CREATE_POSTIT(13),
        LIST_OWNED_POSTITS(14),
        UPDATE_POSTIT(15),
        MOVE_POSTIT(16),
        DELETE_POSTIT(17),
        UNDO_POSTIT(18),
        VIEW_BOARD_HISTORY(19),
        LIST_READ_BOARDS(20),
        ARCHIVE_BOARD(21),
        SUCCESS_ARCHIVE_BOARD(22),
        LIST_BOARDS_TO_ARCHIVE(23),

        CURRENT_USER(24), SUCCESS_RESTORE_BOARD(25), RESTORE_BOARD(26), LIST_BOARDS_TO_RESTORE(27),
        VIEW_BOARD_VERSION(28),
        BOARD_BY_ID(29), BOARD_SELECTED(30),
        GET_SESSION(31),
        LOCK_BOARD(32),
        UNLOCK_BOARD(33);




        private final int value;

        Code(int value) {
            this.value = value;
        }

        public static Code valueOf(int value) {
            for (Code code : Code.values()) {
                if (code.value == value) {
                    return code;
                }
            }
            throw new IllegalArgumentException("Invalid message code: " + value);
        }

        public static Code valueOfAscii(int asciiCode) {
            for (Code code : Code.values()) {
                if (code.value == asciiCode) {
                    return code;
                }
            }
            throw new IllegalArgumentException("Invalid message code: " + asciiCode);
        }
    }


    /**
     * Returns a string representation of the SBPMessage.
     *
     * @return A string representation of the SBPMessage
     */
    @Override
    public String toString() {
        try {
            StringBuilder sb = new StringBuilder("SBPMessage[");
            sb.append("version=").append(version)
                    .append(", code=").append(code)
                    .append(", dataLength1=").append(dataLength1)
                    .append(", dataLength2=").append(dataLength2)
                    .append(", data=");

            if (data != null) {
                sb.append(new String(data, "ASCII"));
            } else {
                sb.append("null");
            }

            sb.append(']');
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the data of the SBPMessage as a string.
     *
     * @return The data of the SBPMessage as a string, or null if the data is null
     */
    public String getDataAsString() {
        if (this.data != null) {
            try {
                return new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}