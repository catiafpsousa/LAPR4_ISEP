package eapli.base.classmanagement.domain;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.framework.domain.model.DomainFactory;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

public class ExtraClassBuilder implements DomainFactory<ExtraordinaryClass> {
    private ClassTitle title;
    private  ClassDuration duration;
    private Calendar day;
    private LocalTime time;
    private List<Student> participantList;
    private Teacher teacher;
    private Course course;

    public ExtraClassBuilder withTitle(final String title){
        return withTitle(ClassTitle.valueOf(title));
    }

    public ExtraClassBuilder withTitle(final ClassTitle title){
        this.title=title;
        return this;
    }

    public ExtraClassBuilder withTeacher (final Teacher teacher){
        this.teacher=teacher;
        return this;
    }

    public ExtraClassBuilder withCourse (final Course course){
        this.course=course;
        return this;
    }



    public ExtraClassBuilder withDuration(final int duration){
        return withDuration(ClassDuration.valueOf(duration));
    }

    public ExtraClassBuilder withDuration(final ClassDuration duration){
        this.duration=duration;
        return this;
    }

    public ExtraClassBuilder withDay(final Calendar day){
        this.day=day;
        return this;
    }

    public ExtraClassBuilder withTime(final LocalTime time){
        this.time=time;
        return this;
    }

    public ExtraClassBuilder withParticipants(final List <Student> students){
        this.participantList=students;
        return this;
    }


    @Override
    public ExtraordinaryClass build() {
        return new ExtraordinaryClass(this.title,this.duration, this.day, this.time, this.teacher, this.participantList, this.course);
    }
}
