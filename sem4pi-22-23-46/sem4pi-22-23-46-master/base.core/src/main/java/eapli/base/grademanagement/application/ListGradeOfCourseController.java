package eapli.base.grademanagement.application;

import eapli.base.coursemanagement.application.CourseManagementService;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.grademanagement.domain.ExamGrade;
import eapli.base.usermanagement.domain.EcourseRoles;

import java.util.List;

public class ListGradeOfCourseController {
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final GradeManagementService gradeSvc = AuthzRegistry.gradeService() ;
    private final CourseManagementService courseSvc = AuthzRegistry.courseService() ;


    public  Iterable<Course> allCoursesByTeacher(){
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.TEACHER);
        Iterable<Course> courseList = courseSvc.allCoursesBySystemUser(authz.session().get().authenticatedUser());
        return courseList;
    }

    public Iterable<ExamGrade> allGradesByCourse(CourseCode courseCode) {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.TEACHER);
        return gradeSvc.allGradesByCourse(courseCode);
    }
}
