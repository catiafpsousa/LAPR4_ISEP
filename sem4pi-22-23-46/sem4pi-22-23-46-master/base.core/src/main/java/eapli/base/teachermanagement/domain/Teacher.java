package eapli.base.teachermanagement.domain;

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

import javax.persistence.*;

import eapli.base.infrastructure.authz.domain.model.VatID;
import eapli.base.strings.util.StringSplitter;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

/**
 * A Teacher User.
 * <p>
 * This class represents a Teacher user. It follows a DDD approach where User
 * is the root entity of the Base User Aggregate and all of its properties
 * are instances of value objects. A Teacher User references a System User
 * <p>
 * This approach may seem a little more complex than just having String or
 * native type attributes but provides for real semantic of the domain and
 * follows the Single Responsibility Pattern
 *
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */
@Entity
@Table(name = "teacher")
public class Teacher implements AggregateRoot<Acronym> {

    @Version
    private Long version;

    @EmbeddedId
    private Acronym acronym;

    /**
     * cascade = CascadeType.NONE as the systemUser is part of another aggregate
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "IDSYSTEMUSER")
    private SystemUser systemUser;


    public Teacher(final SystemUser user) {
        this.acronym = Acronym.valueOf(StringSplitter.getFirstPartOfEmail(user.identity().toString()));
        this.systemUser = user;
        if (user == null || acronym == null) {
            throw new IllegalArgumentException();
        }
    }

    protected Teacher() {
        // for ORM only
    }

    public SystemUser user() {
        return this.systemUser;
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
        return DomainEntities.areEqual(this, other);
    }

    @Override
    public Acronym identity() {
        return this.acronym;
    }


    @Override
    public String toString() {
        return "Teacher - " +
                ", name ='" + systemUser.shortName() +
                ", acronym =" + acronym +
                 " + systemUser.identity()" + systemUser +
                '}';
    }
}

