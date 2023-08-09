package eapli.base.app.sharedboard.console.presentation;
import eapli.base.util.Consolle;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;

import java.io.IOException;

public class LoginTcpUI extends AbstractUI {
    private static final int MAX_ATTEMPTS = 3;
    private TcpClient tcpClient;

    public LoginTcpUI(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }
    @Override
    protected boolean doShow() {

        java.io.Console console = System.console();
        var attempt = 1;

        if (console != null) {
            while (attempt <= MAX_ATTEMPTS) {
                final String email = Consolle.readNonEmptyLine("Email:", "Please provide an email address");
                char[] passwordChars = console.readPassword("\nPassword:\n(for security reasons no characters will show as you type)\n");
                String password = new String(passwordChars);
                try {
                    tcpClient.sendAuthMessage(email, password);
                } catch (IOException e) {
                    System.out.println("Error sending AUTH message");
                }

                if (tcpClient.receiveACkMessage()) {
                    return true;
                }
                System.out.printf("Wrong email or password. You have %d attempts left.%n%n»»»»»»»»»%n",
                        MAX_ATTEMPTS - attempt);
                attempt++;
            }
            System.out.println("Sorry, we are unable to authenticate you. Please contact your system admnistrator.");
            return false;
        } else {
            while (attempt <= MAX_ATTEMPTS) {
                final String email = Console.readNonEmptyLine("Email:", "Please provide an email address");
                final String password = Console.readLine("Password:");
                try {
                    tcpClient.sendAuthMessage(email, password);
                } catch (IOException e) {
                    System.out.println("Error sending AUTH message");
                }

                if (tcpClient.receiveACkMessage()) {
                    return true;
                }
                System.out.printf("Wrong email or password. You have %d attempts left.%n%n»»»»»»»»»%n",
                        MAX_ATTEMPTS - attempt);
                attempt++;
            }
            System.out.println("Sorry, we are unable to authenticate you. Please contact your system admnistrator.");
            return false;
        }
    }

    @Override
    public String headline() {
        return "Login";
    }
}
