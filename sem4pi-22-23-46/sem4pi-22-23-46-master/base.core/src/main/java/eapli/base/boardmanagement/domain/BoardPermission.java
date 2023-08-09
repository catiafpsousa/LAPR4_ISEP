package eapli.base.boardmanagement.domain;
import eapli.framework.domain.model.ValueObject;
import java.io.Serializable;

public enum BoardPermission implements ValueObject, Serializable {
    READ, WRITE
}
