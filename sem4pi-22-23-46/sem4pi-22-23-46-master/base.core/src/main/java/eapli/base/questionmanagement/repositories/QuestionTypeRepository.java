package eapli.base.questionmanagement.repositories;

import eapli.base.questionmanagement.domain.Question;
import eapli.base.questionmanagement.domain.QuestionType;
import eapli.framework.domain.repositories.DomainRepository;

import java.util.Optional;

public interface QuestionTypeRepository extends DomainRepository<Long, QuestionType> {

    Iterable<QuestionType> findAll();


}