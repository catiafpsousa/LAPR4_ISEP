package eapli.base.teachermanagement.domain;
import eapli.framework.domain.model.ValueObject;
import validations.util.Preconditions;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class Acronym implements ValueObject, Serializable, Comparable<Acronym> {

    private String acronym;
    private static final Pattern VALID_CODE_REGEX = Pattern.compile("[a-zA-Z]{3}",
            Pattern.CASE_INSENSITIVE);

    public static boolean isAcronym(final String text) {
        final Matcher matcher = VALID_CODE_REGEX.matcher(text);
        return matcher.find();
    }

    public Acronym(String acronym) {
        Preconditions.nonEmpty(acronym, "Acronym can not be empty.");
        Preconditions.nonNull(acronym, "Acronym can not be null.");
        Preconditions.ensure(isAcronym(acronym));

        this.acronym = acronym.toUpperCase();
    }

    public Acronym() {

    }


    @Override
    public int hashCode() {
        return Objects.hash(acronym);
    }

    /**
     * builds a acronym object
     *
     * @param acronym
     *
     * @return a acronym
     */
    public static Acronym valueOf(final String acronym) {
        return new Acronym(acronym);
    }

    /**
     * @return the countryCode
     */
    public String acronym() {
        return acronym;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Acronym)) {
            return false;
        }

        final var other = (Acronym) o;

        if (!acronym.equals(other.acronym)) {
            return false;
        }
        return acronym == (other.acronym);
    }

    @Override
    public String toString() {
        return acronym;
    }

    @Override
    public int compareTo(Acronym o) {
        return this.acronym.compareTo(o.acronym);
    }
}

