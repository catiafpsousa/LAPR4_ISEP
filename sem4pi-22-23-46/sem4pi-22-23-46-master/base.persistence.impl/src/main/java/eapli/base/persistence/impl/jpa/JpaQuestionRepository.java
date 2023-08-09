package eapli.base.persistence.impl.jpa;

import eapli.base.Application;
import eapli.base.meetingmanagement.domain.Meeting;
import eapli.base.questionmanagement.domain.Question;
import eapli.base.questionmanagement.domain.QuestionType;
import eapli.base.questionmanagement.repositories.QuestionRepository;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import net.bytebuddy.dynamic.DynamicType;

import javax.persistence.TypedQuery;
import java.util.Optional;

public class JpaQuestionRepository extends JpaAutoTxRepository<Question, Long, Long> implements QuestionRepository {
    public JpaQuestionRepository(TransactionalContext tx) {
        super(tx, "id");
    }

    public JpaQuestionRepository(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(),
                "id");
    }

    public Optional<Question> findQuestionByQuestionType(String questionType) {
        final TypedQuery<Question> query = entityManager().createQuery("SELECT q FROM Question q  where q.questionType.questionType = :questionType", Question.class);
        query.setParameter("questionType", questionType);
        return Optional.ofNullable(query.getSingleResult());
    }
}
