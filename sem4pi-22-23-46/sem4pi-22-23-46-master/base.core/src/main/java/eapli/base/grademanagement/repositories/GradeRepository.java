package eapli.base.grademanagement.repositories;

import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.grademanagement.domain.ExamGrade;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.repositories.DomainRepository;

public interface GradeRepository extends DomainRepository<Long, ExamGrade> {
    Iterable<ExamGrade> findAll();
    Iterable<ExamGrade> findByStudent(final SystemUser student);
    Iterable<ExamGrade> findByCourse( final CourseCode courseCode);

}
