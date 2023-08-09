package eapli.base.boardmanagement.domain;

import eapli.base.infrastructure.authz.domain.model.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class CellTest {

    //USER
    private static final String EMAIL1 = "pcs@isep.pt";
    private static final String EMAIL2 = "ajs@isep.pt";
    private static final String EMAIL3 = "bfm@isep.pt";
    private static final String PASSWORD = "Password1";
    private static final String FULL_NAME = "Professor Teste Unitario";
    private static final String SHORT_NAME = "Teste";
    private static final String VAT_ID = "123456789";
    private static final int DAY = 12;
    private static final int MONTH = 01;
    private static final int YEAR = 1985;

    //SHARED BOARD
    private static final int ROW=4;
    private static final int COLUMN=4;
    private static final String TITLE="Title";
    private final Set<Role> roles = new HashSet<>();
    final SystemUserBuilder userBuilder = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder());
    final SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();
    final SystemUser user2 = userBuilder.with(EMAIL2, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();
    final BoardBuilder boardBuilder = new BoardBuilder();
    SharedBoard board =boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle(TITLE).build();


    @Test(expected = IllegalArgumentException.class)
    public void ensureCreateCellCantHaveColumnNull() {
        BoardRow row = new BoardRow(2, null);
        new Cell(row, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureCreateCellCantHaveRowNull() {
        BoardColumn column = new BoardColumn(3, null);
        new Cell(null, column);
    }

    @Test
    public void verifyCellCanHavePostIt() {
      Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        cell1.addPostIt(user1, new Content("conteudo1"));
        Cell cell2 = it.next();
        assertTrue(cell2.canHavePostIt());
    }

    @Test
    public void verifyCellCanNotHavePostIt() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        Cell cell2 = it.next();
        cell2.addPostIt(user1, new Content("conteudo2"));
        assertFalse(cell2.canHavePostIt());
    }

    @Test
    public void ensureCellAddPostItCorrectly() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        cell1.addPostIt(user1, new Content("conteudo1"));
        PostIt addedPostIt = new PostIt(user1, new Content("conteudo1"));
        assertTrue((cell1.latestActivePostIt().postItOwner().equals(addedPostIt.postItOwner())) && ( cell1.latestActivePostIt().content().equals(addedPostIt.content())));
    }


    @Test
    public void verifyIfCellAddPostItNotWrong() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        cell1.addPostIt(user1, new Content("conteudo1"));
        PostIt addedPostIt = new PostIt(user1, new Content("conteudoErrado"));
        assertFalse((cell1.latestActivePostIt().postItOwner().equals(addedPostIt.postItOwner())) && ( cell1.latestActivePostIt().content().equals(addedPostIt.content())));
    }

    @Test
    public void ensureCellMovedPostItCorrectly() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        PostIt parent = new PostIt(user1, new Content("conteudo1"));
        cell1.addMovedPostIt(parent);
        assertTrue((cell1.latestActivePostIt().postItOwner().equals(parent.postItOwner())) && ( cell1.latestActivePostIt().content().equals(parent.content())));
    }


    @Test
    public void verifyIfCellAddMovedPostItNotWrong() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        PostIt parent = new PostIt(user1, new Content("conteudo1"));
        cell1.addMovedPostIt(parent);
        PostIt movedPostIt = new PostIt(user1, new Content("conteudoErrado"));
        assertFalse((cell1.latestActivePostIt().postItOwner().equals(movedPostIt.postItOwner())) && ( cell1.latestActivePostIt().content().equals(movedPostIt.content())));
    }

    @Test
    public void ensureCellUpdatedPostItCorrectly() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        PostIt original = new PostIt(user1, new Content("conteudo"));
        PostIt updated = new PostIt(original, new Content("updated"));
        cell1.updatePostIt(original, new Content("updated"));
        assertTrue((cell1.latestActivePostIt().postItOwner().equals(updated.postItOwner())) && ( cell1.latestActivePostIt().content().equals(updated.content())));
    }

    @Test
    public void verifyCellUpdatedPostItNotWrong() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        PostIt original = new PostIt(user1, new Content("conteudo"));
        cell1.updatePostIt(original, new Content("updated"));
        assertFalse((cell1.latestActivePostIt().postItOwner().equals(original.postItOwner())) && ( cell1.latestActivePostIt().content().equals(original.content())));
    }

    @Test
    public void ensureCellHasLatestPostItCorrectly() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        PostIt original = new PostIt(user1, new Content("conteudo"));
        cell1.postItList().add(original);
        PostIt latest = cell1.latestActivePostIt();
        assertTrue(original.equals(latest));
    }

    @Test
    public void verifyCellHasNotLatestPostItIfDeleted() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        PostIt original = new PostIt(user1, new Content("conteudo"));
        original.deletePostIt();
        cell1.postItList().add(original);
        assertNull(cell1.latestActivePostIt());
    }
}