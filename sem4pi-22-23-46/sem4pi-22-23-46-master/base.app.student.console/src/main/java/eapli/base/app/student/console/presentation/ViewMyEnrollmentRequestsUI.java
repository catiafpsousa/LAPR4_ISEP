package eapli.base.app.student.console.presentation;

import eapli.base.enrollmentmanagement.application.ViewMyEnrollmentRequestsController;
import eapli.base.enrollmentmanagement.domain.Enrollment;
import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;
public class ViewMyEnrollmentRequestsUI extends AbstractListUI {
    private ViewMyEnrollmentRequestsController controller = new ViewMyEnrollmentRequestsController();

    @Override
    protected Iterable<Enrollment> elements() {
        return controller.allEnrollmentsByStudent();
    }
    @Override
    protected Visitor<Enrollment> elementPrinter() {
        return new EnrollmentPrinter();
    }
    @Override
    protected String elementName() {
        return "Enrollment";
    }
    @Override
    protected String listHeader() {
        return String.format("#  %-20s%-20s%-20s", "COURSE", "STATUS", "DATE");
    }
    @Override
    protected String emptyMessage() {
        return "No Enrollment Requests";
    }
    @Override
    public String headline() {
        return "View My Enrollment Requests";
    }
}
