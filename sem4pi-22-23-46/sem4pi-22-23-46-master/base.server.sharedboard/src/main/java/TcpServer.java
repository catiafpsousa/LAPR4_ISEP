import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import sharedboardprotocol.server.SBPMessageParser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


class TcpSrv {
    private  static final Logger LOGGER = LogManager.getLogger(TcpSrv.class);
    static ServerSocket sock;
    private final SBPMessageParser parser;
    private static final int PORT = 9999;

    TcpSrv(SBPMessageParser parser) {
        this.parser = parser;
    }

    public void start() throws Exception {
        Socket cliSock;

        try {
            sock = new ServerSocket(PORT);
        } catch (IOException ex) {
            System.out.println("Failed to open server socket");
            System.exit(1);
        }

        while (true) {
            cliSock = sock.accept();
            new Thread(new TcpSrvThread(cliSock, parser)).start();
        }
    }
}

class TcpSrvThread implements Runnable {
    private Socket s;;
    private final SBPMessageParser parser;
    private DataOutputStream sOut;
    private DataInputStream sIn;

    public TcpSrvThread(Socket cli_s, SBPMessageParser parser) {
        s=cli_s;
        this.parser = parser;
    }

    public void run() {
        InetAddress clientIP;
        clientIP = s.getInetAddress();
        System.out.println("New client connection from " + clientIP.getHostAddress() +
                ", port number " + s.getPort());
        try {
            sOut = new DataOutputStream(s.getOutputStream());
            sIn = new DataInputStream(s.getInputStream());
            while (true) {
                SBPMessage request = SBPMessage.readFromStream(sIn);
                System.out.println("=======================RECEIVING REQUEST========================\n" + request+"\n");
                processRequest(request);
            }
        }catch (IOException | ClassNotFoundException io) {
            System.out.println("Error handling client request\n" + s.getInetAddress().getHostAddress());
        } finally {
        try {
            if(!s.isClosed()) {
                s.close();
            }
            System.out.println("Closed client connection: \n" + s.getInetAddress().getHostAddress());
        } catch (IOException e) {
            System.err.println("Error closing client connection: \n" + e.getMessage());
        }
    }
}

    private synchronized void processRequest(SBPMessage request) throws IOException, ClassNotFoundException {
        SBPMessage response = parser.parse(request,s);
        try {
            if (!s.isClosed() && s.isConnected()) {
                SBPMessage.sendSBPMessage(response, sOut);
                System.out.println("==========================SENT RESPONSE==========================\n" + response+"\n");
            } else {
                System.out.println("Socket is closed or not connected. Cannot send response.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   }