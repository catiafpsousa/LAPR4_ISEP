package eapli.base.enrollmentmanagement.application;

import eapli.base.coursemanagement.application.CourseManagementService;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.coursemanagement.domain.CourseState;
import eapli.base.enrollmentmanagement.domain.Enrollment;
import eapli.base.enrollmentmanagement.domain.EnrollmentStatus;
import eapli.base.enrollmentmanagement.repositories.EnrollmentRepository;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.studentmanagement.application.StudentManagementService;
import eapli.base.studentmanagement.domain.MechanographicNumber;
import eapli.base.studentmanagement.domain.Student;
import eapli.framework.time.util.CurrentTimeCalendars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * Enrollment Management Service. Provides the typical application use cases for
 * managing {@link Enrollment}, e.g., adding, deactivating, listing, searching.
 *
 * @author Cátia Sousa / Pedro Garrido / Pedro Sousa
 */


@Component
public class EnrollmentManagementService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentManagementService studentSvc = AuthzRegistry.studentService();


    @Autowired
    public EnrollmentManagementService(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public Enrollment registerNewEnrollment(SystemUser user, Course course, EnrollmentStatus status, Calendar createdOn) {
        // enrollment.activate(CurrentTimeCalendars.now());
        Student student = studentSvc.studentOfUser(user.identity()).get();
        Iterable<Enrollment> enrollments = enrollmentRepository.findAll();
        for (Enrollment e : enrollments) {
            if (e.student().equals(student) && e.course().equals(course)) {
                try {
                    throw new IllegalArgumentException();
                } catch (IllegalArgumentException ex) {
                    System.out.println("You already request enrollment for this course!");
                    return null;
                }
            }
        }
        System.out.println("Enrollment requested successfully ");
        return enrollmentRepository.save(new Enrollment(student, course, status, createdOn));
    }

    public void createEnrollmentfromParameters(String[] userInfo, int index, List<Course>allCourses) {
        MechanographicNumber mechano;
        CourseCode courseCode;
        mechano = MechanographicNumber.valueOf(userInfo[0]);
        courseCode = CourseCode.valueOf(userInfo[1]);
        Optional<SystemUser> studenttoEnroll = studentSvc.userStudentOfIdentity(mechano);
        Course courseToEnroll = null;
        for (Course c : allCourses) {
            if (c.courseCode().equals(courseCode)) courseToEnroll = c;
        }
        if (courseToEnroll == null) {
            System.out.println("Error registering line " + index + " --> Invalid Course Code");
        } else if (!studenttoEnroll.isPresent()) {
            System.out.println("Error registering line " + index + " --> Invalid Mechanographic / Student Not Found ");
        } else if (!courseToEnroll.courseState().equals(CourseState.ENROLL))
            System.out.println("Error registering line " + index + " --> Course Not Accepting Enrollments ");
        else {
            registerNewEnrollment(studenttoEnroll.get(), courseToEnroll, EnrollmentStatus.ACCEPTED, CurrentTimeCalendars.now());
        }
    }

    public Iterable<Enrollment> allEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Iterable<Enrollment> allEnrollmentsByStudent(Student student) {
        return enrollmentRepository.findByStudent(student);
    }

    public Iterable<Enrollment> pendingRequests() {
        return enrollmentRepository.findByRequestedStatus();
    }

    public Enrollment approveRequest(Enrollment enrollment) {
        enrollment.accepted(CurrentTimeCalendars.now());
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment rejectRequest(Enrollment enrollment) {
        enrollment.rejected(CurrentTimeCalendars.now());
        return enrollmentRepository.save(enrollment);
    }

    public Iterable<Student> findAllStudentsByCourse(Course course) {
        return enrollmentRepository.findByCourseAndStatusAccepted(course);
    }

    public Iterable<Course> findAllCourseByStudent(SystemUser systemUser) {
        return enrollmentRepository.findByAcceptedStatusAndSystemUser(systemUser);
    }
}
