package eapli.base.boardmanagement.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.base.Application;
import eapli.framework.domain.model.ValueObject;
import lombok.EqualsAndHashCode;
import org.springframework.lang.Nullable;
import validations.util.Preconditions;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */
@Embeddable
public class BoardRow implements ValueObject, Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "rownumber")
    private final int number;

    @Nullable
    @Column(name = "rowtitle")
    private String title;

    protected BoardRow(final int number, final String title) {
        this.title=title;
        Preconditions.isWithinMargin(number,1, Application.settings().getSharedBoardMaxRows(), "Invalid row number");
        this.number=number;
    }

    protected BoardRow() {
        this.number=-1;
        this.title=null;
    }

    public static BoardRow valueOf(final int number, final String title) {
        return new BoardRow(number, title);
    }

    public int number(){
        return this.number;
    }

    public String title(){
        return this.title;
    }

    public void updateRowTitle(final String newTitle){
        this.title=newTitle;
    }
}
