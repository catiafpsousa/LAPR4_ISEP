package eapli.base.enrollmentmanagement.domain;

import eapli.framework.domain.model.ValueObject;
import validations.util.Preconditions;

import java.util.UUID;

public class EnrollmentToken implements ValueObject, Comparable<EnrollmentToken>{

    private static final long serialVersionUID = -1820803567379631580L;

    private final String token;

    protected EnrollmentToken() {
        // for ORM
        token = null;
    }

    private EnrollmentToken(final String value) {
        Preconditions.nonEmpty(value);
        token = UUID.fromString(value).toString();
    }

    /**
     * Factory method.
     *
     * @param value
     *
     * @return
     */
    public static EnrollmentToken valueOf(final String value) {
        return new EnrollmentToken(value);
    }

    /**
     * Factory method.
     *
     * @return
     */
    public static EnrollmentToken newToken() {
        return valueOf(UUID.randomUUID().toString());
    }

    @Override
    public int compareTo(final EnrollmentToken o) {
        return token.compareTo(o.token);
    }

    @Override
    public String toString() {
        return token;
    }

}
