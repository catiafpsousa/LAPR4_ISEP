package eapli.base.meetingmanagement.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.ValueObject;
import eapli.framework.time.domain.model.DateInterval;
import eapli.framework.time.domain.model.TimeInterval;
import eapli.framework.validations.Preconditions;

import javax.persistence.Column;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.time.LocalTime;

public class MeetingDuration implements ValueObject, Serializable {
    private static final long serialVersionUID = 1L;


    private int meetingDuration;



    private MeetingDuration(final int meetingDuration) {
        Preconditions.isPositive(meetingDuration, "Class duration must be positive");
        this.meetingDuration = meetingDuration;
    }

    protected MeetingDuration() {
        this.meetingDuration = -1;
    }

    protected int meetingDuration(){return meetingDuration;}

    public static MeetingDuration valueOf(final int meetingDuration) {
        return new MeetingDuration(meetingDuration);
    }

    @Override
    public String toString() {
        return meetingDuration + " min";
    }
}
