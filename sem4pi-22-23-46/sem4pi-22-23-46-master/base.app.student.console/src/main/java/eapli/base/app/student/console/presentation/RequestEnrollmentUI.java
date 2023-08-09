package eapli.base.app.student.console.presentation;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.enrollmentmanagement.application.RequestEnrollmentController;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("squid:S106")
public class RequestEnrollmentUI extends AbstractUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestEnrollmentUI.class);
    private final RequestEnrollmentController theController = new RequestEnrollmentController();


    @Override
    public String headline() {
        return "Request Enrollment in a Course";
    }


    @Override
    public boolean doShow() {
        final List<Course> courses = new ArrayList<>();
        final Iterable<Course> iterable = this.theController.enrollCourses();
        if(!iterable.iterator().hasNext()){
            System.out.println("There are no courses available");
        }else{
            int cont = 1;
            System.out.println("SELECT Course to request enrollment\n");
            System.out.printf("%-6s%-10s%-20s%-30s%-20s%-10s%n", "No.:", "CODE", "NAME", "DESCRIPTION", "COURSE STATE", "TEACHER IN CHARGE");
            for(final Course course : iterable) {
                courses.add(course);
                if (!course.teacherInCharge().isPresent()) {
                    System.out.printf("%-6d%-10s%-20s%-30s%-20s%-10s%n", cont, course.courseCode(), course.courseName(), course.description(), course.courseState(), null);
                } else {
                    System.out.printf("%-6d%-10s%-20s%-30s%-20s%-10s%n", cont, course.courseCode(), course.courseName(), course.description(), course.courseState(), course.teacherInCharge().get().identity());
                }
                cont++;
            }
            final int option = Console.readInteger("Choose one course to request enrollment or 0 to finish:");
            if(option == 0){
                System.out.println("No course selected");
            }else{
                try {
                    this.theController.addNewEnrollment(courses.get(option - 1));
                } catch(IntegrityViolationException | ConcurrencyException e){
                    LOGGER.error("Error performing the operation.", e);
                    System.out.println("Something went wrong.");
                }
            }
        }
        return false;
    }

}
