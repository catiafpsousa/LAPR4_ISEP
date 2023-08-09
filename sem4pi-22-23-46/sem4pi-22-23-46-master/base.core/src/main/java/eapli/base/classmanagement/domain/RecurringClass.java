package eapli.base.classmanagement.domain;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.teachermanagement.domain.Teacher;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name="recurringclasses")
public class RecurringClass extends Class{

    @ElementCollection
//    @MapKeyColumn (name="originalday")
    @Column(name="rescheduledclasses")
    protected Map<Calendar,RescheduledClassDate> rescheduledClasses;


    public RecurringClass(ClassTitle title, ClassDuration duration, Calendar day, LocalTime time, Teacher teacher, Course course) {
        super(title, duration, day, time, teacher, course);
        this.rescheduledClasses = new HashMap<>();
    }

    public RecurringClass() {

    }

    @Override
    public Long identity() {
        return this.id;
    }

    @Override
    public boolean hasIdentity(Long id) {
        return super.hasIdentity(id);
    }

    public void addReschedule(Calendar classDateToChange, Calendar newClassDate, LocalTime time, int duration) {
        RescheduledClassDate rcd = new RescheduledClassDate(newClassDate, time, time.plusMinutes(duration));
        rescheduledClasses.put(classDateToChange, rcd);
    }

    @Override
    public int compareTo(Long other) {
        return super.compareTo(other);
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(day().getTime());
        return String.format(String.format("Recurring Class ID: " +identity()+"\nTitle:"  + title +"\nCourse: "+course().courseName()+ "\nTeacher: "+teacher.identity()+"\nDate: " + formattedDate +"\nTime: "
                + timestart().getHour() + ":"+ timestart().getMinute() + "\nDuration: "+duration()+" minutes\n"));
    }
}
