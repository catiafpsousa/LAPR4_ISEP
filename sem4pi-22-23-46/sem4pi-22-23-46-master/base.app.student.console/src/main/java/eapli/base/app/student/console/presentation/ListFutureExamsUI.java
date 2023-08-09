package eapli.base.app.student.console.presentation;

import eapli.base.app.teacher.console.presentation.ListAllExamsOfCourseUI;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.exammanagement.application.ListFutureExamsController;
import eapli.base.exammanagement.domain.AutomaticFormativeExam;
import eapli.base.exammanagement.domain.Exam;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ListFutureExamsUI extends AbstractUI {

    private ListFutureExamsController controller = new ListFutureExamsController();
    private static final Logger LOGGER = LoggerFactory.getLogger(ListAllExamsOfCourseUI.class);


    @Override
    protected boolean doShow() {
        final List<Course> courses = new ArrayList<>();
        final Iterable<Course> iterable = this.controller.allCoursesbyStudent();
        if(!iterable.iterator().hasNext()){
            System.out.println("There are no courses available");
        }else{
            int cont = 1;
            System.out.println("SELECT Course to list Exams\n");
            System.out.printf("%-6s%-10s%-20s%-30s%-10s%n", "No.:", "CODE", "NAME", "DESCRIPTION", "TEACHER IN CHARGE");
            for(final Course course : iterable) {
                courses.add(course);
                if (!course.teacherInCharge().isPresent()) {
                    System.out.printf("%-6d%-10s%-20s%-30s%-10s%n", cont, course.courseCode(), course.courseName(), course.description(), "not assigned");
                } else {
                    System.out.printf("%-6d%-10s%-20s%-30s%-10s%n", cont, course.courseCode(), course.courseName(), course.description(), course.teacherInCharge().get().identity());
                }
                cont++;
            }
            final int option = Console.readInteger("Choose one course to list Future Exams or 0 to finish:");
            if(option == 0){
                System.out.println("No course selected");
            }else{
                try {
                    final List<Exam> exams = new ArrayList<>();
                    final List<AutomaticFormativeExam> formativeExams = new ArrayList<>();
                    final Iterable<AutomaticFormativeExam> iterable3 = controller.allFormativeExamsByCourse(courses.get(option-1).courseCode());
                    final Iterable<Exam> iterable2 = controller.allExamsByCourse(courses.get(option-1).courseCode());
                    System.out.println("Regular Exams:");
                    if (!iterable2.iterator().hasNext()) {
                        System.out.println("There are no regular exams for the selected course!");
                    }else{
                        int cont2 = 1;
                        System.out.printf("%-6s%-40s%-30s%-30s%n", "No.:", "EXAM", "OPEN_DATE", "CLOSE_DATE");
                        for(final Exam exam : iterable2) {
                            exams.add(exam);
                            if (exam.examDates() == null) {
                                System.out.printf("%-6s%-40s%-30s%-30s%n", cont2, exam.title(), "no open date", "no close date");
                                cont2++;
                            } else {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                String formattedDate = dateFormat.format(exam.examDates().openDate().getTime());
                                String formattedDate2 = dateFormat.format(exam.examDates().closeDate().getTime());
                                System.out.printf("%-6s%-40s%-30s%-30s%n", cont2, exam.title(), formattedDate, formattedDate2);
                                cont2++;
                            }
                        }
                    }
                    System.out.println("\nFormative Exams:");
                    if(!iterable3.iterator().hasNext()) {
                        System.out.println("There are no formative exams for the selected course!");
                    }else{
                        int cont3 = 1;
                        System.out.printf("%-6s%-40s%n", "No.:", "EXAM");
                        for(final AutomaticFormativeExam automatic : iterable3) {
                            formativeExams.add(automatic);
                            System.out.printf("%-6s%-40s%n", cont3, automatic.title());
                            cont3++;
                        }
                    }
                } catch(IntegrityViolationException | ConcurrencyException e){
                    LOGGER.error("Error performing the operation.", e);
                    System.out.println("Something went wrong.");
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

    @Override
    public String headline() {
        return "List Future Exams";
    }


    /*
    @Override
    protected Iterable<Exam> elements() {
        return controller.allExamsByCourses();
    }

    @Override
    protected Visitor elementPrinter() {
        return new FutureExamsPrinter();
    }

    @Override
    protected String elementName() {
        return "Exams";
    }

    @Override
    protected String listHeader() {
        return String.format("#  %-20s%-20s", "EXAM", "DESCRIPTION");
    }

    @Override
    protected String emptyMessage() {
        return "No Future Exams to show";
    }

    @Override
    public String headline() {
        return "List of Future Exams";
    }

     */
}
