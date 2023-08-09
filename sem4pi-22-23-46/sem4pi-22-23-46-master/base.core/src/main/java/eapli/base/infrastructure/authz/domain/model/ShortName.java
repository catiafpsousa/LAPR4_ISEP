package eapli.base.infrastructure.authz.domain.model;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;

import java.io.Serializable;
import java.util.regex.Pattern;
import javax.persistence.Embeddable;
import lombok.Value;
import lombok.experimental.Accessors;

/**
 * A Person's Short Name
 */
@Embeddable
@Value
@Accessors(fluent = true)
public class ShortName implements ValueObject, Serializable {

    @SuppressWarnings("squid:S4784")
    private static final Pattern VALID_NAME_REGEX = Pattern.compile("^[A-Z][a-zA-Z ',.\\-]*$",
            Pattern.CASE_INSENSITIVE);

    private final String shortName;

    public ShortName(String shortName) {
        Preconditions.nonEmpty(shortName,"The short name should neither be null nor empty");
        Preconditions.matches(VALID_NAME_REGEX, shortName, "Invalid short name: " + shortName);
        this.shortName = shortName;
    }

    protected ShortName() {
        // ORM only
        shortName = "";
    }

    /**
     * builds a ShortName object
     * @param shortName
     *
     * @return a Short Name
     */
    public static ShortName valueOf(final String shortName) {
        return new ShortName(shortName);
    }

    @Override
    public String toString() {
        return shortName;
    }
}
