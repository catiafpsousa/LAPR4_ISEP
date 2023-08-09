package eapli.base.persistence.impl.jpa;

import eapli.base.Application;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.meetingmanagement.domain.*;
import eapli.base.meetingmanagement.repositories.MeetingRepository;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;

import javax.persistence.TypedQuery;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;


public class JpaMeetingRepository extends JpaAutoTxRepository<Meeting, MeetingToken, MeetingToken>implements MeetingRepository{

    public JpaMeetingRepository(TransactionalContext tx) {
        super(tx, "meetingToken");
    }

    public JpaMeetingRepository(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(),
                "meetingToken");
    }

    @Override
    public Optional<Meeting> ofIdentity(MeetingToken token) {
        TypedQuery<Meeting> query = super.createQuery(
                "SELECT e FROM Meeting e WHERE e.token =:token",
                Meeting.class);
        query.setParameter("token", token);

        return Optional.ofNullable(query.getSingleResult());
    }

    public Boolean findByDateAndUser(SystemUser systemUser, Calendar date, LocalTime meetingTimeStart ) {
        final TypedQuery<Boolean> query = entityManager().createQuery("SELECT count(c) > 0 FROM Meeting mp " + "JOIN mp.participantList c " + "WHERE mp.date = :date AND c.systemUser = :systemUser AND :meetingTimeStart BETWEEN  mp.meetingTimeStart AND mp.meetingTimeEnd", Boolean.class);
        query.setParameter("date", date);
        query.setParameter("systemUser", systemUser);
        query.setParameter("meetingTimeStart", meetingTimeStart);
        return query.getSingleResult();
    }

    public List<Meeting> allMeetings(Calendar date ,LocalTime meetingTimeStart){
        final TypedQuery<Meeting> query = entityManager().createQuery( "SELECT m FROM Meeting m where m.date = :date AND :meetingTimeStart BETWEEN  m.meetingTimeStart AND m.meetingTimeEnd", Meeting.class);
        query.setParameter("date", date);
        query.setParameter("meetingTimeStart", meetingTimeStart);
        return query.getResultList();
    }
    @Override
    public void saveMeetingParticipant(List<MeetingParticipant> availableParticipant) {

    }

    public  Iterable<Meeting> allMeetingsByOwner(SystemUser systemUser){
        final TypedQuery<Meeting> query = entityManager().createQuery( "SELECT m FROM Meeting m where m.systemUser =:systemUser AND m.meetingStatus =: meetingStatus", Meeting.class);
        query.setParameter("systemUser", systemUser);
        query.setParameter("meetingStatus", MeetingStatus.ACTIVE);
        return query.getResultList();
    }

    @Override
    public Iterable<Meeting> findPendingMeetingsByUser(SystemUser user) {
        final TypedQuery<Meeting> query = entityManager().createQuery("SELECT mp FROM Meeting mp JOIN mp.participantList c WHERE c.systemUser = :authenticatedUser and c.inviteStatus=:invite and mp.meetingStatus=:status", Meeting.class);
        query.setParameter("authenticatedUser", user);
        query.setParameter("invite", InviteStatus.UNKNOWN);
        query.setParameter("status", MeetingStatus.ACTIVE);
        return query.getResultList();
    }

    @Override
    public Iterable<Meeting> findMeetingsByUser(SystemUser authenticatedUser) {
        final TypedQuery<Meeting> query = entityManager().createQuery("SELECT mp FROM Meeting mp JOIN mp.participantList c WHERE c.systemUser = :authenticatedUser AND mp.meetingStatus = :status", Meeting.class);
        query.setParameter("authenticatedUser", authenticatedUser);
        query.setParameter("status", MeetingStatus.ACTIVE);
        return query.getResultList();
    }

    @Override
    public Iterable<MeetingParticipant> findParticipantsByMeeting(MeetingToken identity) {
        final TypedQuery<MeetingParticipant> query = entityManager().createQuery("SELECT pl FROM Meeting m JOIN m.participantList pl where m.token = :identity", MeetingParticipant.class);
        query.setParameter("identity", identity);
        return query.getResultList();
    }


}
