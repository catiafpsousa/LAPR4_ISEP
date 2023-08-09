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
import java.util.Calendar;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.time.util.CurrentTimeCalendars;
import eapli.framework.util.HashCoder;
import eapli.framework.validations.Preconditions;

/**
 * A role assignment of a user. Serves as history of roles(action rights) the
 * user has/had.
 * <p>
 * This an example of a Value Object in the Domain but mapped as a Table
 * (entity) in the relational model to overcome a limitation with Hibernate JPA
 * that does not support embeddedable dentro de embeddeables.
 *
 * @author Paulo Gandra Sousa
 */
@Entity
public class RoleAssignment implements ValueObject, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue // (strategy = GenerationType.IDENTITY)
    private Long pk;

    private final Role type;

    @Temporal(TemporalType.DATE)
    private final Calendar assignedOn;

    @Temporal(TemporalType.DATE)
    private Calendar unassignedOn;

    private boolean expired;

    /**
     * @param type
     */
    public RoleAssignment(final Role type) {
        this(type, CurrentTimeCalendars.now());
    }

    /**
     * @param type
     * @param assignedOn
     */
    public RoleAssignment(final Role type, final Calendar assignedOn) {
        Preconditions.noneNull(type, assignedOn);

        this.type = type;
        this.assignedOn = assignedOn;
        expired = false;
    }

    protected RoleAssignment() {
        // for ORM
        type = null;
        assignedOn = null;
    }

    /**
     * @return <code>true</code> if the role is expired
     */
    public boolean isExpired() {
        return expired;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleAssignment)) {
            return false;
        }

        final RoleAssignment other = (RoleAssignment) o;
        final boolean b = type == other.type && assignedOn.equals(other.assignedOn) && expired == other.expired;
        if (!b) {
            return false;
        }

        return ((unassignedOn != null && unassignedOn.equals(other.unassignedOn))
                || unassignedOn == null && other.unassignedOn == null);
    }

    @Override
    public int hashCode() {
        return new HashCoder().with(type).with(assignedOn).with(expired).with(unassignedOn).code();
    }

    @Override
    public String toString() {
        return type + "@" + assignedOn;
    }

    /**
     * @return the role
     */
    public Role type() {
        return type;
    }

    /**
     * @param r
     *
     * @return <code>true</code> if this assignment if of a certain {@link Role}
     */
    public boolean isOf(final Role r) {
        return type.equals(r);
    }

    /**
     * Unassigns this role, marking it as expired.
     *
     * @return false if the role was already expired.
     */
    public boolean unassign() {
        if (expired) {
            return false;
        }

        expired = true;
        unassignedOn = CurrentTimeCalendars.now();
        return true;
    }

    /**
     * @return the date the role was unassigned
     */
    public Optional<Calendar> unassignedOn() {
        return Optional.ofNullable(unassignedOn);
    }
}
