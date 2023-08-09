package eapli.base.enrollmentmanagement.domain;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.studentmanagement.domain.Student;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.validations.Preconditions;

import javax.persistence.*;
import java.util.Calendar;


/**
 * An Enrollment.
 *
 * This class represents an enrollment. It follows a DDD approach where Enrollment
 * is the root entity of the Enrollment Aggregate and all of its properties
 * are instances of value objects.
 *
 * This approach provides for real semantic of the domain and
 * follows the Single Responsibility Pattern
 *
 * @author Catia Sousa
 */

@Entity
@Table(name = "enrollment")
public class Enrollment implements AggregateRoot<EnrollmentToken> {

    private static final long serialVersionUID = 1L;
    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    private EnrollmentToken enrollmentToken;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Course course;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus enrollmentStatus;

    @Temporal(TemporalType.DATE)
    private Calendar modifiedOn;


    protected Enrollment() {
        // for ORM only

    }

    public Enrollment(final Student student, final Course course, final EnrollmentStatus enrollmentStatus, final Calendar createdOn){
        Preconditions.noneNull(student, course);
        this.student = student;
        this.course = course;
        this.enrollmentStatus = enrollmentStatus;
        this.modifiedOn = createdOn;
        this.enrollmentToken = EnrollmentToken.newToken();
    }


    public void requested(Calendar now){
        enrollmentStatus = EnrollmentStatus.REQUESTED;
        this.modifiedOn = now;
    }

    public void accepted(Calendar now){
        enrollmentStatus = EnrollmentStatus.ACCEPTED;
        this.modifiedOn = now;
    }

    public void rejected(Calendar now){
        enrollmentStatus = EnrollmentStatus.REJECTED;
        this.modifiedOn = now;
    }

    public boolean isRequested(){
        return enrollmentStatus.equals(EnrollmentStatus.REQUESTED);
    }

    public boolean isAccepted(){
        return enrollmentStatus.equals(EnrollmentStatus.ACCEPTED);
    }

    public boolean isRejected(){
        return enrollmentStatus.equals(EnrollmentStatus.REJECTED);
    }

    public Student student() {
        return this.student;
    }

    public Course course() {
        return this.course;
    }

    public EnrollmentStatus enrollmentStatus() {
        return this.enrollmentStatus;
    }

    public Long version() {
        return this.version;
    }

    public Long getId() {
        return id;
    }

    public Calendar modifiedOn() {
        return this.modifiedOn;
    }

    public EnrollmentToken identity() {
        return enrollmentToken;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(final Object o) {
        return DomainEntities.areEqual(this, o);
    }

    @Override
    public boolean sameAs(Object other) {
        return DomainEntities.areEqual(this, other);
    }

    @Override
    public int compareTo(EnrollmentToken other) {
        return AggregateRoot.super.compareTo(other);
    }

    @Override
    public boolean hasIdentity(EnrollmentToken id) {
        return AggregateRoot.super.hasIdentity(id);
    }



}
