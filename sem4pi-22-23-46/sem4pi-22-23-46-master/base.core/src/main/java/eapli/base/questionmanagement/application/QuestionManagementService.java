package eapli.base.questionmanagement.application;

import eapli.base.questionmanagement.domain.ECourseQuestionTypeStrings;
import eapli.base.questionmanagement.domain.Question;
import eapli.base.questionmanagement.domain.QuestionType;
import eapli.base.questionmanagement.repositories.QuestionRepository;
import eapli.base.questionmanagement.repositories.QuestionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class QuestionManagementService {
    private final QuestionRepository questionRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private QuestionTypeRepository repository;
    public static final QuestionType MATCHING_QUESTION = QuestionType.valueOf("Matching");
    public static final QuestionType MULTIPLE_CHOICE = QuestionType.valueOf("Multiple choice");
    public static final QuestionType SHORT_ANSWER = QuestionType.valueOf("Short answer");
    public static final QuestionType NUMERICAL = QuestionType.valueOf("Numerical");
    public static final QuestionType SELECT_MISSING_WORDS = QuestionType.valueOf("Select missing words");
    public static final QuestionType TRUE_OR_FALSE = QuestionType.valueOf("True/False");


    @Autowired
    public QuestionManagementService(QuestionRepository questionRepository, QuestionTypeRepository questionTypeRepository){
       this.questionRepository=questionRepository;
       this.questionTypeRepository=questionTypeRepository;
    }

    @Transactional
    public void InitializeQuestionTypes() {
        questionTypeRepository.save(QuestionType.valueOf(ECourseQuestionTypeStrings.MATCHING_QUESTION));
        questionTypeRepository.save(QuestionType.valueOf(ECourseQuestionTypeStrings.MULTIPLE_CHOICE));
        questionTypeRepository.save(QuestionType.valueOf(ECourseQuestionTypeStrings.SHORT_ANSWER));
        questionTypeRepository.save(QuestionType.valueOf(ECourseQuestionTypeStrings.NUMERICAL));
        questionTypeRepository.save(QuestionType.valueOf(ECourseQuestionTypeStrings.SELECT_MISSING_WORDS));
        questionTypeRepository.save(QuestionType.valueOf(ECourseQuestionTypeStrings.TRUE_OR_FALSE));
    }


    public Optional<Question> findQuestionByType(String questionType) {
        return questionRepository.findQuestionByQuestionType(questionType);
    }


}