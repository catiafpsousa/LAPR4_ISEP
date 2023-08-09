package eapli.base.app.backoffice.console.presentation.authz;

import eapli.base.enrollmentmanagement.domain.Enrollment;
import eapli.framework.visitor.Visitor;

public class EnrollmentPrinter implements Visitor<Enrollment> {

    @Override
    public void visit(final Enrollment visitee) {
        System.out.printf("%-20s%-50s%-10s%-20s", visitee.student().identity(), visitee.student().user().fullName(), visitee.course().courseCode(), visitee
                .enrollmentStatus());
    }
}
