package eapli.base.boardmanagement.domain;

import eapli.framework.domain.model.ValueObject;

import java.io.Serializable;

public enum BoardStatus implements ValueObject, Serializable {
    ACTIVE, ARCHIVED;
}
