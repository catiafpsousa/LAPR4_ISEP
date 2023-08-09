package eapli.base.exammanagement.application;

import eapli.base.exammanagement.domain.Exam;
import eapli.base.exammanagement.repositories.ExamRepository;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.base.usermanagement.domain.EcourseRoles;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;
import java.text.ParseException;

public class CreateExamController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final ExamManagementService examSvc = AuthzRegistry.examService();
    private final ExamRepository examRepository = PersistenceContext.repositories().exams();

//    private final QuestionManagementService questionSvc = AuthzRegistry.questionService();

    public Iterable<Exam> findAll() {
        return examSvc.findAll();
    }


    public void createExam(String examPath) throws IOException, ParseException {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.TEACHER);
        examSvc.verifyExam(examPath);
        Exam exam = new Exam(CharStreams.fromFileName(examPath).toString());
        if (!examSvc.verifyCourse(exam.course())) throw new IllegalArgumentException("!!!The course present on the exam submited doesn't exist!!!");
        examRepository.save(exam);
    }
}

