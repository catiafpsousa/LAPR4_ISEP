package eapli.base.classmanagement.domain;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.teachermanagement.domain.Teacher;
import validations.util.Preconditions;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */
@Entity
@Table (name="extraclass")
public class ExtraordinaryClass extends Class {

    @ManyToMany
    protected List<Student> participantList = new ArrayList<>();

    protected ExtraordinaryClass(){
        //ORM
    }

    public ExtraordinaryClass(ClassTitle title, ClassDuration duration, Calendar day, LocalTime time, Teacher teacher, List<Student> participantList, Course course) {
        super(title, duration, day, time, teacher, course);
        Preconditions.nonEmpty(participantList);
        this.participantList = participantList;
    }


    protected List<Student> participantlist(){
        return this.participantList;
    }


    @Override
    public Long identity() {
        return this.id;
    }


    @Override
    public boolean hasIdentity(Long id) {
        return super.hasIdentity(id);
    }

    @Override
    public int compareTo(Long other) {
        return super.compareTo(other);
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(day().getTime());
        return String.format(String.format("Extraordinary Class ID: " +identity()+"\nTitle:" + title +"\nCourse: "+course().courseName()+ "\nTeacher:"+teacher.identity()+"\nDate: " + formattedDate +"\nTime: "
                + timestart().getHour() + ":"+ timestart().getMinute() + "\nDuration: "+duration()+" minutes\n"));
    }
}
