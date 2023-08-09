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
package eapli.base.infrastructure.authz.application;

import eapli.base.infrastructure.authz.application.exceptions.UnauthenticatedException;
import eapli.base.infrastructure.authz.application.exceptions.UnauthorizedException;
import eapli.base.infrastructure.authz.domain.model.Role;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Very simple session holder and permission checking helper. To obtain a valid
 * session, use either Spring Security and the
 * {@link UserDetailsServiceImpl#loadUserByUsername(String) UserDetailService}
 * implementation or
 * {@link AuthenticationService#authenticate(String, String, Role...)} - you can
 * obtain an instance thru {@link AuthzRegistry#authorizationService()}.
 *
 * <p>
 * Permissions are not checked dynamically against a database but against the
 * roles assigned to a user.
 *
 * @author Paulo Gandra de Sousa
 *
 */
@Component
public class AuthorizationService {
    private static final Logger LOGGER = LogManager.getLogger(AuthorizationService.class);

    /**
     * The current logged in user session. {@code null} if there is no session.
     */
    private UserSession theSession = null;

    /**
     * Hiding the constructor so no direct creation of objects is possible.
     */
    protected AuthorizationService() {
        // empty
    }

    /**
     * Creates a session for a user. If the user is {@code null}, it deletes the current
     * session.
     *
     * @param user
     * @return
     */
    /* package */ Optional<UserSession> createSessionForUser(final SystemUser user) {
        if (user != null) {
            theSession = new UserSession(user);
        } else {
            clearSession();
        }
        return session();
    }

    /**
     * Clear the session.
     */
    public Optional<UserSession> clearSession() {
        theSession = null;
        return session();
    }

    /**
     * checks if there is an authenticated user session
     *
     * @return {@code true} if there is an authenticated user session
     */
    public boolean hasSession() {
        return theSession != null;
    }

    /**
     * Returns the current session or an empty optional if there is no session.
     *
     * @return the current session or an empty optional if there is no session.
     */
    public Optional<UserSession> session() {
        return Optional.ofNullable(theSession);
    }

    /**
     * Checks if the currently logged in user has the specified roles.
     *
     * @param actions
     * @return {@code true} if the authenticated user has at least one of the
     *         desired roles
     */
    public boolean isAuthenticatedUserAuthorizedTo(final Role... actions) {
        return session().map(us -> us.authenticatedUser().hasAny(actions)).orElse(false);
    }

    /**
     * Checks the permission of the currently logged in user and throws an
     * exception if there is no session or the currently logged in user does not
     * have the requested permissions.
     *
     * <p>
     * Consider using {@link #loggedinUserWithPermissions(Role...)} to perform
     * this check without throwing exceptions, as these scenarios are not really
     * exceptions but something accounted for.
     *
     * @param actions
     * @throws UnauthenticatedException
     * @throws UnauthorizedException
     *
     */
    public void ensureAuthenticatedUserHasAnyOf(final Role... actions) {
        final UserSession us = session().orElseThrow(() -> {
            LOGGER.info("Unauthenticated access attempt");
            return new UnauthenticatedException();
        });
        if (!us.authenticatedUser().hasAny(actions)) {
            LOGGER.info("Unauthorized access attempt by user {}", us.authenticatedUser().identity());
            throw new UnauthorizedException(us.authenticatedUser(), actions);
        }
    }

    /**
     * Returns the authenticated user if there is a session and the
     * authenticated user has the desired permissions. otherwise returns an
     * empty optional.
     *
     * @param actions
     * @return the authenticated user if there is a session and the
     *         authenticated user has the desired permissions. otherwise returns
     *         an empty optional
     */
    public Optional<SystemUser> loggedinUserWithPermissions(final Role... actions) {
        return session().filter(us -> us.authenticatedUser().hasAny(actions)).map(UserSession::authenticatedUser);
    }
}
