package eapli.base.boardmanagement.domain;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */
public class BoardColumnTest {

    @Test
    public void ensureColumnsMustBePositive(){
        System.out.println("Column must be positive");
        assertThrows(IllegalArgumentException.class, () -> new BoardColumn(-1, null));
    }

    @Test
    public void ensureColumnsMustBeLessOrEqualThanLimit(){
        System.out.println("Column must be less or equal than limit");
        assertThrows(IllegalArgumentException.class, () -> new BoardColumn(1000, null));
    }

    @Test
    public void ensureBoardColumnIsWithNumber(){
        BoardColumn board = new BoardColumn(5, null);
        assertNotNull(board);
    }


}