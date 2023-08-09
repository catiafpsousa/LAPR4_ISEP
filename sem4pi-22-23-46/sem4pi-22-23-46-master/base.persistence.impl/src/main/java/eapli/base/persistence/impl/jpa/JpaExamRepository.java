package eapli.base.persistence.impl.jpa;

import eapli.base.Application;

import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.exammanagement.domain.*;
import eapli.base.exammanagement.repositories.ExamRepository;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.List;


public class JpaExamRepository extends JpaAutoTxRepository<Exam, ExamTitle, ExamTitle> implements ExamRepository {

    public JpaExamRepository(TransactionalContext tx) {
        super(tx, "examTitle");
    }

    public JpaExamRepository(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(),
                "examTitle");
    }

    public Iterable<Exam>findAllExam(){
        TypedQuery<Exam> query = entityManager().createQuery("SELECT e FROM Exam e WHERE e.examDates is not null ", Exam.class);
        return query.getResultList();
    }

    public Iterable<AutomaticFormativeExam>findAllAutomaticFormativeExam(){
        TypedQuery<AutomaticFormativeExam> query = entityManager().createQuery("SELECT e FROM AutomaticFormativeExam e ", AutomaticFormativeExam.class);
        return query.getResultList();
    }


    @Override
    public Iterable<Exam> findExamsByCourse(final CourseCode code) {
        final TypedQuery<Exam> query = entityManager().createQuery("SELECT e FROM Exam e WHERE e.courseCode = :course AND e.examDates != null", Exam.class);
        query.setParameter("course", code);
        return query.getResultList();
    }

    @Override
    public Iterable<Exam> findExamsByCourseAndDates(final CourseCode code, final Calendar calendar) {
        final TypedQuery<Exam> query = entityManager().createQuery("SELECT e FROM Exam e WHERE e.courseCode = :course AND :calendar BETWEEN e.examDates.openDate AND e.examDates.closeDate", Exam.class);
        query.setParameter("course", code);
        query.setParameter("calendar", calendar);
        return query.getResultList();
    }

    @Override
    public List<Exam> findExamsByCourses(List<CourseCode> courses) {
        TypedQuery<Exam> query = entityManager().createQuery("SELECT e FROM Exam e WHERE e.courseCode IN :courses AND e.examDates.openDate > current_date", Exam.class);
        query.setParameter("courses", courses);
        return query.getResultList();
    }

    @Override
    public Iterable<AutomaticFormativeExam> findFormativeExamsByCourse(CourseCode code) {
        TypedQuery<AutomaticFormativeExam> query = entityManager().createQuery("SELECT e FROM AutomaticFormativeExam e WHERE e.courseCode = :course", AutomaticFormativeExam.class);
        query.setParameter("course", code);
        return query.getResultList();
    }

    @Override
    public Iterable<Exam> findExamsByCourseAndDate(CourseCode course) {
        TypedQuery<Exam> query = entityManager().createQuery("SELECT e FROM Exam e WHERE e.courseCode = :course AND e.examDates.openDate > current_date", Exam.class);
        query.setParameter("course", course);
        return query.getResultList();
    }

    public void update(final ExamTitle examTitle, final CourseCode courseCode , final ExamTitle examTitleBefore){
        Query query =entityManager().createQuery("UPDATE AutomaticFormativeExam e SET examTitle= :examTitle  WHERE e.examTitle =:examTitleBefore ", AutomaticFormativeExam.class);

        query.setParameter("examTitle", examTitle);
        query.setParameter("examTitleBefore", examTitleBefore);
        query.executeUpdate();
    }
}
