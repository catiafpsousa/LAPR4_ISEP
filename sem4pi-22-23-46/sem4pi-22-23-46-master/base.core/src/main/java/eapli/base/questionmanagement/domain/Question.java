package eapli.base.questionmanagement.domain;

import eapli.base.utils.ExamFileManipulation;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;

import javax.persistence.*;
/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */
@Entity
@Table(name = "questions")
@Inheritance(strategy = InheritanceType.JOINED)
public class Question implements AggregateRoot<Long> {

    private static final String SCORE_GRAMMAR_STRING = "score-";

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column (name = "IDQUESTION")
    protected Long id;

    @Version
    protected Long version;

    @Column(name = "CONTENT")
    @Lob
    protected String questionContent;

    @Column(name = "QUESTION_SCORE")
    protected double score;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    protected QuestionType questionType;

    protected Question(){

    }

    public Question(String questionType, String questionContent){
        this.questionType=QuestionType.valueOf(questionType);
        this.questionContent=questionContent;
        this.score=Double.parseDouble(ExamFileManipulation.findContentWithoutQuotesInContent(questionContent,SCORE_GRAMMAR_STRING));
    }


    public QuestionType questionType(){
        return this.questionType;
    }


    public String questionContent(){
        return this.questionContent;
    }


    @Override
    public Long identity() {
        return this.id;
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
}
