package eapli.base.persistence.impl.jpa;

import eapli.base.Application;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.coursemanagement.domain.CourseState;
import eapli.base.enrollmentmanagement.domain.Enrollment;
import eapli.base.enrollmentmanagement.domain.EnrollmentStatus;
import eapli.base.enrollmentmanagement.domain.EnrollmentToken;
import eapli.base.enrollmentmanagement.repositories.EnrollmentRepository;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.studentmanagement.domain.Student;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;

import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class JpaEnrollmentRepository extends JpaAutoTxRepository<Enrollment, Long, EnrollmentToken> implements EnrollmentRepository {


    public JpaEnrollmentRepository(TransactionalContext tx) {
        super(tx, "id");
    }

    public JpaEnrollmentRepository(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(),
                "id");
    }


    @Override
    public Optional<Enrollment> findById(EnrollmentToken id) {
        TypedQuery<Enrollment> query = super.createQuery(
                "SELECT c FROM Enrollment c WHERE c.id = '"+ id +"'",
                Enrollment.class);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Optional<Enrollment> ofIdentity(Enrollment enrollment) {
        TypedQuery<Enrollment> query = super.createQuery(
                "SELECT c FROM Enrollment c WHERE c.id = '"+ enrollment.identity() +"'",
                Enrollment.class);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Iterable<Enrollment> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<Enrollment> ofIdentity(EnrollmentToken id) {
        return Optional.empty();
    }

    @Override
    public void delete(Enrollment entity) {

    }

    @Override
    public void deleteOfIdentity(EnrollmentToken entityId) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public Iterable<Enrollment> findByRequestedStatus() {
        String jpql = "SELECT c FROM Enrollment c WHERE c.enrollmentStatus = :state";
        TypedQuery<Enrollment> query = super.createQuery(jpql, Enrollment.class);
        query.setParameter("state", EnrollmentStatus.REQUESTED);
        return query.getResultList();
    }

    @Override
    public Iterable<Enrollment> findByAcceptedStatus() {
        return null;
    }

    @Override
    public Iterable<Enrollment> findByRejectedStatus() {
        return null;
    }

    @Override
    public Iterable<Enrollment> findByCourse(Course course) {
        return null;
    }

    @Override
    public Iterable<Enrollment> findByStudent(final Student user) {
        final TypedQuery<Enrollment> query = entityManager().createQuery("SELECT c FROM Enrollment c  WHERE c.student = :student", Enrollment.class);
        query.setParameter("student", user);
        return query.getResultList();
        //return null;
    }

    public Iterable<Enrollment> findByStudentAndCourse(final Student student, final Course course){
        final TypedQuery<Enrollment> query = entityManager().createQuery("SELECT c FROM Enrollment c WHERE c.student = :student AND c.course = :course", Enrollment.class);
        query.setParameter("student", student);
        query.setParameter("course", course);
        return query.getResultList();
    }


    @Override
    public Iterable<Course> findByAcceptedStatusAndSystemUser(final SystemUser systemUser) {
        final TypedQuery<Course> query = entityManager().createQuery("SELECT distinct c.course FROM Enrollment c WHERE c.student.systemUser = :systemUser AND c.enrollmentStatus = :status AND c.course.courseState =:course1 OR c.course.courseState =:course2", Course.class);
        query.setParameter("systemUser", systemUser);
        query.setParameter("status", EnrollmentStatus.ACCEPTED);
        query.setParameter("course2", CourseState.IN_PROGRESS);
        query.setParameter("course1", CourseState.ENROLL);
        return query.getResultList();
    }

    @Override
    public Iterable<Student> findByCourseAndStatusAccepted(Course course) {
        final TypedQuery<Student> query = entityManager().createQuery("SELECT c.student FROM Enrollment c  WHERE c.course = :course AND c.enrollmentStatus = :status", Student.class);
        query.setParameter("course", course);
        query.setParameter("status", EnrollmentStatus.ACCEPTED);
        return query.getResultList();
    }
}
