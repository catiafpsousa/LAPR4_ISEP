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
package eapli.base.infrastructure.authz.application;

import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.model.ValueObject;
import eapli.framework.time.util.CurrentTimeCalendars;
import eapli.framework.validations.Preconditions;
import lombok.Value;
import lombok.experimental.Accessors;

import java.util.Calendar;
import java.util.UUID;

/**
 * The session of an authenticated user.
 *
 * @author Paulo Gandra Sousa
 */
@Value
@Accessors(fluent = true)
public final class UserSession implements ValueObject {
    private static final long serialVersionUID = 1L;

    private final SystemUser authenticatedUser;
    private final UUID token;
    private final Calendar startedOn;

    /**
     * @param user
     */
    public UserSession(final SystemUser user) {
        Preconditions.nonNull(user, "user must not be null");

        authenticatedUser = user;
        token = UUID.randomUUID();
        startedOn = CurrentTimeCalendars.now();
    }

    @Override
    public String toString() {
        return authenticatedUser.identity() + "@" + token;
    }
}
