/*
 * Copyright (c) 2013-2022 the original author or authors.
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
package eapli.base.infrastructure.authz.domain.model;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;

import eapli.framework.domain.model.DomainFactory;
import eapli.base.infrastructure.authz.application.PasswordPolicy;
import eapli.framework.validations.Preconditions;

/**
 * A factory for User entities. It helps construct the object and enforces the
 * password policies and encoding.
 *
 * <p>
 * This class demonstrates the use of the factory (DDD) pattern using a fluent
 * interface; it acts as a Builder (GoF). For a class such as {@link SystemUser} there is not really
 * a need for a builder
 * and a simple factory or even the constructor would suffice.
 */
public class SystemUserBuilder implements DomainFactory<SystemUser> {

    private static final Logger LOGGER = LogManager.getLogger(SystemUserBuilder.class);

    private EmailAddress email;
    private Password password;
    private FullName fullName;
    private ShortName shortName;
    private VatID vatID;
    private final RoleSet roles;
    private BirthDate birthdate;
    private Calendar createdOn;


    private final PasswordPolicy policy;
    private final PasswordEncoder encoder;

    public SystemUserBuilder(final PasswordPolicy policy, final PasswordEncoder encoder) {
        this.policy = policy;
        this.encoder = encoder;
        roles = new RoleSet();
    }

    /**
     * Helper for the most common and mandatory properties of a SystemUser
     * *
     * @param email
     * @param password
     * @param fullName
     * @param shortName
     * @param vatID
     * @param day
     * @param month
     * @param year
     * @return this SystemUserBuilder
     */
    public SystemUserBuilder with(final String email, final String password,
            final String fullName,
            final String shortName, final String vatID,  final int day, final int month, final int year) {
        withEmail(email);
        withPassword(password);
        withFullName(fullName);
        withVatID(vatID);
        withShortName(shortName);
        withBirthDate(day, month, year);

        return this;
    }


    /**
     * Helper for the most common and mandatory properties of a SystemUser. Note
     * hat {@code password} is assumed to be already validated and encoded.
     * @param email
     * @param password
     * @param fullName
     * @param shortName
     * @param vatID
     * @param birthdate
     * @return this SystemUserBuilder
     */
    public SystemUserBuilder with(final EmailAddress email, final Password password, final FullName fullName,
                                 final ShortName shortName, final VatID vatID,  final BirthDate birthdate) {
        withEmail(email);
        withPassword(password);
        withFullName(fullName);
        withShortName(shortName);
        withVatID(vatID);
        withBirthDate(birthdate);
        return this;
    }

    /**
     * Sets the password of the user <strong>performing</strong> policy
     * enforcement and encoding. If the password does not meet the requirements
     * of the policy, an {@code IllegalArgumentException} is thrown.
     *
     * @param rawPassword
     * @throws IllegalArgumentException
     * @return this builder
     */
    public SystemUserBuilder withPassword(final String rawPassword) {
        password = Password.encodedAndValid(rawPassword, policy, encoder)
                .orElseThrow(IllegalArgumentException::new);
        return this;
    }

    /**
     * Sets the password of the user. This method is mostly to be used to
     * support the registration process where a password has already been
     * validated and encoded.
     *
     * @param password
     * @return this builder
     */
    public SystemUserBuilder withPassword(final Password password) {
        Preconditions.nonNull(password);
        this.password = password;
        return this;
    }


    public SystemUserBuilder withFullName(final String fullName) {
        this.fullName = FullName.valueOf(fullName);
        return this;
    }

    public SystemUserBuilder withFullName(final FullName fullName) {
        this.fullName = fullName;
        return this;
    }

    public SystemUserBuilder withShortName(final String shortName) {
        this.shortName = ShortName.valueOf(shortName);
        return this;
    }

    public SystemUserBuilder withShortName(final ShortName shortName) {
        this.shortName = shortName;
        return this;
    }

    public SystemUserBuilder withEmail(final String email) {
        this.email = EmailAddress.valueOf(email);
        return this;
    }

    public SystemUserBuilder withEmail(final EmailAddress email) {
        this.email = email;
        return this;
    }

    public SystemUserBuilder withRoles(final Role... onlyWithThis) {
        for (final Role each : onlyWithThis) {
            roles.add(new RoleAssignment(each));
        }
        return this;
    }

    public SystemUserBuilder withVatID (final String vatID) {
        this.vatID = VatID.valueOf(vatID);
        return this;
    }

    public SystemUserBuilder withVatID (final VatID vatID) {
        this.vatID = vatID;
        return this;
    }

    public SystemUserBuilder withBirthDate(int day, int month, int year) {
        this.birthdate = new BirthDate(day, month, year);
        return this;
    }

    public SystemUserBuilder withBirthDate(final BirthDate birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public SystemUserBuilder withRole(final RoleAssignment role) {
        roles.add(role);
        return this;
    }

    public SystemUserBuilder createdOn(final Calendar createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public SystemUserBuilder withRoles(final Set<Role> someRoles) {
        List<RoleAssignment> theRoles;
        if (createdOn == null) {
            theRoles = someRoles.stream().map(RoleAssignment::new).collect(Collectors.toList());
        } else {
            theRoles = someRoles.stream().map(rt -> new RoleAssignment(rt, createdOn))
                    .collect(Collectors.toList());
        }
        roles.addAll(theRoles);
        return this;
    }

    public SystemUserBuilder withRoles(final RoleSet roles) {
        this.roles.addAll(roles);
        return this;
    }

    @Override
    public SystemUser build() {
        // since the factory knows that all the parts are needed it could throw
        // an exception. however, we will leave that to the constructor
        SystemUser user;
        if (createdOn != null) {
            user = new SystemUser(email,password, fullName, shortName, birthdate, vatID , roles, createdOn);
        } else {
            user = new SystemUser(email,password, fullName, shortName, birthdate, vatID, roles);
        }
        if (LOGGER.isDebugEnabled()) {
            final String roleLog = roles.roleTypes().toString();
            LOGGER.debug("Creating new user [{}] {} ({} {}) with roles {}", user, email, fullName, shortName, roleLog);
        }
        return user;
    }
}
