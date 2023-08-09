package eapli.base.boardmanagement.domain;

import eapli.base.Application;
import eapli.framework.domain.model.ValueObject;
import lombok.EqualsAndHashCode;
import validations.util.Preconditions;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */
@Embeddable
@EqualsAndHashCode
public class NumberOfColumns implements ValueObject, Serializable {
    private static final long serialVersionUID = 1L;
    //@JsonProperty
    private final int numberOfColumns;

    protected NumberOfColumns(final int numberOfColumns) {
        Preconditions.isWithinMargin(numberOfColumns, 1, Application.settings().getSharedBoardMaxColumns(), "Invalid number of columns");
        this.numberOfColumns = numberOfColumns;
    }

    protected NumberOfColumns() {
        this.numberOfColumns=-1;
    }

    public int numberOfColumns(){
        return this.numberOfColumns;
    }

    public static NumberOfColumns valueOf(final int numberRows) {
        return new NumberOfColumns(numberRows);
    }

    @Override
    public String toString() {
        return String.format("%d",numberOfColumns);
    }
}
