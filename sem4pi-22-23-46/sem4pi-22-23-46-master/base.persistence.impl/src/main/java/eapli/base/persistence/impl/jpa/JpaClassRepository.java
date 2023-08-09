package eapli.base.persistence.impl.jpa;

import eapli.base.Application;
import eapli.base.classmanagement.domain.Class;
import eapli.base.classmanagement.domain.RecurringClass;
import eapli.base.classmanagement.repositories.ClassRepository;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;

import javax.persistence.TypedQuery;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

public class JpaClassRepository extends JpaAutoTxRepository <Class, Long, Long> implements ClassRepository {
    public JpaClassRepository(TransactionalContext tx) {
        super(tx, "id");
    }

    public JpaClassRepository(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(),
                "id");
    }

    @Override
    public Iterable<RecurringClass> findRecurringClassesOfTeacher(Teacher teacher) {
        final TypedQuery<RecurringClass> query = entityManager().createQuery("SELECT c FROM RecurringClass c WHERE c.teacher = :teacher" , RecurringClass.class);
        query.setParameter("teacher", teacher);
        return query.getResultList();
    }

    @Override
    public Iterable<Class> findByTeacher(Teacher teacher) {
        return null;
    }

    @Override
    public Iterable<Class> findByStudent(Student student) {
        return null;
    }

    @Override
    public Iterable<Class> findByCourse(Course course) {
        return null;
    }

    /**
     *
     *
     * @param teacher
     * @param date
     * @param startTime
     * @param endTime
     * @return returns True if the class can be scheduled and False otherwise
     */
    @Override
    public Boolean checkScheduleAvailability(Teacher teacher, Calendar date, LocalTime startTime, LocalTime endTime) {

        TypedQuery<Boolean> query_EC = entityManager().createQuery("SELECT COUNT(ec) > 0 FROM ExtraordinaryClass ec WHERE ec.teacher = :teacher AND ec.dayClass = :date AND ((:startTime BETWEEN ec.classTimeStart AND ec.classTimeEnd) OR (:endTime BETWEEN ec.classTimeStart AND ec.classTimeEnd))", Boolean.class);
        query_EC.setParameter("teacher", teacher);
        query_EC.setParameter("date", date);
        query_EC.setParameter("startTime", startTime);
        query_EC.setParameter("endTime", endTime);

        if (!(query_EC.getSingleResult())) {

            TypedQuery<Boolean> query_RC = entityManager().createQuery("SELECT COUNT(c) > 0 FROM RecurringClass c WHERE c.teacher = :teacher AND c.weekDay = :weekday AND ((:startTime BETWEEN c.classTimeStart AND c.classTimeEnd) OR (:endTime BETWEEN c.classTimeStart AND c.classTimeEnd))", Boolean.class);
            query_RC.setParameter("teacher", teacher);
            query_RC.setParameter("weekday", date.get(Calendar.DAY_OF_WEEK));
            query_RC.setParameter("startTime", startTime);
            query_RC.setParameter("endTime", endTime);
            return !(query_RC.getSingleResult());

        } else {
            return false;
        }

    }


    public Boolean findByDateAndSystemUser(SystemUser systemUser, Calendar dayClass , LocalTime classTimeStart ) {
        final TypedQuery<Boolean> query = entityManager().createQuery("SELECT count(c) > 0 FROM Class c "+" WHERE c.teacher.systemUser = :systemUser AND c.dayClass =: dayClass AND :classTimeStart BETWEEN c.classTimeStart AND c.classTimeEnd ", Boolean.class);
        query.setParameter("dayClass", dayClass);
        query.setParameter("systemUser", systemUser);
        query.setParameter("classTimeStart", classTimeStart);
        return query.getSingleResult();
    }

    public Boolean findBycourseAndSystemUserinClasses( SystemUser systemUser,Course course, Calendar dayClass , LocalTime classTimeStart ) {
        final TypedQuery<Boolean> query = entityManager().createQuery("SELECT count(c) > 0 FROM Class c "+" WHERE c.teacher.systemUser = :systemUser AND c.course = :course AND c.dayClass =: dayClass  AND :classTimeStart BETWEEN c.classTimeStart AND c.classTimeEnd", Boolean.class);
        query.setParameter("dayClass", dayClass);
        query.setParameter("systemUser", systemUser);
        query.setParameter("classTimeStart", classTimeStart);
        query.setParameter("course", course);

        return query.getSingleResult();
    }



    public Boolean findByDateAndSystemUserExtraClass(SystemUser systemUser, Calendar dayClass , LocalTime classTimeStart ) {
        final TypedQuery<Boolean> query = entityManager().createQuery("SELECT count(c) > 0 FROM ExtraordinaryClass c "+"JOIN c.participantList pl"+" WHERE  c.teacher.systemUser = :systemUser OR pl.systemUser = :systemUser1 AND c.dayClass =: dayClass AND :classTimeStart BETWEEN c.classTimeStart AND c.classTimeEnd", Boolean.class);
        query.setParameter("dayClass", dayClass);
        query.setParameter("systemUser", systemUser);
        query.setParameter("systemUser1", systemUser);
        query.setParameter("classTimeStart", classTimeStart);
        return query.getSingleResult();
    }

    public List<Class> allClass(Calendar dayClass , LocalTime classTimeStart){
        final TypedQuery<Class> query = entityManager().createQuery( "SELECT m FROM Class m where m.dayClass = :dayClass AND :classTimeStart BETWEEN m.classTimeStart AND m.classTimeEnd ", Class.class);
        query.setParameter("dayClass", dayClass);
        query.setParameter("classTimeStart", classTimeStart);
        return query.getResultList();
    }

}
