package eapli.base.exammanagement.application;

import eapli.base.exammanagement.domain.Exam;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;
import java.text.ParseException;

public class UpdateExamController {
    private final ExamManagementService examSvc = AuthzRegistry.examService();

    public Iterable<Exam> findAll() {
        return examSvc.findAll();
    }

    public void updateExam(Exam exam, String filePath ) throws IOException, ParseException {
        examSvc.verifyExam(filePath);
        Exam examNew = new Exam(CharStreams.fromFileName(filePath).toString());

        if (!(exam.title().equals(examNew.title()))) throw new IllegalArgumentException("!!!The exam title should be the same as the title of the exam to be updated!!!");
        examSvc.updateNormalExam(exam, examNew);
    }
}
