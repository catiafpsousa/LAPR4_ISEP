package eapli.base.meetingmanagement.application;

import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.base.meetingmanagement.domain.InviteStatus;
import eapli.base.meetingmanagement.domain.Meeting;
import eapli.base.meetingmanagement.repositories.MeetingRepository;

import java.util.Optional;

public class AcceptOrRejectMeetingRequestController {
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final MeetingRepository meetingRepository = PersistenceContext.repositories().meetings();
    private final UserRepository userRepo=PersistenceContext.repositories().users();

    public Iterable <Meeting> findPendingInvitations(){
        SystemUser user = currentUser().orElseThrow(IllegalStateException::new);
        return meetingRepository.findPendingMeetingsByUser(user);
    }

    private Optional<SystemUser> currentUser(){
        return authz.session().flatMap(s->userRepo.ofIdentity(s.authenticatedUser().identity()));
    }

    public Meeting acceptOrRejectMeetingRequest(final Meeting meeting, final InviteStatus status){
        SystemUser user = currentUser().orElseThrow(IllegalStateException::new);
        meeting.updateParticipantInvitationStatus(user,status);
        return meetingRepository.save(meeting);
    }
}
