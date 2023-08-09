package eapli.base.questionmanagement.domain;

import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import validations.util.Preconditions;

import javax.persistence.*;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */
@Entity
@Table(name="questiontype")
public class QuestionType implements AggregateRoot<Long> {
    private static final long serialVersionUID = 1L;

    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "IDQUESTIONTYPE")
    protected Long id;

    @Column(name="questiontypename")
    private String questionType;

    public QuestionType(String questionType){
        Preconditions.nonEmpty(questionType);
        this.questionType = questionType;
    }

    protected QuestionType() {

    }

    public String questiontype(){
        return this.questionType;
    }

    public static QuestionType valueOf(String type){
        return new QuestionType(type);
    }


    @Override
    public boolean equals(final Object o) {
        return DomainEntities.areEqual(this, o);
    }

    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }

    @Override
    public boolean sameAs(final Object other) {
        return DomainEntities.areEqual(this, other);
    }

    @Override
    public Long identity() {
        return this.id;
    }

    @Override
    public String toString(){
        return this.questionType;
    }
}
