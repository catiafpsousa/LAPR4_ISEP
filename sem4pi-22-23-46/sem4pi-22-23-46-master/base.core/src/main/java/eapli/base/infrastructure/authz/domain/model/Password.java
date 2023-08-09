/*
 * Copyright (c) 2013-2022 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eapli.base.infrastructure.authz.domain.model;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.springframework.security.crypto.password.PasswordEncoder;

import eapli.framework.domain.model.ValueObject;
import eapli.base.infrastructure.authz.application.PasswordPolicy;
import eapli.framework.validations.Preconditions;
import lombok.EqualsAndHashCode;

/**
 * A password of a user. Password policy and encoding is done outside of this
 * class by {@link SystemUserBuilder} and {@link PasswordEncoder}.
 * <p>
 * <b>IMPORTANT</b>: passwords should never be stored in plain format
 *
 * @author Paulo Gandra Sousa
 */
@Embeddable
@EqualsAndHashCode
public final class Password implements ValueObject, Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "password")
    private final String value;

    protected Password() {
        // for ORM only
        value = null;
    }

    /* package */ Password(final String password) {
        Preconditions.nonNull(password);
        value = password;
    }

    /**
     * Constructs an encoded password ensuring the raw password adheres to the
     * password policy.
     *
     * @param rawPassword
     * @param policy
     * @param encoder
     *
     * @return the new Password or an empty Optional if the passwords does not
     *         satisfies the password policy
     */
    public static Optional<Password> encodedAndValid(final String rawPassword,
            final PasswordPolicy policy,
            final PasswordEncoder encoder) {
        Preconditions.noneNull(rawPassword, policy, encoder);

        if (policy.isSatisfiedBy(rawPassword)) {
            return Optional.of(new Password(encoder.encode(rawPassword)));
        }
        return Optional.empty();
    }

    /**
     * Let's not return the password by mistake.
     */
    @Override
    public String toString() {
        return "************";
    }

    /**
     * For supporting Spring Security UserDetails
     */
    /* package */String value() {
        return value;
    }
}
