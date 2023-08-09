package eapli.base.coursemanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import lombok.Value;
import lombok.experimental.Accessors;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * A Course Name
 */
@Embeddable
@Value
@Accessors(fluent = true)
public final class CourseName implements ValueObject, Serializable{

    @SuppressWarnings("squid:S4784")
    private static final Pattern VALID_NAME_REGEX = Pattern.compile("^[A-Z][a-zA-Z ',.\\-]*$",
            Pattern.CASE_INSENSITIVE);

    private final String courseName;

    public CourseName(String courseName) {
        Preconditions.nonEmpty(courseName,"The full name should neither be null nor empty");
        Preconditions.matches(VALID_NAME_REGEX, courseName, "Invalid full name: " + courseName);
        this.courseName = courseName;
    }

    protected CourseName() {
        // ORM only
        courseName = "";
    }

    /**
     * builds a FullName object
     * @param courseName
     *
     * @return a Course Name
     */
    public static CourseName valueOf(final String courseName) {
        return new CourseName(courseName);
    }

    @Override
    public String toString() {
        return courseName;
    }
}


