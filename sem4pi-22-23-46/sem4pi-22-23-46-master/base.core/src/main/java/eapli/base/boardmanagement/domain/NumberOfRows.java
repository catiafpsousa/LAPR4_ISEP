package eapli.base.boardmanagement.domain;

import eapli.base.Application;
import eapli.framework.domain.model.ValueObject;
import validations.util.Preconditions;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */
@Embeddable
public class NumberOfRows implements ValueObject, Serializable {
    private static final long serialVersionUID = 1L;
    //@JsonProperty
    private final int numberOfRows;

    protected NumberOfRows(final int numberOfRows) {
        Preconditions.isWithinMargin(numberOfRows,1, Application.settings().getSharedBoardMaxRows(), "Invalid row number");
        this.numberOfRows=numberOfRows;
    }

    protected NumberOfRows() {
        this.numberOfRows=-1;
    }

    public int numberOfRows(){
        return this.numberOfRows;
    }

    public static NumberOfRows valueOf(final int numberOfRows) {
        return new NumberOfRows(numberOfRows);
    }

    @Override
    public String toString() {
        return String.format("%d", numberOfRows);
    }
}
