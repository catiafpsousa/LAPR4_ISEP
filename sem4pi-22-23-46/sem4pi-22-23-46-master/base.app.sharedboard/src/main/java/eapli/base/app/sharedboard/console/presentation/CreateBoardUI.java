package eapli.base.app.sharedboard.console.presentation;


import eapli.base.app.sharedboard.console.sharedboard.application.CreateBoardControllerTCP;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.boardmanagement.domain.SBPMessage;
import eapli.base.util.UiUtils;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;

import java.util.ArrayList;
import java.util.List;

public class CreateBoardUI extends AbstractUI{
   private TcpClient client;
   private CreateBoardControllerTCP ctrl;

    public CreateBoardUI(TcpClient client){
        this.client= client;
        this.ctrl = new CreateBoardControllerTCP(client);
    }

    @Override
    protected boolean doShow() {
        try{
        final String title = Console.readLine("Shared Board title:");
        final int rows= Console.readInteger("Number of rows:");
        final int columns = Console.readInteger("Number of columns");
        List<String> rowTitles = new ArrayList<>();
        List<String> columnTitles = new ArrayList<>();
        boolean updateRows = UiUtils.confirm("Assign titles to shared board rows (S/N)");
            if (updateRows) {

                for (int i = 1; i <= rows; i++) {
                    System.out.printf("Row %d ", i);
                    String rowTitle = Console.readLine("Title:");
                    rowTitles.add(rowTitle);
                }
            }
            boolean updateColumns = UiUtils.confirm("Assign titles to shared board columns (S/N)");
            if (updateColumns) {
                for (int j = 1; j <= columns; j++) {
                    System.out.printf("Column %d ", j);
                    String columnTitle = Console.readLine("Title");
                    columnTitles.add(columnTitle);
                }
            }
            boolean save = UiUtils.confirm("Save shared board? (S/N)");

            if(save) {
                SBPMessage saveBoard= ctrl.createSharedBoard(title,rows, columns, rowTitles, columnTitles);
                if (saveBoard.getCode().equals(SBPMessage.Code.SUCCESS_SAVE_BOARD)){
                    System.out.println("Create shared board successfully.");
                } else if (saveBoard.getCode().equals(SBPMessage.Code.ERR)){
                    System.out.println(saveBoard.getDataAsString());
                    System.out.println("Create shared board unsuccessfully");
                }
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Create shared board unsuccessfully");
        }

        return false;
    }


    @Override
    public String headline() {
        return "Create Shared Board";
    }
}
