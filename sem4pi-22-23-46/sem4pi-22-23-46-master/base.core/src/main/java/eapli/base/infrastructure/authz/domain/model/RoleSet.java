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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import eapli.framework.util.HashCoder;
import eapli.framework.validations.Preconditions;

/**
 * A set of roles. Part of the {@link SystemUser} aggregate.
 *
 * @author Paulo Gandra Sousa
 */
@Entity
/* package */ class RoleSet implements Set<RoleAssignment>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue // (strategy = GenerationType.IDENTITY)
    private Long pk;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final Set<RoleAssignment> assignments = new HashSet<>();

    @Override
    public boolean add(final RoleAssignment arg0) {
        Preconditions.nonNull(arg0);

        // TODO validations are missing, e.g., no overlap in roles with the same
        // role type
        return assignments.add(arg0);
    }

    /**
     * Checks if a user currently has an assignment of a certain role.
     *
     * @param r
     * @return true if a user currently has an assignment of a certain role
     */
    public boolean hasAssignment(final Role r) {
        for (final RoleAssignment assignment : assignments) {
            if (!assignment.isExpired() && assignment.isOf(r)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the user has or had in the past an assignment to a certain
     * role.
     *
     * @param r
     * @return true if the user has or had in the past an assignment to a
     *         certain role
     */
    public boolean hasOrHadAssignment(final Role r) {
        for (final RoleAssignment assignment : assignments) {
            if (assignment.isOf(r)) {
                return true;
            }
        }
        return false;
    }

    public Collection<Role> roleTypes() {
        final List<Role> ret = new ArrayList<>();
        assignments.forEach(role -> ret.add(role.type()));
        return ret;
    }

    /**
     * Returns the first non-expired assignment to a role or an empty optional
     * if no assignment to that role exists.
     *
     * <p>
     * The implementation uses the java Stream API, but is conceptually
     * equivalent to the following more traditional java code:
     *
     * <pre>
     * <code>
     * for (final RoleAssignment assignment : data) {
     *     if (assignment.isOf(role)) {
     *         return Optional.of(assignment);
     *     }
     * }
     * return Optional.empty();
     * </code>
     * </pre>
     *
     * @param role
     * @return the first non-expired assignment to a role or an empty optional
     */
    public Optional<RoleAssignment> getAssignment(final Role role) {
        return assignments.stream().filter(e -> !e.isExpired() && e.isOf(role)).findFirst();
    }

    @Override
    public boolean addAll(final Collection<? extends RoleAssignment> arg0) {
        return assignments.addAll(arg0);
    }

    @Override
    public void clear() {
        assignments.clear();
    }

    @Override
    public boolean contains(final Object arg0) {
        return assignments.contains(arg0);
    }

    @Override
    public boolean containsAll(final Collection<?> arg0) {

        return assignments.containsAll(arg0);
    }

    @Override
    public boolean isEmpty() {
        return assignments.isEmpty();
    }

    @Override
    public Iterator<RoleAssignment> iterator() {
        return assignments.iterator();
    }

    @Override
    public boolean remove(final Object arg0) {
        return assignments.remove(arg0);
    }

    @Override
    public boolean removeAll(final Collection<?> arg0) {
        return assignments.removeAll(arg0);
    }

    @Override
    public boolean retainAll(final Collection<?> arg0) {
        return assignments.retainAll(arg0);
    }

    @Override
    public int size() {
        return assignments.size();
    }

    @Override
    public Object[] toArray() {
        return assignments.toArray();
    }

    @Override
    public <T> T[] toArray(final T[] arg0) {
        return assignments.toArray(arg0);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleSet)) {
            return false;
        }

        final RoleSet roles = (RoleSet) o;

        // we need to perform a deep equals() as we want to compare values and
        // not object instances, so we cannot do this.data.equals(roles.data)
        for (final RoleAssignment r : assignments) {
            boolean found = false;
            for (final RoleAssignment or : roles.assignments) {
                if (r.equals(or)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return new HashCoder().with(assignments).code();
    }
}
