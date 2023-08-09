package eapli.base.boardmanagement.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.base.Application;
import eapli.framework.domain.model.ValueObject;
import org.springframework.lang.Nullable;
import validations.util.Preconditions;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */
@Embeddable
public class BoardColumn implements ValueObject, Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "columnnumber")
    private final int number;

    @Nullable
    @Column(name = "columntitle")
    private String title;

    protected BoardColumn(final int number, final String title) {
        this.title=title;
        Preconditions.isWithinMargin(number, 1, Application.settings().getSharedBoardMaxColumns(), "Invalid number of columns");
        this.number=number;
    }

    protected BoardColumn() {
        this.number=-1;
        this.title=null;
    }

    public int number(){
        return this.number;
    }

    public static BoardColumn valueOf(final int number, final String title) {
        return new BoardColumn(number, title);
    }

    protected void updateColumnTitle(final String newTitle){
        this.title=newTitle;
    }

    public String title() {
        return this.title;
    }
}
