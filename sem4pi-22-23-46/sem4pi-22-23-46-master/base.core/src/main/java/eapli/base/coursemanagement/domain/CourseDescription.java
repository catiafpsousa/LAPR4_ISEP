package eapli.base.coursemanagement.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.ValueObject;

import eapli.framework.validations.Preconditions;
import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class CourseDescription implements ValueObject, Serializable  {
    private static final long serialVersionUID = 1L;
    @JsonProperty
    private final String description;

    protected CourseDescription(final String description) {
        Preconditions.nonEmpty(description, "Course description should neither be null nor empty");
        // To be implemented according to rules not yet clarified
//        Preconditions.ensure(StringPredicates.isCourse(description), "Invalid Description format");
        this.description = description;
    }

    protected CourseDescription() {
        this.description = "";
    }

    public static CourseDescription valueOf(final String description) {
        return new CourseDescription(description);
    }

    public String toString() {
        return this.description;
    }


    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CourseDescription)) {
            return false;
        } else {
            CourseDescription other = (CourseDescription)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$description = this.description;
                Object other$description = other.description;
                if (this$description == null) {
                    if (other$description != null) {
                        return false;
                    }
                } else if (!this$description.equals(other$description)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CourseDescription;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $description = this.description;
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        return result;
    }
}
