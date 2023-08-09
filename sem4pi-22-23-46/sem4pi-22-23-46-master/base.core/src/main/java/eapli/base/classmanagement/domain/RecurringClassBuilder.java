package eapli.base.classmanagement.domain;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.framework.domain.model.DomainFactory;

import java.time.LocalTime;
import java.util.Calendar;

public class RecurringClassBuilder implements DomainFactory<RecurringClass> {

    private ClassTitle title;
    private ClassDuration duration;
    private Calendar day;
    private LocalTime time;
    private Teacher teacher;
    private Course course;

    public RecurringClassBuilder withTitle(final String title){
        return withTitle(ClassTitle.valueOf(title));
    }

    public RecurringClassBuilder withTitle(final ClassTitle title){
        this.title=title;
        return this;
    }

    public RecurringClassBuilder withTeacher (final Teacher teacher){
        this.teacher=teacher;
        return this;
    }

    public RecurringClassBuilder withCourse (final Course course){
        this.course=course;
        return this;
    }



    public RecurringClassBuilder withDuration(final int duration){
        return withDuration(ClassDuration.valueOf(duration));
    }

    public RecurringClassBuilder withDuration(final ClassDuration duration){
        this.duration=duration;
        return this;
    }

    public RecurringClassBuilder withDay(final Calendar day){
        this.day=day;
        return this;
    }

    public RecurringClassBuilder withTime(final LocalTime time){
        this.time=time;
        return this;
    }

    @Override
    public RecurringClass build() {
        return new RecurringClass(this.title,this.duration, this.day, this.time, this.teacher, this.course);
    }
}
