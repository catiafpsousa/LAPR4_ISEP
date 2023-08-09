package eapli.base.classmanagement.domain;

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
public class ClassTitle implements ValueObject, Comparable<ClassTitle>, Serializable{
    private static final long serialVersionUID = 1L;

    @JsonProperty
    private final String title;

    protected ClassTitle() {
        this.title=null;
    }

    private ClassTitle (final String title){
        Preconditions.nonEmpty(title, "Class title cannot be empty");
        this.title=title;
    }

    protected String title(){
        return this.title;
    }

    public static ClassTitle valueOf(final String title) {
        return new ClassTitle(title);
    }

    @Override
    public int compareTo(ClassTitle o) {
        return this.title.compareTo(o.title);
    }

    @Override
    public String toString() {
        return title;
    }
}
