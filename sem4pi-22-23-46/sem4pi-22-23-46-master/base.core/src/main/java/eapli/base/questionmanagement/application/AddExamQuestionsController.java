package eapli.base.questionmanagement.application;

import eapli.base.exammanagement.application.ExamManagementService;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.base.questionmanagement.domain.Question;
import eapli.base.questionmanagement.repositories.QuestionRepository;
import eapli.base.usermanagement.domain.EcourseRoles;
import eapli.framework.application.UseCaseController;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;


/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */

@UseCaseController
public class AddExamQuestionsController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final QuestionRepository questionRepository= PersistenceContext.repositories().questions();
    private final ExamManagementService examSvc = AuthzRegistry.examService();


    @Transactional
    public void addQuestions(String filePath) throws IOException { //retornar uma lista de questões
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.TEACHER);
        Map<String,String> questionsMap = examSvc.getListOfQuestionsFromFile(filePath);
        for (Map.Entry<String, String> questionEntry : questionsMap.entrySet()) {
            Question question = new Question(questionEntry.getKey(), questionEntry.getValue());
            questionRepository.save(question);
        }
        System.out.println("Questions Successfully saved");
    }
}
