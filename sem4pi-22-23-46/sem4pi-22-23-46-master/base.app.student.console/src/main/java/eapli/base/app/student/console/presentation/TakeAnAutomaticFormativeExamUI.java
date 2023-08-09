package eapli.base.app.student.console.presentation;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.exammanagement.application.TakeAnAutomaticFormativeExamControler;
import eapli.base.exammanagement.domain.AutomaticFormativeExam;
import eapli.base.exammanagement.domain.Exam;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.io.util.Files;
import eapli.framework.presentation.console.AbstractUI;
import org.antlr.v4.runtime.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TakeAnAutomaticFormativeExamUI extends AbstractUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TakeAnAutomaticFormativeExamUI.class);
    private final TakeAnAutomaticFormativeExamControler ctrl = new TakeAnAutomaticFormativeExamControler();


    @Override
    public boolean doShow() {
        try {
            final List<Course> list = new ArrayList<>();
            final Iterable<Course> iterable = this.ctrl.allCoursesByStudent();
            if (!iterable.iterator().hasNext()) {
                System.out.println("There are no Courses");
                return false;
            }
            int cont = 1;
            System.out.println("SELECT Course \n");
            System.out.printf("%-6s%-35s%-15s%n", "No.:", "Code", "Name");
            for (final Course course : iterable) {
                list.add(course);
                System.out.printf("%-6d%-35s%-15s%n", cont, course.identity(), course.courseName());
                cont++;
            }
            final int option = Console.readInteger("Enter Course to CLOSE or 0 to finish ");
            if (option == 0) {
                System.out.println("No Course selected");
                return false;
            }
            Course course = list.get(option - 1);
            final List<AutomaticFormativeExam> listExam = new ArrayList<>();
            final Iterable<AutomaticFormativeExam> iterableExam = this.ctrl.allExamsByCourse(course.courseCode());
            if (!iterableExam.iterator().hasNext()) {
                System.out.println("There are no Exams");
                return false;
            }
            int cont1 = 1;
            System.out.println("SELECT Exam\n");
            System.out.printf("%-6s%-35s%-15s%n", "No.:", "Name", "Course");
            for (final AutomaticFormativeExam exam : iterableExam) {
                listExam.add(exam);
                System.out.printf("%-6d%-35s%-15s%n", cont1, exam.title(), exam.course());
                cont1++;
            }
            final int option1 = Console.readInteger("Enter Exam or 0 to finish ");
            if (option1 == 0) {
                System.out.println("No Exam selected");
                return false;
            }
            AutomaticFormativeExam exam1 = listExam.get(option1 - 1);
            System.out.println("Chose where you want to save the exam to be answered to.");
            System.out.println(" 1 - Current directory");
            System.out.println(" 2 - Specify a specific directory");
            System.out.println(" 0 - EXIT MENU");

            int chosenOption = Console.readOption(1, 2, 0);
            if (chosenOption == 0) return false;
            String filePath = "";
            if (chosenOption == 2) {
                boolean validPath;
                do {
                    filePath = Console.readNonEmptyLine("Insert the path where you would like to save the exam to be answerd to.", "The path can't be empty");
                    File testPath = new File(filePath);
                    validPath = testPath.exists();
                    if (!validPath) System.out.println("!!Insert a valid path!!");
                } while (!validPath);
            }
            if (!filePath.isBlank()) filePath = filePath.concat("/");
            filePath = filePath.concat(exam1.course() + "_automaticExam.txt");

            try {

                ctrl.saveExamToBeAnswer(exam1.examContent(), filePath);
                System.out.println("***Exam to be answered generated and saved with success***");
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("----Leaving Menu----");
                return false;
            }

            boolean validAnswer = false;
            do {
                String filePath1 = Console.readNonEmptyLine("Insert the file path/name of the file where the answers are saved.", "The file name shoudn't be null");
                filePath1 = Files.ensureExtension(filePath1, ".txt");
                try {
                    String examFeedback = ctrl.takeAnAutomaticFormativeExam(exam1.examContent(), filePath1);
                    System.out.println("****EXAM FEEDBACK****\n" + examFeedback);
                    validAnswer = true;
                } catch (IOException | ParseException ex) {
                    System.out.println("\n!!!File not found! Enter other file path!!!\n");
                }
            } while (!validAnswer);


        } catch (IntegrityViolationException | ConcurrencyException ex) {
            LOGGER.error("Error performing the operation", ex);
            System.out.println(
                    "Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");


        }

        return false;
    }


    @Override
    public String headline() {
        return "Take An Automatic Formative Exam";
    }

}
