package eapli.base.studentmanagement.domain;

import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;

import javax.persistence.*;


/**
 * A Student User.
 * <p>
 * This class represents a Student user. It follows a DDD approach where User
 * is the root entity of the Base User Aggregate and all of its properties
 * are instances of value objects. A Student User references a System User
 * <p>
 * This approach may seem a little more complex than just having String or
 * native type attributes but provides for real semantic of the domain and
 * follows the Single Responsibility Pattern
 *
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */
@Entity
@Table(name = "student")
public class Student implements AggregateRoot<MechanographicNumber> {

    private static final long serialVersionUID = 1L;

    @Column(name = "MECHANO", unique = true)
    @EmbeddedId
    private MechanographicNumber mechano;

    /**
     * cascade = CascadeType.NONE as the systemUser is part of another aggregate
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "IDSYSTEMUSER")
    private SystemUser systemUser;


    public Student(final SystemUser user, final long repoSize) {
        this.systemUser = user;
        this.mechano = new MechanographicNumber(user, repoSize);
        if (user == null || mechano == null) {
            throw new IllegalArgumentException();
        }
    }

    protected Student() {
        // for ORM only
    }


    public SystemUser user() {
        return this.systemUser;
    }

    public MechanographicNumber mechanographicNumber() {
        return this.mechano;
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
    public MechanographicNumber identity() {
        return this.mechano;
    }


    @Override
    public String toString() {
        return "Student{" +
                ", mechanographicNumber=" + mechano +
                ", systemUser=" + systemUser.identity() +
                '}';
    }
}

