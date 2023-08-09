package eapli.base.boardmanagement.domain;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */

public class NumberOfRowsTest  {
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


}