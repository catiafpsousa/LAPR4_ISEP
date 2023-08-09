package eapli.base.app.common.console.presentation.sharedboard;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.framework.visitor.Visitor;

public class SharedBoardPrinter implements Visitor<SharedBoard> {

    @Override
    public void visit(final SharedBoard visitee) {
        System.out.printf("Shared Board Information:\n");
        System.out.printf("ID: %d\n", visitee.identity());
        System.out.printf("Title: %s\n", visitee.title());
        System.out.printf("Number of Rows: %s\n",visitee.rowNumber().toString());
        System.out.printf("Number of Columns: %s\n", visitee.columnNumber().toString());
    }
}
