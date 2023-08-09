package eapli.base.classmanagement.domain;

import eapli.framework.domain.model.ValueObject;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Calendar;

@Embeddable
public class RescheduledClassDate implements ValueObject, Serializable {

    @Temporal(TemporalType.DATE)
    protected Calendar newdDay;

    @Column(name="newtimestart")
    protected LocalTime newTimeStart;

    @Column(name="newtimeend")
    protected LocalTime newTimeEnd;

    public RescheduledClassDate(Calendar newdDay, LocalTime newTimeStart, LocalTime newTimeEnd) {
        this.newdDay = newdDay;
        this.newTimeStart = newTimeStart;
        this.newTimeEnd = newTimeEnd;
    }

    public RescheduledClassDate() {

    }
}
