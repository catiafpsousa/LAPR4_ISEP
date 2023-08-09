package eapli.base.persistence.impl.jpa;

import eapli.base.Application;
import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.grademanagement.domain.ExamGrade;
import eapli.base.grademanagement.repositories.GradeRepository;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;

import javax.persistence.TypedQuery;

public class JpaGradeRepository extends JpaAutoTxRepository<ExamGrade, Long, Long> implements GradeRepository {

    public JpaGradeRepository(TransactionalContext tx) {
        super(tx, "gradeID");
    }

    public JpaGradeRepository(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(),
                "gradeID");
    }


    public Iterable<ExamGrade> findByStudent(final SystemUser student) {
        TypedQuery<ExamGrade> query = entityManager().createQuery("SELECT e FROM ExamGrade e WHERE e.student =:student", ExamGrade.class);
       query.setParameter("student", student);
        return query.getResultList();
    }

    @Override
    public Iterable<ExamGrade> findByCourse(final CourseCode courseCode){
        TypedQuery<ExamGrade> query = entityManager().createQuery("SELECT e FROM ExamGrade e join  e.exam ex WHERE ex.courseCode = :courseCode ", ExamGrade.class);
        query.setParameter("courseCode",courseCode);
        return query.getResultList();
    }
}
