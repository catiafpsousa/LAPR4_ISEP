package eapli.base.meetingmanagement.repositories;

import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.meetingmanagement.domain.Meeting;
import eapli.base.meetingmanagement.domain.MeetingDuration;
import eapli.base.meetingmanagement.domain.MeetingParticipant;
import eapli.base.meetingmanagement.domain.MeetingToken;
import eapli.framework.domain.repositories.DomainRepository;


import java.time.LocalTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.List;
public interface MeetingRepository extends DomainRepository<MeetingToken, Meeting> {

   Optional<Meeting> ofIdentity(MeetingToken code);
   Iterable<Meeting> findAll();
   Boolean findByDateAndUser(SystemUser systemUser,  Calendar date, LocalTime time);

   List<Meeting> allMeetings(Calendar date, LocalTime time );

   void saveMeetingParticipant(List<MeetingParticipant> availableParticipant);

    Iterable<Meeting> findMeetingsByUser(SystemUser authenticatedUser);

   Iterable<MeetingParticipant> findParticipantsByMeeting(MeetingToken identity);

   Iterable<Meeting> allMeetingsByOwner(SystemUser systemUser);

   Iterable <Meeting> findPendingMeetingsByUser (SystemUser user);
}
