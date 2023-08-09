package eapli.base.exammanagement.repositories;

import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.exammanagement.domain.*;
import eapli.framework.domain.repositories.DomainRepository;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public interface ExamRepository extends DomainRepository<ExamTitle, Exam> {

    Iterable<Exam> findAll();

    Optional<Exam> ofIdentity(ExamTitle examTitle);

    Iterable<Exam> findExamsByCourse(CourseCode code);

    Iterable<Exam> findExamsByCourseAndDate(CourseCode code);

    Iterable<Exam> findExamsByCourseAndDates(CourseCode code, Calendar calendar);

    List<Exam> findExamsByCourses(List<CourseCode> courses);

    Iterable<AutomaticFormativeExam> findFormativeExamsByCourse(CourseCode code);

    Iterable<Exam> findAllExam();

    Iterable<AutomaticFormativeExam> findAllAutomaticFormativeExam();

    void update(final ExamTitle examTitle, final CourseCode courseCode , final ExamTitle examTitleBefore);
}
