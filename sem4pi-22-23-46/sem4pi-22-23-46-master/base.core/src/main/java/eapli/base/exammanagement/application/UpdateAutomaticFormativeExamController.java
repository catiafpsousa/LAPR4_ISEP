package eapli.base.exammanagement.application;

import eapli.base.exammanagement.domain.AutomaticFormativeExam;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;
import java.text.ParseException;

public class UpdateAutomaticFormativeExamController {

    private final ExamManagementService examSvc = AuthzRegistry.examService();
    public Iterable<AutomaticFormativeExam> findAll() {
        return examSvc.findAllAutomaticFormativeExam();
    }

    public void updateExam(AutomaticFormativeExam exam, String filePath ) throws IOException, ParseException {
        examSvc.verifyAutomaticFormativeExam(filePath);
        AutomaticFormativeExam automaticFormativeExam = new AutomaticFormativeExam(CharStreams.fromFileName(filePath).toString());

        if (!(exam.title().equals(automaticFormativeExam.title()))) throw new IllegalArgumentException("!!!The exam title should be the same as the title of the exam to be updated!!!");
        examSvc.updateExam(exam, automaticFormativeExam);
    }

}
