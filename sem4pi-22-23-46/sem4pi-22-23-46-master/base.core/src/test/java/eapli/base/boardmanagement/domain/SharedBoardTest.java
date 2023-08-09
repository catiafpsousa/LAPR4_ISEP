package eapli.base.boardmanagement.domain;

import eapli.base.infrastructure.authz.domain.model.*;
import eapli.base.teachermanagement.domain.Teacher;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */

public class SharedBoardTest {
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

    @Test
    public void ensureSharedBoardHasOwner(){
        System.out.println("Shared Board owner can't be null");
        assertThrows(IllegalArgumentException.class, () -> boardBuilder.withOwner(null).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle(TITLE).build());
    }

    @Test
    public void ensureSharedBoardHasTitle(){
        System.out.println("Shared Board title can't be null");
        assertThrows(IllegalArgumentException.class, () -> boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle("").build());
    }

    @Test
    public void ensureSharedBoardHasRows(){
        System.out.println("Shared Board has rows");
        assertThrows(IllegalArgumentException.class, () -> boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(-1).withTitle(TITLE).build());
    }

    @Test
    public void ensureSharedBoardHasColumns(){
        System.out.println("Shared Board title has columns");
        assertThrows(IllegalArgumentException.class, () -> boardBuilder.withOwner(user1).withColumnNumber(-1).withRowNumber(ROW).withTitle(TITLE).build());
    }

    @Test
    public void ensureSharedBoardIsValid(){
        System.out.println("Shared Board is valid");
        SharedBoard board =boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle(TITLE).build();
        assertNotNull(board);
    }

    @Test
    public void ensureNumberOfColumnsMustBePositive(){
        System.out.println("Column must be positive");
        assertThrows(IllegalArgumentException.class, () -> new NumberOfColumns (-1));
    }

    @Test
    public void ensureNumberOfColumnsMustBeLessOrEqualThanLimit(){
        System.out.println("Column must be less or equal than limit");
        assertThrows(IllegalArgumentException.class, () -> new NumberOfColumns(1000));
    }

    @Test
    public void ensureNumberOfColumnsIsWithNumber(){
        NumberOfColumns number = new NumberOfColumns(5);
        assertNotNull(number);
    }


    @Test
    public void ensureNumberOfRowsMustBePositive(){
        System.out.println("Rows must be positive");
        assertThrows(IllegalArgumentException.class, () -> new NumberOfRows (-1));
    }

    @Test
    public void ensureNumberOfRowsMustBeLessOrEqualThanLimit(){
        System.out.println("Rows must be less or equal than limit");
        assertThrows(IllegalArgumentException.class, () -> new NumberOfRows(1000));
    }

    @Test
    public void ensureNumberOfRowsIsWithNumber(){
        NumberOfRows number = new NumberOfRows(5);
        assertNotNull(number);
    }

    @Test
    public void ensureBoardIsShared(){
        SharedBoard board = boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle(TITLE).build();
        board.shareABoard(user2, BoardPermission.READ);
        assertFalse(board.sharedlist().isEmpty());
    }

    @Test
    public void ensureShareABoarPermissionIsNotNull(){
        SharedBoard board = boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle(TITLE).build();
        assertThrows(IllegalArgumentException.class,()-> board.shareABoard(user2, null));
    }

    @Test
    public void ensureShareABoarUserIsNotNulll(){
        SharedBoard board = boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle(TITLE).build();
        assertThrows(IllegalArgumentException.class,()-> board.shareABoard(null, BoardPermission.READ));
    }


    @Test
    public void ensureAddCreationModificationIsCorrect(){
        SharedBoard board = boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle(TITLE).build();
        PostIt createdPostIt = new PostIt(user1, new Content("conteudo"));
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        BoardModification creation = new BoardModification(createdPostIt, ModificationType.CREATION, cell1);

        board.addCreationModification(createdPostIt, cell1);

        assertEquals(board.modifications().iterator().next().description(), creation.description());
    }


    @Test
    public void ensureAddUpdateModificationIsCorrect(){
        SharedBoard board = boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle(TITLE).build();
        PostIt createdPostIt = new PostIt(user1, new Content("conteudo"));
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        BoardModification update = new BoardModification(createdPostIt, ModificationType.CONTENT_UPDATE, cell1, new Content("novo conteudo"));

        board.addContentModification(createdPostIt, cell1, new Content("novo conteudo"));;

        assertEquals(board.modifications().iterator().next().description(), update.description());
    }


    @Test
    public void ensureAddDeleteModificationIsCorrect(){
        SharedBoard board = boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle(TITLE).build();
        PostIt createdPostIt = new PostIt(user1, new Content("conteudo"));
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        BoardModification delete = new BoardModification(createdPostIt, ModificationType.DELETE, cell1);

        board.addDeleteModification(createdPostIt, cell1);;

        assertEquals(board.modifications().iterator().next().description(), delete.description());
    }


    @Test
    public void ensureAddUndoModificationIsCorrect(){
        SharedBoard board = boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle(TITLE).build();
        PostIt createdPostIt = new PostIt(user1, new Content("conteudo"));
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        BoardModification undo = new BoardModification(createdPostIt, ModificationType.UNDO, cell1);

        board.addUndoModification(createdPostIt, cell1);;

        assertEquals(board.modifications().iterator().next().description(), undo.description());
    }

    @Test
    public void ensureCellIsAvailableIfHasNonePostIts(){
        SharedBoard board = boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle(TITLE).build();
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();

        assertTrue(board.availableCells().contains(cell1));
    }

    @Test
    public void ensureCellIsNotAvailableIfHasPostIts(){
        SharedBoard board = boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle(TITLE).build();
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        cell1.addPostIt(user1, new Content("conteudo"));

        assertFalse(board.availableCells().contains(cell1));
    }

    @Test
    public void ensureCellIsAvailableIfHasDeletedPostIt(){
        SharedBoard board = boardBuilder.withOwner(user1).withColumnNumber(COLUMN).withRowNumber(ROW).withTitle(TITLE).build();
        Iterator<Cell> it = board.cells().iterator();
        Cell cell1 = it.next();
        PostIt postIt = new PostIt(user1, new Content("conteudo"));
        postIt.deletePostIt();
        cell1.postItList().add(postIt);

        assertTrue(board.availableCells().contains(cell1));
    }
}