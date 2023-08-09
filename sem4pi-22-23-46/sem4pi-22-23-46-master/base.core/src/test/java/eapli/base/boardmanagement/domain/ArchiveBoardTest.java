package eapli.base.boardmanagement.domain;

import eapli.base.infrastructure.authz.domain.model.*;
import org.junit.Test;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static eapli.base.infrastructure.authz.model.SystemUserTest.ADMIN;
import static org.junit.Assert.assertFalse;

public class ArchiveBoardTest {
    private static final String EMAIL1 = "pcs@isep.pt";

    private static final String PASSWORD = "Password1";

    private static final String FULL_NAME = "Professor Teste Unitario";

    private static final String SHORT_NAME = "Teste";

    private static final String VAT_ID = "123456789";

    private static final int DAY = 12;

    private static final int MONTH = 01;

    private static final int YEAR = 1985;


    @Test
    public void ensureBoardStatusIsArchievedAfterArchive() throws Exception {

        SystemUserBuilder userBuilder = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder());

        SystemUser user = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).build();
        SharedBoardTitle title = new SharedBoardTitle("Title");
        NumberOfColumns columns = new NumberOfColumns(3);
        NumberOfRows rows = new NumberOfRows(4);

        SharedBoard board = new SharedBoard(user, title, rows, columns);

        Calendar now = Calendar.getInstance();

        board.archive(now);

        final boolean expected = board.isActive();
        assertFalse(expected);
    }
}
