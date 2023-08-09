package eapli.base.exammanagement.application;

import eapli.base.coursemanagement.application.CourseManagementService;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.exammanagement.domain.AutomaticFormativeExam;
import eapli.base.exammanagement.domain.Exam;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.usermanagement.domain.EcourseRoles;

import java.util.ArrayList;
import java.util.List;

public class ListFutureExamsController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final CourseManagementService courseSvc = AuthzRegistry.courseService();
    private final ExamManagementService examSvc = AuthzRegistry.examService();

    public Iterable<Course>allCoursesbyStudent(){
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        return courseSvc.allCoursesByAcceptedStatusAndSystemUser(authz.session().get().authenticatedUser());
    }

    public Iterable<Exam>allExamsByCourse(CourseCode code){
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        return examSvc.findExamsByCourse(code);
    }

    public Iterable<AutomaticFormativeExam> allFormativeExamsByCourse(CourseCode code) {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        return examSvc.findFormativeExamsByCourse(code);
    }

}
