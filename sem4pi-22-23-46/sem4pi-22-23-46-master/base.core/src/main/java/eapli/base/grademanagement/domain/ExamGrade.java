package eapli.base.grademanagement.domain;

import eapli.base.exammanagement.domain.Exam;
import eapli.base.exammanagement.domain.ExamTitle;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.studentmanagement.domain.Student;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.validations.Preconditions;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table (name = "examGrade")
public class ExamGrade  implements AggregateRoot<Long> {

    @Version
    private Long version;

    @Id
    @Column(name = "idExamGrade")
    @GeneratedValue // (strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private SystemUser student;
    @OneToOne
    private Exam exam;

    private double score;

    private Calendar date;

    protected ExamGrade() {
    }

    public ExamGrade(SystemUser student, Exam exam, double score, Calendar date) {
        Preconditions.noneNull(student);
        Preconditions.noneNull(exam);
        Preconditions.noneNull(date);

        this.student = student;
        this.exam = exam;
        this.score = score;
        this.date = date;

    }

    public Exam exam() {
        return this.exam;
    }

    public SystemUser student() {
        return this.student;
    }

    public double score() {
        return this.score;
    }

    public Calendar date() {
        return this.date;
    }



    @Override
    public boolean sameAs(Object other) {
        return false;
    }

    @Override
    public Long identity() {
        return null;
    }
}
