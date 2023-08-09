package eapli.base.app.sharedboard.console.sharedboard.http;

import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.Cell;
import eapli.base.boardmanagement.domain.SharedBoard;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.Set;


/**
 * @author ANDRE MOREIRA (asc@isep.ipp.pt)
 */
public class HttpsServerAjaxBoard extends Thread {
    static private final String BASE_FOLDER = "base.app.sharedboard\\src\\main\\java\\eapli\\base\\app\\sharedboard\\console\\sharedboard\\http\\www";
    static private ServerSocket sock;
    private int port;
    private TcpClient client;

    private Long boardID;

    public HttpsServerAjaxBoard (int port, TcpClient client, Long boardID){
        this.port=port;
        this.client=client;
        this.boardID=boardID;
    }


    public  void run() {
        Socket cliSock;
        accessesCounter = 0;
        for (int i = 0; i < candidatesNumber; i++) {
            candidateName[i] = "Candidate " + i;
            candidateVotes[i] = 0;
        }

        try {
            sock = new ServerSocket(port);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Server failed to open local port " + port);
            System.exit(1);
        }

        while (true) {
            try {
                cliSock = sock.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            HttpAjaxBoardRequest req = new HttpAjaxBoardRequest(cliSock, BASE_FOLDER, client, boardID);
            req.start();
            incAccessesCounter();
        }
    }


    // DATA ACCESSED BY THREADS - LOCKING REQUIRED

    private static final int candidatesNumber = 4;
    private static final String[] candidateName = new String[candidatesNumber];
    private static final int[] candidateVotes = new int[candidatesNumber];
    private static int accessesCounter;

    private static synchronized void incAccessesCounter() {
        accessesCounter++;
    }

    public static synchronized String getBoardStandingInHTML(TcpClient client, Long id) throws IOException, ClassNotFoundException {
        SharedBoard board = client.selectedBoard(id);
        StringBuilder html = new StringBuilder();
        int rowCount = board.rowNumber().numberOfRows();
        int columnCount = board.columnNumber().numberOfColumns();
        String title=board.title().title();
        html.append("<h2>SHARED BOARD: ").append(title).append("</h2>");
        html.append("<table>");
        // Add column titles
        //html.append("<thead><tr><th></th>");
        html.append("<thead><tr>");
        if (board.hasRowTitles()) {
            html.append("<th></th>");
        }
        for (int j = 1; j <= columnCount; j++) {
            Cell cell = findCellByRowAndColumn(board.cells(), 1, j);
            String columnTitle = cell.column().title();
            if (columnTitle != null) {
                html.append("<th class=\"column-title\">").append(columnTitle).append("</th>");
            }
        }
        html.append("</tr></thead>");

        // Add row titles and cell content
        html.append("<tbody>");
        for (int i = 1; i <= rowCount; i++) {
            Cell rowCell = findCellByRowAndColumn(board.cells(), i, 1);
            String rowTitle = rowCell.row().title();
            if(rowTitle != null) {
                html.append("<tr><th class=\"row-title\">").append(rowTitle).append("</th>");
            }
            for (int j = 1; j <= columnCount; j++) {
                Cell cell = findCellByRowAndColumn(board.cells(), i, j);
                String cellContent = cell.latestActivePostIt() != null ? cell.latestActivePostIt().content().toString() : "";
                if(cell.latestActivePostIt() != null) {
                    html.append("<td class=\"board-cell\">")
                            .append("<div class=\"post-it\">")
                            .append("<div class=\"post-it-content\">");
                    if (cell.latestPostIt().content().hasImageContent()) {
                        byte[] imageContent = cell.latestPostIt().content().imageContent();
                        String base64Image = Base64.getEncoder().encodeToString(imageContent);
                        html.append("<img class=\"post-it-image\" src=\"data:image/png;base64,").append(base64Image).append("\"/>");;
                    }
                    if (cell.latestPostIt().content().hasTextContent()) {
                        html.append("<div class=\"post-it-text\">") // New line
                                .append(cell.latestPostIt().content().textContent())
                                .append("</div>");
                    }
                    html.append("</div>") // closing post-it-content div
                            .append("</div>") // closing post-it div
                            .append("</td>");
                }else{
                    html.append("<td class=\"board-cell\">")
                            .append(cellContent)
                            .append("</td>");
                }
            }
            html.append("</tr>");
        }
        html.append("</tbody>");

        html.append("</table>");

        return html.toString();
    }

    private static Cell findCellByRowAndColumn(Set<Cell> cells, int row, int column) {
        for (Cell cell : cells) {
            if (cell.row().number() == row && cell.column().number() == column) {
                return cell;
            }
        }
        return null;
    }

}
