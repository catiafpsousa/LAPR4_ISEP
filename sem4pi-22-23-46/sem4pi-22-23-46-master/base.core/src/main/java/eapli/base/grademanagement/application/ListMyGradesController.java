package eapli.base.grademanagement.application;

import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.grademanagement.domain.ExamGrade;
import eapli.base.usermanagement.domain.EcourseRoles;

public class ListMyGradesController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final GradeManagementService gradeSvc = AuthzRegistry.gradeService() ;


    public Iterable<ExamGrade> allGradesByStudent() {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        return gradeSvc.allGradesByStudent(authz.session().get().authenticatedUser());
    }
}
