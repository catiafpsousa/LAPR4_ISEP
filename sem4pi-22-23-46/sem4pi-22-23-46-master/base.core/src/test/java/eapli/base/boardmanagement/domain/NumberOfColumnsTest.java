package eapli.base.boardmanagement.domain;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */
public class NumberOfColumnsTest {
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

}