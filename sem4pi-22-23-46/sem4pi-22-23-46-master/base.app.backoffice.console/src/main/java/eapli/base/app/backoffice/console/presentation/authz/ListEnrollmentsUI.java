package eapli.base.app.backoffice.console.presentation.authz;

import eapli.base.enrollmentmanagement.domain.Enrollment;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.usermanagement.application.ListAndFindController;
import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;

public class ListEnrollmentsUI extends AbstractListUI<Enrollment>{
        private ListAndFindController theController = new ListAndFindController();

        @Override
        public String headline() {
            return "List ENROLLMENTS";
        }

        @Override
        protected String emptyMessage() {
            return "No data.";
        }

        @Override
        protected Iterable<Enrollment> elements() {
            return theController.allEnrollments();
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
            return String.format("# %-4s%-20s%-50s%-10s%-20s%n", "No.", "MECHANOGRAPHIC No.", "NAME", "COURSE", "STATUS");
        }
}
