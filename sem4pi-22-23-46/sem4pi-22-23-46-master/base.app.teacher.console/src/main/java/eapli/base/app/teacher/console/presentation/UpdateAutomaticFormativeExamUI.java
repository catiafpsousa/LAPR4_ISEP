package eapli.base.app.teacher.console.presentation;

import eapli.base.exammanagement.application.UpdateAutomaticFormativeExamController;
import eapli.base.exammanagement.domain.AutomaticFormativeExam;
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

public class UpdateAutomaticFormativeExamUI extends AbstractUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAutomaticFormativeExamUI.class);

    private final UpdateAutomaticFormativeExamController ctrl = new UpdateAutomaticFormativeExamController();

    @Override
    protected boolean doShow() {

        final List<AutomaticFormativeExam> list = new ArrayList<>();
        final Iterable<AutomaticFormativeExam> examsIterable = this.ctrl.findAll();
        if (!examsIterable.iterator().hasNext()) {
            System.out.println("There is no registered Automatic Formative exam");
        } else {
            int cont = 1;
            System.out.println("SELECT Automatic Formative exam to Update\n");

            for (final AutomaticFormativeExam exam: examsIterable) {
                System.out.printf("%-2d.", cont);
                System.out.printf("%-20s\n", exam.title());
                list.add(exam);
                cont++;
            }

            final Integer selection = Console.readInteger("\nEnter selection: ");
            if (selection == null) {
                System.out.println("No Automatic Formative exam selected");
            } else {
                try {
                    boolean validFile = false;
                    String examContent="";
                    do {
                        String filePath = Console.readNonEmptyLine("Insert the file path/name of the file where the exam is saved or type -> 0 <- to return to MENU", "The file name shoudn't be empty");
                        if (filePath.trim().equals("0")) return false;
                        filePath = Files.ensureExtension(filePath, ".txt");
                        try {
                            AutomaticFormativeExam exam = list.get(selection - 1);
                            this.ctrl.updateExam(exam, filePath);
                            validFile = true;
                            System.out.println(CharStreams.fromFileName(filePath));
                            System.out.println("***Automatic Exam updated with success***");
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
        return "Update Automatic Formative Exam";
    }
}
