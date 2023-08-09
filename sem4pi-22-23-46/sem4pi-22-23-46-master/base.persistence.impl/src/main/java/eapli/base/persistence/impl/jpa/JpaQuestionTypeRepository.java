package eapli.base.persistence.impl.jpa;

import eapli.base.Application;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.exammanagement.domain.Exam;
import eapli.base.questionmanagement.domain.Question;
import eapli.base.questionmanagement.domain.QuestionType;
import eapli.base.questionmanagement.repositories.QuestionTypeRepository;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;

import javax.persistence.TypedQuery;
import java.util.Optional;

public class JpaQuestionTypeRepository extends JpaAutoTxRepository<QuestionType, Long,Long> implements QuestionTypeRepository {
    public JpaQuestionTypeRepository(TransactionalContext tx) {
        super(tx, "id");
    }

    public JpaQuestionTypeRepository(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(),
                "id");
    }
    public Iterable<QuestionType> findAll(){
        return super.findAll();
    }


}
