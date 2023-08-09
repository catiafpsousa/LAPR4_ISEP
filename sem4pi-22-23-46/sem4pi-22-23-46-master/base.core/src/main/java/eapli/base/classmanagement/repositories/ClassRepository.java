package eapli.base.classmanagement.repositories;

import eapli.base.classmanagement.domain.Class;
import eapli.base.classmanagement.domain.RecurringClass;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.framework.domain.repositories.DomainRepository;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Joana Nogueira 1202924@isep.ipp.pt
 */
public interface ClassRepository extends DomainRepository<Long, Class> {

    Iterable<Class> findByTeacher(Teacher teacher);

    Iterable<Class> findByStudent(Student student);

    Iterable <Class> findByCourse(Course course);

    Iterable<RecurringClass> findRecurringClassesOfTeacher(Teacher teacher);

    Boolean checkScheduleAvailability (Teacher teacher, Calendar date, LocalTime startTime, LocalTime endTime);

    Boolean findByDateAndSystemUser(SystemUser systemUser, Calendar date, LocalTime time);

    Boolean findByDateAndSystemUserExtraClass(SystemUser systemUser, Calendar date, LocalTime time);

    Boolean findBycourseAndSystemUserinClasses(SystemUser systemUser,Course course, Calendar dayClass , LocalTime classTimeStart );

    List<Class> allClass(Calendar date, LocalTime time );
}