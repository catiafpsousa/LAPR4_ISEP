package eapli.base.infrastructure.authz.domain.model;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.base.strings.util.StringPredicates;
import eapli.framework.domain.model.ValueObject;
import eapli.framework.strings.StringMixin;



import eapli.framework.validations.Preconditions;
import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class EmailAddress implements ValueObject, Comparable<EmailAddress>, Serializable, StringMixin {
    private static final long serialVersionUID = 1L;
    @JsonProperty
    private final String email;

    protected EmailAddress(final String address) {
        Preconditions.nonEmpty(address, "email address should neither be null nor empty");
        Preconditions.ensure(StringPredicates.isEmail(address), "Invalid E-mail format");
        this.email = address;
    }

    protected EmailAddress() {
        this.email = "";
    }

    public static EmailAddress valueOf(final String address) {
        return new EmailAddress(address);
    }

    public String toString() {
        return this.email;
    }

    public int compareTo(final EmailAddress o) {
        return this.email.compareTo(o.email);
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof EmailAddress)) {
            return false;
        } else {
            EmailAddress other = (EmailAddress)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$email = this.email;
                Object other$email = other.email;
                if (this$email == null) {
                    if (other$email != null) {
                        return false;
                    }
                } else if (!this$email.equals(other$email)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof EmailAddress;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $email = this.email;
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        return result;
    }
}

