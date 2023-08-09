package eapli.base.coursemanagement.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.ValueObject;

import eapli.framework.validations.Preconditions;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CourseCode implements ValueObject, Comparable<CourseCode>, Serializable  {
    private static final long serialVersionUID = 1L;
    @JsonProperty
    private final String code;

    public CourseCode(final String code) {
        Preconditions.nonEmpty(code, "Course code should neither be null nor empty");
        // To be implemented according to rules not yet clarified
//        Preconditions.ensure(StringPredicates.isCourse(code), "Invalid Course Code format");
        this.code = code;
    }

    protected CourseCode() {
        this.code = "";
    }

    public static CourseCode valueOf(final String code) {
        return new CourseCode(code);
    }

    public String toString() {
        return this.code;
    }

    public int compareTo(final CourseCode o) {
        return this.code.compareTo(o.code);
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CourseCode)) {
            return false;
        } else {
            CourseCode other = (CourseCode)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$code = this.code;
                Object other$code = other.code;
                if (this$code == null) {
                    if (other$code != null) {
                        return false;
                    }
                } else if (!this$code.equals(other$code)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CourseCode;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $code = this.code;
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        return result;
    }
}
