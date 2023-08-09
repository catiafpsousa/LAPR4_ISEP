package eapli.base.app.teacher.console.presentation;

import eapli.base.app.backoffice.console.presentation.authz.CoursePrinter;
import eapli.base.coursemanagement.application.ListCoursesByUserController;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.coursemanagement.domain.CourseState;
import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;

import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ListCoursesByUserUI extends AbstractListUI<Course> {



    private final ListCoursesByUserController theController = new ListCoursesByUserController();

    final SystemUser systemUser = theController.systemUser();

    @Override
    public String headline() {
        return "List COURSES";
    }

    @Override
    protected String emptyMessage() {
        return "No data.";
    }


    protected Iterable<Course> elements () {
        return allCoursesBySystemUser();
    }

    protected Iterable<Course> allCoursesBySystemUser() {
        return theController.allCoursesBySystemUser(systemUser);
    }



    @Override
    protected Visitor<Course> elementPrinter() {
        return new CoursePrinter();
    }

    @Override
    protected String elementName() {
        return "Course";
    }

    @Override
    protected String listHeader() {
        return String.format("#  %-15s%-35s%-15s%-30s%-10s", "CODE", "NAME", "NR. TEACHER", "TEACHER IN CHARGE", "COURSE STATUS");
    }
}
