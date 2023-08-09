package eapli.base.app.teacher.console.presentation;

import eapli.base.exammanagement.application.CreateExamController;
import eapli.framework.io.util.Console;
import eapli.framework.io.util.Files;
import eapli.framework.presentation.console.AbstractUI;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.io.IOException;
import java.text.ParseException;

public class CreateExamUI extends AbstractUI {

    private final CreateExamController ctrl = new CreateExamController();

    @Override
    protected boolean doShow() {

        boolean validExam = false;
        String examContent="";
        do {
            String filePath = Console.readNonEmptyLine("Insert the file path/name of the file where the exam is saved or type -> 0 <- to return to MENU", "The file name shoudn't be empty");
            if (filePath.trim().equals("0")) return false;
            filePath = Files.ensureExtension(filePath, ".txt");
            try{
                ctrl.createExam(filePath);
                validExam = true;
                System.out.println(CharStreams.fromFileName(filePath));
                System.out.println("***Exam successfully created and saved***");
            } catch (ParseCancellationException pEx) {
                System.out.println("\n!!!The file submited has grammar errors!!!\n");
            } catch (IOException ex) {
                System.out.println("\n!!!File not found! Enter other file path!!!\n");
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            } catch (ParseException e) {
                System.out.println("\n!!!The format of the dates on the chosen exam file is wrong!!!\n");
            }
        } while (!validExam);

        System.out.println(examContent);

        return false;
    }

    @Override
    public String headline() {
        return "Create Exam";
    }
}
