package eapli.base.infrastructure.authz.domain.model;


import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import lombok.Value;
import lombok.experimental.Accessors;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * A Person's Full Name
 */
@Embeddable
@Value
@Accessors(fluent = true)
public final class FullName implements ValueObject, Serializable{

    @SuppressWarnings("squid:S4784")
    private static final Pattern VALID_NAME_REGEX = Pattern.compile("^[A-Z][a-zA-Z ',.\\-]*$",
            Pattern.CASE_INSENSITIVE);

    private final String fullName;

    public FullName(String fullName) {
        Preconditions.nonEmpty(fullName,"The full name should neither be null nor empty");
        Preconditions.matches(VALID_NAME_REGEX, fullName, "Invalid full name: " + fullName);
        this.fullName = fullName;
    }

    protected FullName() {
        // ORM only
        fullName = "";
    }

    /**
     * builds a FullName object
     * @param fullName
     *
     * @return a Full Name
     */
    public static FullName valueOf(final String fullName) {
        return new FullName(fullName);
    }

    @Override
    public String toString() {
        return fullName;
    }
}

