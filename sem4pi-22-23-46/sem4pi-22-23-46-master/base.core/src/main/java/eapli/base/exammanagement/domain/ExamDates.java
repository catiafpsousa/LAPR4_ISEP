package eapli.base.exammanagement.domain;

import eapli.framework.domain.model.ValueObject;
import validations.util.Preconditions;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Calendar;

@Embeddable
public class ExamDates implements ValueObject, Serializable {

    @Temporal(TemporalType.DATE)
    private Calendar openDate;

    @Temporal(TemporalType.DATE)
    private Calendar closeDate;

    protected ExamDates() {
    }

    public ExamDates(Calendar openDate, Calendar closeDate) {
        this.openDate = openDate;
        this.closeDate = closeDate;
        Preconditions.isAfter(openDate, closeDate, "Close date must be greater than open date!");
    }

    public static ExamDates valueOf(final Calendar openDate, final Calendar closeDate) {
        return new ExamDates(openDate, closeDate);
    }

    public Calendar openDate (){return this.openDate;}

    public Calendar closeDate (){return this.closeDate;}

}
