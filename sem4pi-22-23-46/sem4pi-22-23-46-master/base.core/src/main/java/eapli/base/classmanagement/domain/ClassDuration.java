package eapli.base.classmanagement.domain;
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
public class ClassDuration implements ValueObject, Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * class duration in minutes
     */
    private final int classDuration;

    protected ClassDuration(final int classDuration) {
        Preconditions.isPositive(classDuration, "Class duration must be positive");
        this.classDuration = classDuration;
    }

    protected ClassDuration() {
        this.classDuration=-1;
    }

    protected int classDuration() {
        return classDuration;
    }

    public static ClassDuration valueOf(final int classDuration) {
        return new ClassDuration(classDuration);
    }

    @Override
    public String toString() {
        return String.format("%d",classDuration);
    }
}
