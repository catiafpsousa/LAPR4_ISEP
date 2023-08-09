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
package eapli.base.infrastructure.authz.impl.jpa;

import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

import eapli.base.infrastructure.authz.domain.repositories.UserRepository;

import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;

import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The JPA implementation for scenarios where the application is not running in
 * a container.
 *
 * @author Paulo Gandra de Sousa.
 */

public class JpaAutoTxUserRepository extends JpaAutoTxRepository<SystemUser, EmailAddress, EmailAddress>
        implements UserRepository {

    public JpaAutoTxUserRepository(final TransactionalContext autoTx) {
        super(autoTx, "email");
    }

    public JpaAutoTxUserRepository(final String puname,
            @SuppressWarnings({ "rawtypes", "java:S3740" }) final Map properties) {
        super(puname, properties, "email");
    }


    @Override
    public Iterable<SystemUser> findByActive(final boolean active) {
        return match(UserRepositoryConstants.FIND_BY_ACTIVE, "active", active);
    }

    @Override
    public Iterable<SystemUser> findAllUsers() {
        return null;
    }

    @Override
    public boolean findByEmailAndPassword(EmailAddress email) {
        String whereClause = "email = :email";
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);

        Optional<SystemUser> user = matchOne(whereClause, params);
        return user.isPresent();
    }


}
