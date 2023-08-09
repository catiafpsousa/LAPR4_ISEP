package eapli.base.persistence.impl.jpa;

import eapli.base.Application;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.coursemanagement.domain.CourseState;
import eapli.base.coursemanagement.repositories.CourseRepository;

import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;


import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class JpaCourseRepository extends JpaAutoTxRepository<Course, CourseCode, CourseCode> implements CourseRepository {

    public JpaCourseRepository(TransactionalContext tx) {
        super(tx, "courseCode");
    }

    public JpaCourseRepository(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(),
                "courseCode");
    }

    @Override
    public Optional<Course> findById(CourseCode code) {
        TypedQuery<Course> query = super.createQuery(
                "SELECT e FROM Course e WHERE e.code =:code = '" + code + "'",
                Course.class);
        return Optional.ofNullable(query.getSingleResult());
    }

    public Optional<Course> ofIdentity(CourseCode code) {
        TypedQuery<Course> query = super.createQuery(
                "SELECT e FROM Course e WHERE e.code =:code",
                Course.class);
        query.setParameter("code", code);

        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Iterable<Course> findAllActiveCourses() {
        return null;
    }


    @Override
    public Iterable<Course> findByEnrollState(boolean b) {
        return null;
    }

    @Override
    public Iterable<Course> findByClosedState(boolean b) {
        if (!b) {
            String jpql = "SELECT c FROM Course c WHERE c.courseState <> :state";
            TypedQuery<Course> query = super.createQuery(jpql, Course.class);
            query.setParameter("state", CourseState.CLOSED);

            return query.getResultList();
        }
        String jpql = "SELECT c FROM Course c WHERE c.courseState = :state";
        TypedQuery<Course> query = super.createQuery(jpql, Course.class);
        query.setParameter("state", CourseState.CLOSED);

        return query.getResultList();
    }

    @Override
    public Iterable<Course> findByOpenedState() {
        String jpql = "SELECT c FROM Course c WHERE c.courseState = :state";
        TypedQuery<Course> query = super.createQuery(jpql, Course.class);
        query.setParameter("state", CourseState.OPEN);

        return query.getResultList();
    }

    @Override
    public Iterable<Course> findByEnrollState() {
        String jpql = "SELECT c FROM Course c WHERE c.courseState = :state";
        TypedQuery<Course> query = super.createQuery(jpql, Course.class);
        query.setParameter("state", CourseState.ENROLL);

        return query.getResultList();
    }


    @Override
    public void deleteOfIdentity(CourseCode entityId) {

    }


    public Iterable<Course> findAll() {
        return super.findAll();
    }

    @Override
    public Iterable<Course> findAll(boolean b) {
        return null;
    }

    public Iterable<Course> findBySystemUser(final SystemUser systemUser) {
        final TypedQuery<Course> query = entityManager().createQuery("SELECT DISTINCT c FROM Course c LEFT JOIN c.courseTeachers ct" + " WHERE c.teacherInCharge.systemUser =:systemUser OR ct.systemUser = :systemUser ", Course.class);
        query.setParameter("systemUser", systemUser);

        return query.getResultList();
    }


    public Boolean checkCourseCode(CourseCode courseCode) {
        final TypedQuery<Boolean> query = entityManager().createQuery("SELECT count(c) > 0 FROM Course c " + "WHERE c.code =: courseCode", Boolean.class);
        query.setParameter("courseCode", courseCode);
        return query.getSingleResult();
    }

}
