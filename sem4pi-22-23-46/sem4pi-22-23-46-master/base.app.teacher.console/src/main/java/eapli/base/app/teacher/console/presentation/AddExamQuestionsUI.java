package eapli.base.app.teacher.console.presentation;

import eapli.base.questionmanagement.application.AddExamQuestionsController;
import eapli.framework.io.util.Console;
import eapli.framework.io.util.Files;
import eapli.framework.presentation.console.AbstractUI;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.io.IOException;


public class AddExamQuestionsUI extends AbstractUI{
    private final AddExamQuestionsController ctrl= new AddExamQuestionsController();


    @Override
    protected boolean doShow() {

        boolean validExam = false;
        do {
            String filePath = Console.readNonEmptyLine("Insert the file path/name of the file where the questions are saved or type -> 0 <- to return to MENU", "The file name shoudn't be empty");
            if (filePath.trim().equals("0")) return false;
            filePath = Files.ensureExtension(filePath, ".txt");
            try{
                ctrl.addQuestions(filePath);
                validExam = true;
                System.out.println(CharStreams.fromFileName(filePath));
                System.out.println("***Questions successfully created and saved***");
            } catch (ParseCancellationException pEx) {
                System.out.println("\n!!!The file submited has grammar errors!!!\n");
            } catch (IOException ex) {
                System.out.println("\n!!!File not found! Enter other file path!!!\n");
            }
        } while (!validExam);

        return false;
    }

    @Override
    public String headline() {
        return "Add Exam Question";
    }
}