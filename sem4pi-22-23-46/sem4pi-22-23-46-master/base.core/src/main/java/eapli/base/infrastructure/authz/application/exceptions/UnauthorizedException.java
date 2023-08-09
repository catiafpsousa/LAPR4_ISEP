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
package eapli.base.infrastructure.authz.application.exceptions;

import eapli.base.infrastructure.authz.domain.model.Role;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

/**
 * @author Paulo Gandra Sousa
 *
 */
public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = -5601684795034834561L;

    private final SystemUser user;
    private final Role[] roles;

    /**
     * @param message
     * @param user
     * @param roles
     */
    public UnauthorizedException(final String message, final SystemUser user, final Role... roles) {
        super(buildMessage(message, user, roles));
        this.roles = roles;
        this.user = user;
    }

    /**
     * @param user
     * @param roles
     */
    public UnauthorizedException(final SystemUser user, final Role... roles) {
        super(buildMessage("", user, roles));
        this.roles = roles;
        this.user = user;
    }

    private static String buildMessage(final String original, final SystemUser user, final Role... roles) {
        return "User " + user.identity() + " is not authorized to perform one of these actions: " + roles + "\n"
                + original;
    }

    /**
     *
     * @return the user trying to access the resource
     */
    public SystemUser user() {
        return user;
    }

    /**
     *
     * @return the roles that are needed to perform the action
     */
    public Role[] intendedRoles() {
        return roles;
    }
}
