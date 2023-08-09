/*
 * Copyright (c) 2013-2023 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package eapli.base.clientusermanagement.domain;

import java.util.Calendar;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.base.infrastructure.authz.domain.model.FullName;
import eapli.base.infrastructure.authz.domain.model.Password;
import eapli.base.infrastructure.authz.domain.model.ShortName;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;

import eapli.framework.validations.Preconditions;

/**
 * A Signup Request. This class represents the Signup Request created right
 * after a person applies for a Client User account.
 *
 * <p>
 * It follows a DDD approach where all of its properties are instances of value
 * objects. This approach may seem a little more complex than just having String
 * or native type attributes but provides for real semantic of the domain and
 * follows the Single Responsibility Pattern.
 *
 * @author Jorge Santos ajs@isep.ipp.pt
 *
 */
@Entity
public class SignupRequest implements AggregateRoot<EmailAddress> {

    private static final long serialVersionUID = 1L;

    @Version
    private Long version;

    @EmbeddedId
    private EmailAddress email;
    private Password password;
    private FullName fullName;
    private ShortName shortName;

    private MecanographicNumber mecanographicNumber;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;
    @Temporal(TemporalType.DATE)
    private Calendar createdOn;

    /* package */ SignupRequest(final EmailAddress email, final Password password, final FullName fullName, final ShortName shortName,
                                final MecanographicNumber mecanographicNumber,
                                final Calendar createdOn) {
        Preconditions.noneNull(email, password, fullName,shortName, mecanographicNumber);

        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.shortName = shortName;
        this.mecanographicNumber = mecanographicNumber;
        // by default
        approvalStatus = ApprovalStatus.PENDING;
        this.createdOn = createdOn;
    }

    protected SignupRequest() {
        // for ORM only
    }

    public void accept() {
        approvalStatus = ApprovalStatus.ACCEPTED;
    }

    public void refuse() {
        approvalStatus = ApprovalStatus.REFUSED;
    }

    @Override
    public boolean equals(final Object o) {
        return DomainEntities.areEqual(this, o);
    }

    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }

    @Override
    public boolean sameAs(final Object other) {
        if (!(other instanceof SignupRequest)) {
            return false;
        }

        final SignupRequest that = (SignupRequest) other;
        if (this == that) {
            return true;
        }

        return email.equals(that.email) && password.equals(that.password)
                && fullName.equals(that.fullName) && shortName.equals(that.shortName)
                && mecanographicNumber.equals(that.mecanographicNumber);
    }

    public MecanographicNumber mecanographicNumber() {
        return mecanographicNumber;
    }

    @Override
    public EmailAddress identity() {
        return email;
    }

    public FullName fullName() {
        return fullName;
    }

    public ShortName shortName() {
        return shortName;
    }

    public boolean isPending() {
        return approvalStatus == ApprovalStatus.PENDING;
    }


    public Password password() {
        return password;
    }
}
