package eapli.base.app.student.console.presentation;
import eapli.base.enrollmentmanagement.domain.Enrollment;
import eapli.framework.visitor.Visitor;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EnrollmentPrinter implements Visitor<Enrollment> {
    @Override
    public void visit(final Enrollment visitee) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(visitee.modifiedOn().getTime());
        System.out.printf("%-20s%-20s%-20s%n", visitee.course().identity(), visitee.enrollmentStatus(), formattedDate);
    }
}
