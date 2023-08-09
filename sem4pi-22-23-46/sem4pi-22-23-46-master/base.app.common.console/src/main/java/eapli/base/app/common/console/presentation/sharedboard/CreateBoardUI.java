package eapli.base.app.common.console.presentation.sharedboard;

import eapli.base.boardmanagement.application.CreateBoardController;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.base.util.UiUtils;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;

public class CreateBoardUI extends AbstractUI{
    private final CreateBoardController ctrl= new CreateBoardController();
    @Override
    protected boolean doShow() {
        final String title = Console.readLine("Shared Board title:");
        final int rows= Console.readInteger("Number of rows:");
        final int columns = Console.readInteger("Number of columns");
        try{
            ctrl.createSharedBoard(title, rows, columns);

            boolean updateRows = UiUtils.confirm("Assign titles to shared board rows (S/N)");
            if (updateRows) {
                for (int i = 1; i <= rows; i++) {
                    System.out.printf("Row %d ", i);
                    String rowTitle = Console.readLine("Title:");
                    try {
                        ctrl.updateRowTitles(i, rowTitle);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            boolean updateColumns = UiUtils.confirm("Assign titles to shared board columns (S/N)");
            if (updateColumns) {
                for (int j = 1; j <= columns; j++) {
                    System.out.printf("Column %d ", j);
                    String columnTitle = Console.readLine("Title");
                    try {
                        ctrl.updateColumnTitles(j, columnTitle);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            boolean save = UiUtils.confirm("Save shared board? (S/N)");

            if(save) {
                System.out.println("**Create shared board successfully**");
                SharedBoard board = ctrl.saveSharedBoard();
                SharedBoardPrinter printer = new SharedBoardPrinter();
                printer.visit(board);
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
