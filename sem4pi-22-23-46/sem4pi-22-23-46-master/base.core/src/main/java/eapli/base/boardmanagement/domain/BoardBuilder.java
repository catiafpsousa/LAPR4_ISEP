package eapli.base.boardmanagement.domain;

import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.model.DomainFactory;


public class BoardBuilder implements DomainFactory<SharedBoard> {
    private SystemUser owner;
    private SharedBoardTitle title;
    private NumberOfRows rowNumber;
    private NumberOfColumns columnNumber;

    public BoardBuilder withOwner(final SystemUser owner){
        this.owner=owner;
        return this;
    }

    public BoardBuilder withTitle(final String title){
        return withTitle(SharedBoardTitle.valueOf(title));
    }

    public BoardBuilder withTitle(final SharedBoardTitle title){
        this.title=title;
        return this;
    }

    public BoardBuilder withColumnNumber(final int columnNumber){
        return withColumnNumber(NumberOfColumns.valueOf(columnNumber));
    }

    public BoardBuilder withColumnNumber(NumberOfColumns columnNumber){
        this.columnNumber=columnNumber;
        return this;
    }

    public BoardBuilder withRowNumber(final int rowNumber){
        return withRowNumber(NumberOfRows.valueOf(rowNumber));
    }

    public BoardBuilder withRowNumber(NumberOfRows rowNumber){
        this.rowNumber=rowNumber;
        return this;
    }

    @Override
    public SharedBoard build() {
        return new SharedBoard(this.owner, this.title, this.rowNumber, this.columnNumber);
    }
}
