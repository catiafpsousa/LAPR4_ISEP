package eapli.base.meetingmanagement.application;

import eapli.base.coursemanagement.application.CourseManagementService;
import eapli.base.exammanagement.application.ExamManagementService;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.meetingmanagement.domain.Meeting;
import eapli.base.meetingmanagement.domain.MeetingParticipant;
import eapli.base.meetingmanagement.domain.MeetingToken;
import eapli.base.usermanagement.domain.EcourseRoles;

public class ListParticipantsInMeetingController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final MeetingManagementService meetingSvc = AuthzRegistry.meetingService();

    public Iterable<Meeting> allMeetingsByUser() {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.POWER_USER, EcourseRoles.ADMIN, EcourseRoles.STUDENT, EcourseRoles.TEACHER);
        return meetingSvc.allMeetingsByUser(authz.session().get().authenticatedUser());
    }

    public Iterable<MeetingParticipant> allParticipantsByMeeting(MeetingToken identity) {
        return meetingSvc.allParticipantsByMeeting(identity);
    }
}
