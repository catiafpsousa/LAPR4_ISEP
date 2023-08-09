package eapli.base.app.teacher.console.presentation;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.grademanagement.application.ListGradeOfCourseController;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.base.grademanagement.domain.ExamGrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;


public class ListGradesOfCourseUI extends AbstractUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListGradesOfCourseUI.class);

    private final ListGradeOfCourseController theController = new ListGradeOfCourseController();


    @Override
    protected boolean doShow() {
        final List<Course> list = new ArrayList<>();
        final Iterable<Course> iterable = this.theController.allCoursesByTeacher();
        if (!iterable.iterator().hasNext()) {
            System.out.println("There are no Courses");
        } else {
            int cont = 1;
            System.out.println("SELECT Course\n");
            System.out.printf("%-6s%-35s%-130s%-10s%n", "No.:", "Code", "Name", "State");
            for (final Course course : iterable) {
                list.add(course);
                System.out.printf("%-6d%-35s%-30s%-10s%n", cont, course.identity(), course.courseName(),
                        course.courseState());
                cont++;
            }
            final int option = Console.readInteger("Enter Course or 0 to finish ");
            if (option == 0) {
                System.out.println("No Course selected");
            } else {
                try {
                    Course course = list.get(option - 1);
                    final List<ExamGrade> grades = new ArrayList<>();

                    final Iterable<ExamGrade> gradeIterable = theController.allGradesByCourse(course.courseCode());
                    System.out.println("Grades:");
                    if (!gradeIterable.iterator().hasNext()) {
                        System.out.println("There are no grades available!");
                    } else {
                        int cont2 = 1;
                        System.out.printf("%-6s%-30s%-30s%-10s%-10s%n", "No.:", "EXAM", "STUDENT", "GRADE", "DATE");
                        for (final ExamGrade grade : gradeIterable) {
                            grades.add(grade);

                            System.out.printf("%-6s%-30s%-30s%-10s%-10s%n", cont2, grade.exam().title(), grade.student().identity(), grade.score(), grade.date().getTime());
                            cont2++;
                        }
                    }


                } catch (IntegrityViolationException | ConcurrencyException ex) {
                    LOGGER.error("Error performing the operation", ex);
                    System.out.println(
                            "Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");
                }
            }
        }


        return false;
    }

    @Override
    public String headline() {
        return "List Grades of a Course";
    }

}
