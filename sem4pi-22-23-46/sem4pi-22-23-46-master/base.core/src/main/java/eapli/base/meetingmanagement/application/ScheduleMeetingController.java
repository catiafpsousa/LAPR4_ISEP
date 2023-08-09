package eapli.base.meetingmanagement.application;


import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.meetingmanagement.domain.Meeting;
import eapli.base.meetingmanagement.domain.MeetingDuration;
import eapli.base.meetingmanagement.domain.MeetingParticipant;
import eapli.base.usermanagement.domain.EcourseRoles;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

public class ScheduleMeetingController {
    private final AuthorizationService authz = AuthzRegistry.authorizationService();

    private final MeetingManagementService meetingSvc = AuthzRegistry.meetingService();

    public SystemUser systemUser() {
        return authz.session().get().authenticatedUser();
    }


     public Meeting scheduleMeeting(SystemUser systemUser,List<MeetingParticipant> list,  final Calendar data,final LocalTime time, final MeetingDuration duration) {

        return meetingSvc.scheduleNewMeeting(systemUser,data, duration,time, list);

    }

    public List<MeetingParticipant> availableMeetingParticipant(String selection, List<SystemUser> list, final Calendar date, final LocalTime time,   final MeetingDuration duration ){
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.POWER_USER, EcourseRoles.ADMIN, EcourseRoles.STUDENT, EcourseRoles.TEACHER);

        return meetingSvc.availableMeetingParticipant(selection, list,date,time);
    }

    public Iterable<SystemUser> allUsers() {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.POWER_USER, EcourseRoles.ADMIN, EcourseRoles.STUDENT, EcourseRoles.TEACHER);
        return meetingSvc.allUsers();
    }


    public Iterable<Meeting> allMeetings() {
        return meetingSvc.allMeetings();
    }
}