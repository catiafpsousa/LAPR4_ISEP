package eapli.base.classmanagement.domain;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import validations.util.Preconditions;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Calendar;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */

@Entity
@Table (name = "classes")
@Inheritance (strategy = InheritanceType.JOINED)
public abstract class Class implements AggregateRoot<Long> {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column (name = "IDCLASS")
    protected Long id;

    @Version
    protected Long version;

    @Embedded
    @Column(unique = true)
    protected ClassTitle title;

    @Embedded
    protected ClassDuration duration;

    @Temporal(TemporalType.DATE)
    protected Calendar dayClass;

    @Column(name="weekday")
    protected int weekDay;

    @Column(name="classstart")
    protected LocalTime classTimeStart;

    @Column(name="classend")
    protected LocalTime classTimeEnd;


    @ManyToOne
    protected Teacher teacher;

    @ManyToOne
    protected Course course;

    protected Class(){
        //ORM
    }


    public Class(ClassTitle title, ClassDuration duration, Calendar day, LocalTime time, Teacher teacher, Course course) {
        Preconditions.isInTheFuture(day,"Class date must be in the future");
        Preconditions.noneNull(teacher);
        Preconditions.noneNull(time);
        Preconditions.noneNull(course);
        this.title = title;
        this.duration = duration;
        this.dayClass = day;
        this.weekDay = day().get(Calendar.DAY_OF_WEEK);
        this.classTimeStart = time;
        this.teacher = teacher;
        this.course=course;
        this.classTimeEnd = time.plusMinutes(duration.classDuration());
    }

    public ClassTitle title() {
        return title;
    }

    public ClassDuration duration() {
        return duration;
    }

    public Calendar day() {
        return dayClass;
    }

    public LocalTime timestart() {
        return classTimeStart;
    }

    public LocalTime timeend() {
        return classTimeEnd;
    }

    public Teacher teacher() {
        return teacher;
    }

    public Course course() {return course;}

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

}
