package eapli.base.boardmanagement.domain;

import eapli.base.infrastructure.authz.domain.model.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

public class BoardModificationTest {


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
    private static final int ROW = 4;
    private static final int COLUMN = 4;
    private static final String TITLE = "Title";
    private final Set<Role> roles = new HashSet<>();
    final SystemUserBuilder userBuilder = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder());
    final SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH, YEAR).withRoles(roles).build();
    final SystemUser user2 = userBuilder.with(EMAIL2, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH, YEAR).withRoles(roles).build();
    final BoardBuilder boardBuilder = new BoardBuilder();
    SharedBoard board = boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle(TITLE).build();


    @Test(expected = NullPointerException.class)
    public void ensureCreateBoardModificationCantHavePostItNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(null, ModificationType.CREATION, cell1);
    }


    @Test(expected = NullPointerException.class)
    public void ensureCreateBoardModificationCantHaveTypeNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(new PostIt(user1, new Content("conteudo")), null, cell1);
    }

    @Test(expected = NullPointerException.class)
    public void ensureCreateBoardModificationCantHaveCellNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(new PostIt(user1, new Content("conteudo")), ModificationType.CREATION, null);
    }

    @Test(expected = NullPointerException.class)
    public void ensureUpdateBoardModificationCantHavePostItNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(null, ModificationType.CONTENT_UPDATE, cell1, new Content("novo conteudo"));
    }

    @Test
    public void ensureUpdateBoardModificationCanHaveTypeNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(new PostIt(user1, new Content("conteudo")), null, cell1, new Content("novo conteudo"));
    }

    @Test(expected = NullPointerException.class)
    public void ensureUpdateBoardModificationCantHaveCellNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(new PostIt(user1, new Content("conteudo")), ModificationType.CONTENT_UPDATE, null, new Content("novo conteudo"));
    }

    @Test
    public void ensureUpdateBoardModificationCanHaveContentNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(new PostIt(user1, new Content("conteudo")), ModificationType.CONTENT_UPDATE, cell1, (Content) null);
    }


    @Test(expected = NullPointerException.class)
    public void ensureMoveBoardModificationCantHavePostItNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        Cell cell2 = it.next();
        new BoardModification(null, ModificationType.MOVED, cell1, cell2);
    }


    @Test
    public void ensureMoveBoardModificationCanHaveTypeNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        Cell cell2 = it.next();
        new BoardModification(new PostIt(user1, new Content("conteudo")), null, cell1, cell2);
    }

    @Test(expected = NullPointerException.class)
    public void ensureMoveBoardModificationCantHaveBothCellsNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(new PostIt(user1, new Content("conteudo")), ModificationType.MOVED, (Cell) null, (Cell) null);
    }


    @Test(expected = NullPointerException.class)
    public void ensureMoveBoardModificationCantHaveSourceCellNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(new PostIt(user1, new Content("conteudo")), ModificationType.MOVED, (Cell) null, cell1);
    }


    @Test(expected = NullPointerException.class)
    public void ensureMoveBoardModificationCantHaveDestinationCellNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(new PostIt(user1, new Content("conteudo")), ModificationType.MOVED, cell1, (Cell) null);
    }


    @Test(expected = NullPointerException.class)
    public void ensureDeleteBoardModificationCantHavePostItNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(null, ModificationType.DELETE, cell1);
    }


    @Test(expected = NullPointerException.class)
    public void ensureDeleteBoardModificationCantHaveTypeNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(new PostIt(user1, new Content("conteudo")), null, cell1);
    }

    @Test(expected = NullPointerException.class)
    public void ensureUndoBoardModificationCantHaveCellNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(new PostIt(user1, new Content("conteudo")), ModificationType.DELETE, null);
    }


    @Test(expected = NullPointerException.class)
    public void ensureUndoBoardModificationCantHavePostItNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(null, ModificationType.DELETE, cell1);
    }


    @Test(expected = NullPointerException.class)
    public void ensureUndoBoardModificationCantHaveTypeNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(new PostIt(user1, new Content("conteudo")), null, cell1);
    }

    @Test(expected = NullPointerException.class)
    public void ensureDeleteBoardModificationCantHaveCellNull() {
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        new BoardModification(new PostIt(user1, new Content("conteudo")), ModificationType.DELETE, null);
    }


    @Test
    public void ensureModificationDescriptionIsCorrectForCreation() {
        PostIt createdPostIt = new PostIt(user1, new Content("conteudo"));
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        BoardModification creation = new BoardModification(createdPostIt, ModificationType.CREATION, cell1);
        String expectedDescription = "PostIt created by " + createdPostIt.postItOwner().identity().toString() +  " on cell (" + cell1.row().number() + ", " + cell1.column().number() + ")";
        assertEquals(expectedDescription, creation.description().toString());
    }


    @Test
    public void ensureModificationDescriptionIsCorrectForUpdate() {
        PostIt createdPostIt = new PostIt(user1, new Content("conteudo"));
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        BoardModification update = new BoardModification(createdPostIt, ModificationType.CONTENT_UPDATE, cell1, new Content("novo conteudo"));
        String expectedDescription = "PostIt of cell (" + cell1.row().number() + ", " + cell1.column().number() + ")" + "changed content: " + createdPostIt.content() + " -->" + "novo conteudo" + "(done by " + createdPostIt.postItOwner().identity().toString() + ")";
        assertEquals(expectedDescription, update.description().toString());
    }


    @Test
    public void ensureModificationDescriptionIsCorrectForMove() {
        PostIt createdPostIt = new PostIt(user1, new Content("conteudo"));
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        Cell cell2 = it.next();
        BoardModification move = new BoardModification(createdPostIt, ModificationType.MOVED, cell1, cell2);
        String expectedDescription = "PostIt moved by " + createdPostIt.postItOwner().identity().toString() + " from cell (" + cell1.row().number() + ", " + cell1.column().number() + ") to (" + cell2.row().number() + ", " + cell2.column().number() + ")";
        assertEquals(expectedDescription, move.description().toString());
    }


    @Test
    public void ensureModificationDescriptionIsCorrectForDelete() {
        PostIt createdPostIt = new PostIt(user1, new Content("conteudo"));
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        BoardModification delete = new BoardModification(createdPostIt, ModificationType.DELETE, cell1);
        String expectedDescription = "PostIt deleted by " + createdPostIt.postItOwner().identity().toString() + "from cell (" + cell1.row().number() + ", " + cell1.column().number() + ")";
        assertEquals(expectedDescription, delete.description().toString());
    }


    @Test
    public void ensureModificationDescriptionIsCorrectForUndo() {
        PostIt createdPostIt = new PostIt(user1, new Content("conteudo"));
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        BoardModification undo = new BoardModification(createdPostIt, ModificationType.UNDO, cell1);
        String expectedDescription = "Undo operation on PostIt by " + createdPostIt.postItOwner().identity().toString();
        assertEquals(expectedDescription, undo.description().toString());
    }
}