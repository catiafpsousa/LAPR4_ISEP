package eapli.base.meetingmanagement.domain;


import eapli.base.classmanagement.domain.ExtraordinaryClass;
import eapli.base.infrastructure.authz.domain.model.NilPasswordPolicy;
import eapli.base.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.model.SystemUserBuilder;
import eapli.framework.time.util.CurrentTimeCalendars;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

public class MeetingTest  {

    private static final String EMAIL1 = "pcs@isep.pt";

    private static final String PASSWORD = "Password1";

    private static final String FULL_NAME = "Professor Teste Unitario";

    private static final String SHORT_NAME = "Teste";

    private static final String VAT_ID = "123456789";

    private final SystemUserBuilder userBuilder = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder());
    private static final int DAY = 12;

    private static final int MONTH = 01;

    private static final int YEAR = 1985;
    private final SystemUser user = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).build();

    private final List<MeetingParticipant> meetingParticipant = new ArrayList<>();
    private final MeetingParticipant participant =new MeetingParticipant(user, InviteStatus.UNKNOWN);

    private final LocalTime time =  LocalTime.of(9, 30);


    private static  Calendar valid_date = Calendar.getInstance();
    private static  Calendar invalid_date = Calendar.getInstance();

    private final MeetingDuration duration = new MeetingDuration();

    @Test
    public void ensureMeetingHasDate() {
        assertThrows(NullPointerException.class, () ->
                new Meeting(user,null,time, duration, meetingParticipant));
    }
    @Test
    public void ensureMeetingHasInvalidDate() {
        invalid_date.set(2023,01,23);
        assertThrows(IllegalArgumentException.class, () ->
                new Meeting(user, invalid_date,time, duration, meetingParticipant));
    }
    @Test
    public void ensureMeetingHasDuration() {
        valid_date.set(2023,06,23);
        assertThrows(NullPointerException.class, () ->
                new Meeting(user ,valid_date,null, duration , meetingParticipant));
    }

    @Test
    public void ensureMeetingHasToken() {
        valid_date.set(2023,06,23);
        final Meeting e = new Meeting(user, valid_date,time, duration,meetingParticipant);
        assertNotNull(e.identity());
    }

    @Test
    public void ensureRequestIsAccepted(){
        valid_date.set(2023,06,23);
        meetingParticipant.add(participant);
        final Meeting e = new Meeting(user, valid_date,time, duration,meetingParticipant);
        e.updateParticipantInvitationStatus(user, InviteStatus.ACCEPT);


    }



}