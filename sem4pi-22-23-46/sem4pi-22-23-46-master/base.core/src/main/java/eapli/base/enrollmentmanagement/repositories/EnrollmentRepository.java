package eapli.base.enrollmentmanagement.repositories;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.enrollmentmanagement.domain.Enrollment;
import eapli.base.enrollmentmanagement.domain.EnrollmentToken;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.studentmanagement.domain.Student;
import eapli.framework.domain.repositories.DomainRepository;

import java.util.List;
import java.util.Optional;


public interface EnrollmentRepository extends DomainRepository<EnrollmentToken,Enrollment > {

    Optional<Enrollment> ofIdentity(Enrollment enrollment);

    Optional<Enrollment> findById(EnrollmentToken id);

    Iterable<Enrollment> findAll();

    Iterable<Enrollment> findByRequestedStatus();

    Iterable<Enrollment> findByAcceptedStatus();

    Iterable<Enrollment> findByRejectedStatus();

    Iterable<Enrollment> findByCourse(Course course);

    Iterable<Enrollment> findByStudent(Student student);

    Iterable<Enrollment> findByStudentAndCourse(Student student, Course course);


    Iterable<Course> findByAcceptedStatusAndSystemUser(SystemUser systemUser);

    Iterable <Student> findByCourseAndStatusAccepted(Course course);



}


