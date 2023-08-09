package eapli.base.meetingmanagement.domain;

import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.domain.model.ValueObject;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table (name = "meetingParticipant")
public class MeetingParticipant implements ValueObject, Serializable {

    @Enumerated(EnumType.STRING)
    private InviteStatus inviteStatus;

    @Id
    @Column(name = "idMeetingParticipant")
    @GeneratedValue // (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private SystemUser systemUser;


    public MeetingParticipant(SystemUser systemUser, InviteStatus
                              inviteStatus) {
        this.systemUser = systemUser;
        this.inviteStatus = inviteStatus;

    }

    public MeetingParticipant() {

    }
    public SystemUser list(){return this.systemUser;}

    public void accept() {
        inviteStatus = InviteStatus.ACCEPT;
    }

    public void reject() {
        inviteStatus = InviteStatus.REJECT;
    }
    public void unknown() {
        inviteStatus = InviteStatus.UNKNOWN;
    }

    public void updateParticipantInvitationStatus(InviteStatus newStatus){
        this.inviteStatus= newStatus;
    }

    public boolean equals(final SystemUser o) {
        return DomainEntities.areEqual( this.systemUser,o);
    }

    public boolean sameAs(final SystemUser other) {
        return DomainEntities.areEqual( this.systemUser, other);
    }

    public InviteStatus getInviteStatus() {
        return inviteStatus;
    }
}
