package eapli.base.app.teacher.console.presentation;

import eapli.base.exammanagement.application.UpdateExamController;
import eapli.base.exammanagement.domain.Exam;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.io.util.Files;
import eapli.framework.presentation.console.AbstractUI;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class UpdateExamUI extends AbstractUI {


    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateExamUI.class);

    private final UpdateExamController ctrl = new UpdateExamController();

    @Override
    protected boolean doShow() {

        final List<Exam> list = new ArrayList<>();
        final Iterable<Exam> examsIterable = this.ctrl.findAll();
        if (!examsIterable.iterator().hasNext()) {
            System.out.println("There is no registered Exam");
        } else {
            int cont = 1;
            System.out.println("SELECT Exam to Update\n");

            for (final Exam exam: examsIterable) {
                System.out.printf("%-2d.", cont);
                System.out.printf("%-20s\n", exam.title());
                list.add(exam);
                cont++;
            }

            final Integer selection = Console.readInteger("\nEnter selection: ");
            if (selection == null) {
                System.out.println("No Exam selected");
            } else {
                try {
                    boolean validFile = false;
                    do {
                        String filePath = Console.readNonEmptyLine("Insert the file path/name of the file where the exam is saved or type -> 0 <- to return to MENU", "The file name shoudn't be empty");
                        if (filePath.trim().equals("0")) return false;
                        filePath = Files.ensureExtension(filePath, ".txt");
                        try {
                            Exam exam = list.get(selection - 1);
                            this.ctrl.updateExam(exam, filePath);
                            validFile = true;
                            System.out.println(CharStreams.fromFileName(filePath));
                            System.out.println("***Exam updated with success***");
                        } catch (ParseCancellationException pEx) {
                            System.out.println("\n!!!The file submited has grammar errors!!!\n");
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        } catch (IOException ex) {
                            System.out.println("\n!!!File not found! Enter other file path!!!\n");
                        } catch (ParseException e) {
                            System.out.println("\n!!!The format of the dates on the chosen exam file is wrong!!!\n");
                        }
                    } while (!validFile);

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
        return "Update Exam";
    }
}
