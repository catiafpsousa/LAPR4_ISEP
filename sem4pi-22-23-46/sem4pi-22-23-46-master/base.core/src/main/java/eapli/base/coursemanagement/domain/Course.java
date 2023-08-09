package eapli.base.coursemanagement.domain;

import eapli.base.teachermanagement.domain.Teacher;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * A Course.
 *
 * This class represents a course. It follows a DDD approach where CourseCode
 * is the root entity of the Course Aggregate and all of its properties
 * are instances of value objects.
 *
 * This approach provides for real semantic of the domain and
 * follows the Single Responsibility Pattern
 *
 * @author Pedro Sousa 1201326@isep.ipp.pt
 *
 */


@Entity
@Table(name = "course")
public class Course implements AggregateRoot<CourseCode> {

    private static final long serialVersionUID = 1L;

    @Version
    private Long version;

    @Column (name = "code", unique = true)
    @EmbeddedId
    private CourseCode code;

    @Embedded
    private CourseName name;

    @Embedded
    private CourseDescription description;

    @Enumerated
    private CourseState courseState;

    @ManyToMany
    private List<Teacher> courseTeachers;

    @ManyToOne
    private Teacher teacherInCharge;

    @Embedded
    private CourseStudentsLimit limits;

    @Temporal(TemporalType.DATE)
    private Calendar modifiedOn;

    protected Course(){
    }

    public Course(CourseCode code, CourseName name, CourseDescription description, CourseStudentsLimit limits) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.courseState = CourseState.CLOSED;
        this.limits = limits;
        this.courseTeachers = new ArrayList<>();
    }




    public Course(String code, String name, String description, int minLimit, int maxLimit, Calendar created) {
        this.code = CourseCode.valueOf(code);
        this.name = CourseName.valueOf(name);
        this.description = CourseDescription.valueOf(description);
        this.courseState = CourseState.CLOSED;
        this.limits = new CourseStudentsLimit(minLimit, maxLimit);
        this.modifiedOn = created;
        new Course(this.code, this.name, this.description, this.limits);
    }

    public void open(Calendar now) {
        courseState = CourseState.OPEN;
        this.modifiedOn = now;
    }

    public void enroll(Calendar now) {
        courseState = CourseState.ENROLL;
        this.modifiedOn = now;
    }

    public void inProgress(Calendar now) {
        courseState = CourseState.IN_PROGRESS;
        this.modifiedOn = now;
    }

    public void close(Calendar now){
        courseState = CourseState.CLOSED;
        this.modifiedOn = now;
    }

    public CourseName courseName(){
        return this.name;
    }

    public CourseCode courseCode(){
        return this.code;
    }

    public CourseDescription description(){
        return this.description;
    }

    public CourseState courseState(){
        return this.courseState;
    }

    public List<Teacher> courseTeachers(){
        return this.courseTeachers;
    }

    public Optional<Teacher> teacherInCharge(){
        return Optional.ofNullable(this.teacherInCharge);
    }

    public void setCourseTeachers(List<Teacher> courseTeachers) {
        this.courseTeachers = courseTeachers;
    }

    public void setTeacherInCharge(Teacher toTeacherInCharge) {
        this.teacherInCharge = toTeacherInCharge;
    }

    @Override
    public int compareTo(CourseCode other) {
        return AggregateRoot.super.compareTo(other);
    }

    @Override
    public CourseCode identity() {
        return this.code;
    }

    @Override
    public boolean hasIdentity(CourseCode id) {
        return AggregateRoot.super.hasIdentity(id);
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
    public String toString() {
        return "Course{" +
                "code=" + code +
                ", name=" + name +
                ", description=" + description +
                ", courseState=" + courseState +
                '}';
    }
}
