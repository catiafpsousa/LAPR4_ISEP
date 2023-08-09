package eapli.base.coursemanagement.application;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.enrollmentmanagement.domain.Enrollment;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.usermanagement.domain.EcourseRoles;
import eapli.framework.application.UseCaseController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author losa
 */
@UseCaseController
public class ListCoursesByUserController {
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final CourseManagementService courseSvc = AuthzRegistry.courseService();


    public Iterable<Course> allCoursesStateEnroll() {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        return courseSvc.enrollingCourses();
    }


    public Iterable<Course> allCoursesBySystemUser(SystemUser systemUser) {

        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.TEACHER);
        return courseSvc.allCoursesBySystemUser(systemUser);
    }




    public Iterable<Course> allCoursesByAcceptedStatusAndSystemUser1(SystemUser systemUser) {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        return  courseSvc.allCoursesByAcceptedStatusAndSystemUser(systemUser);
    }


    public SystemUser systemUser() {

        return authz.session().get().authenticatedUser();
    }


}


