package eapli.base.enrollmentmanagement.application;

import eapli.base.coursemanagement.application.CourseManagementService;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.enrollmentmanagement.domain.Enrollment;
import eapli.base.enrollmentmanagement.domain.EnrollmentStatus;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.studentmanagement.application.StudentManagementService;
import eapli.base.usermanagement.domain.EcourseRoles;
import eapli.framework.application.UseCaseController;
import eapli.framework.time.util.CurrentTimeCalendars;

import java.util.Calendar;

@UseCaseController
public class RequestEnrollmentController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final CourseManagementService courseSvc = AuthzRegistry.courseService();
    private final StudentManagementService studentSvc = AuthzRegistry.studentService();

    private final EnrollmentManagementService enrollmentSvc = AuthzRegistry.enrollmentService();

    public Iterable<Course> enrollCourses() {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        return courseSvc.enrollingCourses();
    }


    public Enrollment addNewEnrollment(Course course, final Calendar createdOn) {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        final SystemUser user = authz.session().get().authenticatedUser();
        return enrollmentSvc.registerNewEnrollment(user, course, EnrollmentStatus.REQUESTED, createdOn);
    }



    public Enrollment addNewEnrollment(Course course) {
        //authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        return addNewEnrollment(course, CurrentTimeCalendars.now());
    }


}
