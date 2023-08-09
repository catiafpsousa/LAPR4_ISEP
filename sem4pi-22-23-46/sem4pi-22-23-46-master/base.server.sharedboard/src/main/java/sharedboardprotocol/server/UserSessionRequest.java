package sharedboardprotocol.server;

import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class UserSessionRequest extends Thread{

    private final AuthorizationService authz = AuthzRegistry.authorizationService();

    private SBPMessage result;
    private SystemUser user;

    public UserSessionRequest(SystemUser user) {
        this.user = user;
    }

    public void run() {
        try {
            // Serialize the list of users
            SystemUser user = authz.session().get().authenticatedUser();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(user);
            objectOutputStream.close();

            // Get the serialized data as bytes
            byte[] serializedData = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();

            // Create the SBP message
            int dataLength = serializedData.length;
            short dLength1 = (short) (dataLength % SBPMessage.DATA_LENGHT_CONSTANT);
            short dLength2 = (short) (dataLength / SBPMessage.DATA_LENGHT_CONSTANT);
            result = new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.SEND_USERS, dLength1, dLength2, serializedData);
        } catch (IOException e) {
            String error = "It was not possible to list users";
            result= buildErrorMessage(error);
        }

    }

    private synchronized SBPMessage buildErrorMessage(String errorMessage) {
        byte [] responseData = errorMessage.getBytes();
        byte dLength1 = (byte) (responseData.length % SBPMessage.DATA_LENGHT_CONSTANT);
        byte dLength2 = (byte) (responseData.length / SBPMessage.DATA_LENGHT_CONSTANT);
        return new SBPMessage(SBPMessage.VERSION, SBPMessage.Code.ERR, dLength1, dLength2, responseData);
    }

    public SBPMessage sendUserSession(){
        return this.result;
    }
}
