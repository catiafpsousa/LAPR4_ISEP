package eapli.base.infrastructure.authz.domain.model;
import eapli.framework.domain.model.ValueObject;
import validations.util.Preconditions;

import javax.persistence.*;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class VatID implements ValueObject, Serializable, Comparable<VatID> {

    private String vatID;
    private static final Pattern VALID_CODE_REGEX = Pattern.compile("[0-9]{9}",
            Pattern.CASE_INSENSITIVE);

    public static boolean isVatId(final String text) {
        final Matcher matcher = VALID_CODE_REGEX.matcher(text);
        return matcher.find();
    }

    public VatID(String vatID) {
        Preconditions.nonEmpty(vatID, "Vat ID can not be null.");
        Preconditions.nonNull(vatID, "Vat ID can not be null.");
        Preconditions.ensure(isVatId(vatID));

        this.vatID = vatID;
    }

    public VatID() {

    }


    /**
     * builds a vatID object
     *
     * @param vatID
     *
     * @return a vatID
     */
    public static VatID valueOf(final String vatID) {
        return new VatID(vatID);
    }

    /**
     * @return the countryCode
     */
    public String getVatID() {
        return vatID;
    }

    public void setVatID(String vatID) {
        this.vatID = vatID;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VatID)) {
            return false;
        }

        final var vat = (VatID) o;

        if (!vatID.equals(vat.vatID)) {
            return false;
        }
        return vatID == (vat.vatID);
    }

    @Override
    public String toString() {
        return vatID;
    }


    @Override
    public int compareTo(final VatID arg0) {
        return vatID.compareTo(arg0.vatID);
    }
}

