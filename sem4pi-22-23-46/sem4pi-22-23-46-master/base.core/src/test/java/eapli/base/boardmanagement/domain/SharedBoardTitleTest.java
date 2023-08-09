package eapli.base.boardmanagement.domain;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */

public class SharedBoardTitleTest {

    @Test
    public void ensureSharedBoardTitleMustBeNotNull(){
        System.out.println("Title of board must be not null");
        assertThrows(IllegalArgumentException.class, () -> new SharedBoardTitle(null));
    }

    @Test
    public void ensureSharedBoardTitleHasTitle(){
        System.out.println("Title of board is not null");
        SharedBoardTitle title = new SharedBoardTitle("SB TITLE TEST");
        System.out.println(title.title());
        assertNotNull(title.title());
    }



}