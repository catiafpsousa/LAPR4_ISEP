package eapli.base.app.sharedboard.console.sharedboard.application;

import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.app.sharedboard.console.sharedboard.http.HttpsServerAjaxBoard;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class ViewBoardControllerTCP {
    final TcpClient client;
    private final static int PORT = 80;

    public ViewBoardControllerTCP(TcpClient client) {
        this.client = client;
    }

    public Iterable<SharedBoard> listBoards() throws IOException, ClassNotFoundException {
        return this.client.listReadBoards();
    }

    public void selectSharedBoard(Long id) throws IOException, ClassNotFoundException {
        try {
            // Start the HTTPS server
            HttpsServerAjaxBoard server = new HttpsServerAjaxBoard(PORT, client, id);
            server.start();
            // Open the browser automatically
            String url = "http://localhost:80"; // Update with the appropriate URL
            openBrowser(url);

        } catch (Exception ex) {
            System.out.println("Failed to start the server: " + ex.getMessage());
        }
    }

    private void openBrowser(String url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                System.out.println("Failed to open browser: " + ex.getMessage());
            }
        } else {
            System.out.println("Opening browser is not supported on this platform.");
        }
    }
}
