package eapli.base.app.student.console.presentation;

import eapli.base.app.backoffice.console.presentation.authz.CoursePrinter;
import eapli.base.coursemanagement.application.ListCoursesByUserController;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.coursemanagement.domain.CourseState;
import eapli.base.enrollmentmanagement.domain.Enrollment;
import eapli.base.infrastructure.authz.application.UserSession;
import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.studentmanagement.domain.MechanographicNumber;
import eapli.base.studentmanagement.domain.Student;
import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.ListWidget;
import eapli.framework.visitor.Visitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ListCoursesByUserUI extends AbstractUI {


    private final ListCoursesByUserController theController = new ListCoursesByUserController();

    final SystemUser systemUser = theController.systemUser();

    @Override
    public String headline() {
        return "List COURSES";
    }

    @Override
    protected boolean doShow() {

        System.out.println("Courses open to enroll:");
        final Iterable<Course> iterable = this.theController.allCoursesStateEnroll();


        for (final Course course : iterable) {
            System.out.printf("%-10s%-30s%-15s%-20s%n", "CODE", "NAME", "NR. TEACHER", "COURSE STATUS");

            System.out.printf("%-10s%-30s%-15s%-20s%n", course.identity(), course.courseName(), course.courseTeachers().size(), course.courseState().name());

        }


        System.out.println("\nMy Courses:");
        final Iterable<Course> iterable1 = this.theController.allCoursesByAcceptedStatusAndSystemUser1(systemUser);

        for (final Course course : iterable1) {
            System.out.printf("%-10s%-30s%-15s%-20s%n", "CODE", "NAME", "NR. TEACHER", "COURSE STATUS");
            System.out.printf("%-10s%-30s%-15s%-20s%n", course.identity(), course.courseName(), course.courseTeachers().size(), course.courseState().name());

        }

        return false;
    }


}
