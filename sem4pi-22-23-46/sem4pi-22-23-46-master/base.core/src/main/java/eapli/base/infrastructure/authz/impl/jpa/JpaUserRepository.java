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
import eapli.base.infrastructure.authz.domain.model.Password;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

import eapli.base.infrastructure.authz.domain.repositories.UserRepository;

import eapli.framework.infrastructure.repositories.impl.jpa.JpaBaseRepository;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.stereotype.Component;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * The JPA implementation for scenarios running in a container.
 *
 * @author nuno on 20/03/16.
 */
@Component
public class JpaUserRepository extends JpaBaseRepository<SystemUser, EmailAddress, EmailAddress> implements UserRepository {

    public JpaUserRepository() {
        super("email");
    }

    @Override
    public Iterable<SystemUser> findByActive(final boolean active) {
        return match(UserRepositoryConstants.FIND_BY_ACTIVE, "active", active);
    }

    @Override
    public Optional<SystemUser> ofIdentity(EmailAddress email) {
        TypedQuery<SystemUser> query = super.createQuery(
                "SELECT u FROM SystemUser u WHERE u.emailAddress = '"+ email+"'",
                SystemUser.class);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Iterable<SystemUser> findAllUsers() {
        TypedQuery<SystemUser> query = super.createQuery(
                "SELECT * FROM SystemUser ",
                SystemUser.class);
        return query.getResultList();
    }

    @Override
    public boolean findByEmailAndPassword(EmailAddress email) {
        final TypedQuery<Boolean> query = entityManager().createQuery("SELECT count(c) > 0 FROM SystemUser c where c.id = :email", Boolean.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }
}
