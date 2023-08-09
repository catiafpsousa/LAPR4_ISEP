package eapli.base.meetingmanagement.application;

import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.meetingmanagement.domain.Meeting;

public class CancelMeetingController {
    private final AuthorizationService authz = AuthzRegistry.authorizationService();

    private final MeetingManagementService meetingSvc = AuthzRegistry.meetingService();

    public SystemUser systemUser() {
        return authz.session().get().authenticatedUser();
    }

    public Iterable<Meeting> allMeetingsByOwner(SystemUser systemUser) {
        return meetingSvc.allMeetingsByOwner(systemUser);
    }

    public void cancelMeeting(Meeting meeting) {
        meetingSvc.cancelMeeting(meeting);
    }
}
