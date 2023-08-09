package eapli.base.enrollmentmanagement.application;
import eapli.base.enrollmentmanagement.domain.Enrollment;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.studentmanagement.application.StudentManagementService;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.usermanagement.domain.EcourseRoles;
import eapli.framework.application.UseCaseController;

import java.util.Optional;

@UseCaseController
public class ViewMyEnrollmentRequestsController {

        private final AuthorizationService authz = AuthzRegistry.authorizationService();
        private final EnrollmentManagementService enrollmentSvc = AuthzRegistry.enrollmentService();
    private final StudentManagementService studentSvc = AuthzRegistry.studentService();

    public Iterable<Enrollment> allEnrollmentsByStudent() {
            authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
            Optional<Student> student = studentSvc.studentOfUser(authz.session().get().authenticatedUser().identity());
            return enrollmentSvc.allEnrollmentsByStudent(student.get());
        }
    }

