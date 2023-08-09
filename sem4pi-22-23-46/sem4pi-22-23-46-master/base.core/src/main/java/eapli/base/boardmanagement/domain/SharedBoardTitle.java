package eapli.base.boardmanagement.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */
@Embeddable
@EqualsAndHashCode
public class SharedBoardTitle implements ValueObject, Serializable {
    private static final long serialVersionUID = 1L;
    //@JsonProperty
    private final String title;

    protected SharedBoardTitle(final String title) {
        Preconditions.nonEmpty(title, "Shared board title cannot be empty.");
        this.title=title;
    }

    protected SharedBoardTitle() {
        this.title=null;
    }

    public String title(){
        return this.title;
    }

    public static SharedBoardTitle valueOf(final String title) {
        return new SharedBoardTitle(title);
    }

    @Override
    public String toString() {
        return this.title;
    }
}
