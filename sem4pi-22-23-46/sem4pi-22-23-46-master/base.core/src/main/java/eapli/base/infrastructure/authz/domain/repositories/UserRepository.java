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
package eapli.base.infrastructure.authz.domain.repositories;

import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.base.infrastructure.authz.domain.model.Password;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.repositories.DomainRepository;

import java.util.Optional;


/**
 * User repository.
 *
 * @author nuno on 21/03/16.
 */
public interface UserRepository extends DomainRepository<EmailAddress, SystemUser> {

    /**
     * Returns the currently active/inactive users.
     *
     * @param active
     *            the status we want to look for
     *
     * @return the currently active/inactive users
     */
    Iterable<SystemUser> findByActive(boolean active);

    /**
     * returns the user  with the given email
     *
     * @param email
     * @return the user  with the given email
     */
    Optional<SystemUser> ofIdentity(EmailAddress email);

    /**
     * Finds all users
     *
     * @return an iterable with all  users
     */
    Iterable<SystemUser> findAllUsers();

    boolean findByEmailAndPassword(EmailAddress email);

}
