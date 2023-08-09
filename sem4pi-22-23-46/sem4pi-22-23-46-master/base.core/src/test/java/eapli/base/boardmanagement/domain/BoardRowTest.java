package eapli.base.boardmanagement.domain;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */
public class BoardRowTest{

    @Test
    public void ensureRowsMustBePositive(){
        System.out.println("Row must be positive");
        assertThrows(IllegalArgumentException.class, () -> new BoardRow(-1, null));
    }

    @Test
    public void ensureRowsMustBeLessOrEqualThanLimit(){
        System.out.println("Row must be less or equal than limit");
        assertThrows(IllegalArgumentException.class, () -> new BoardRow(21, null));
    }

    @Test
    public void ensureRowsColumnIsWithNumber(){
        BoardRow board = new BoardRow(5, null);
        assertNotNull(board);
    }

}