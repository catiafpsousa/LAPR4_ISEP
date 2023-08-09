package eapli.base.meetingmanagement.domain;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import validations.util.Preconditions;
import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

@Entity
@Table (name = "meeting")
public class Meeting implements AggregateRoot<MeetingToken >  {

    @Version
    private Long version;

    @Column(unique = true, nullable = false)
    private MeetingToken token;

    @Temporal(TemporalType.DATE)
    private Calendar date;

    @Column(name="meetingstart")
    protected LocalTime meetingTimeStart;

    @Column(name="meetingend")
    protected LocalTime meetingTimeEnd;

    @Embedded
    private MeetingDuration duration;

    @OneToOne
    private SystemUser systemUser;

    @Enumerated
    private MeetingStatus meetingStatus;

    @Temporal(TemporalType.DATE)
    private Calendar modifiedOn;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MeetingParticipant> participantList;

    @Id
    @Column(name = "idMeeting")
    @GeneratedValue // (strategy = GenerationType.IDENTITY)
    private Long id;

    protected Meeting() {
    }

    public Meeting(SystemUser systemUser, Calendar date , LocalTime time,  MeetingDuration duration,  List<MeetingParticipant> participantList) {
        Preconditions.isInTheFuture(date,"Class date must be in the future");
        this.date= date;
        this.duration = duration;
        this.meetingTimeStart = time;
        this.participantList = participantList;
         token = MeetingToken.newToken();
         this.meetingTimeEnd = time.plusMinutes(duration.meetingDuration());
         this.systemUser = systemUser;
         this.meetingStatus = MeetingStatus.ACTIVE;
    }

    public void cancel(Calendar now) {
       meetingStatus = MeetingStatus.CANCELED;
        this.modifiedOn = now;
    }
    public int duration(){return duration.meetingDuration();}


    public SystemUser owner(){return systemUser;}


    public LocalTime timestart() {
        return meetingTimeStart;
    }

    public LocalTime timeend() {
        return meetingTimeEnd;
    }

    public MeetingStatus meetingStatus(){return  meetingStatus;}

    public void updateParticipantInvitationStatus(SystemUser user, InviteStatus status){
        for(MeetingParticipant participant: participantList){
            if(participant.list().equals(user)){
                participant.updateParticipantInvitationStatus(status);
                break;
            }
        }
    }

    @Override
    public int compareTo(MeetingToken other) {
        return AggregateRoot.super.compareTo(other);
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(date().getTime());
        return String.format(String.format("| Meeting ID: " +id()+"| Date: " + formattedDate+" | Time: "
                + meetingTimeStart.getHour() + ":"+ meetingTimeStart.getMinute() + " | Duration: "+duration()+" minutes | "));
    }

    @Override
    public MeetingToken identity() {
        return this.token;
    }

    public Long id (){
        return this.id;
    }


    public Calendar date(){
        return this.date;
    }
    public List<MeetingParticipant> list(){return this.participantList;}
    @Override
    public boolean hasIdentity(MeetingToken id) {
        return AggregateRoot.super.hasIdentity(id);
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
