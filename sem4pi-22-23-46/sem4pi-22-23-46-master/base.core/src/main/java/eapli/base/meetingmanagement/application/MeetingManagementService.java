package eapli.base.meetingmanagement.application;

import eapli.base.classmanagement.repositories.ClassRepository;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.coursemanagement.domain.CourseState;
import eapli.base.enrollmentmanagement.repositories.EnrollmentRepository;
import eapli.base.infrastructure.authz.application.PasswordPolicy;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.meetingmanagement.domain.*;
import eapli.base.meetingmanagement.repositories.MeetingRepository;
import eapli.base.studentmanagement.repositories.StudentRepository;
import eapli.base.teachermanagement.repositories.TeacherRepository;
import eapli.framework.time.util.CurrentTimeCalendars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;

@Component
public class MeetingManagementService {
    //  private final ScheduleRepository scheduleRepository;
    private final MeetingRepository meetingRepository;

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final PasswordPolicy policy;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    public MeetingManagementService(final UserRepository userRepo, final PasswordPolicy policy,
                                    final PasswordEncoder encoder, final TeacherRepository teacherRepository, final StudentRepository studentRepository, final MeetingRepository meetingRepository, final ClassRepository classRepository, final EnrollmentRepository enrollmentRepository) {
        userRepository = userRepo;
        this.policy = policy;
        this.encoder = encoder;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.meetingRepository = meetingRepository;
        this.classRepository = classRepository;
        this.enrollmentRepository = enrollmentRepository;

    }


    @Transactional
    public Meeting scheduleNewMeeting(final SystemUser systemUser, final Calendar data, final MeetingDuration duration, final LocalTime time, final List<MeetingParticipant> participantList) {
        Meeting newMeeting = new Meeting(systemUser, data, time, duration, participantList);
        return meetingRepository.save(newMeeting);
    }

    public List<MeetingParticipant> availableMeetingParticipant(String selection, List<SystemUser> list, final Calendar data, final LocalTime time) {
        List<SystemUser> participantList = setParticipantList(selection, list);
        List<MeetingParticipant> availableParticipant = new ArrayList<>();

        if (!(meetingRepository.allMeetings(data, time).isEmpty()) || !(classRepository.allClass(data, time).isEmpty())) {

            for (SystemUser u : participantList) {
                Iterable<Course> course = enrollmentRepository.findByAcceptedStatusAndSystemUser(u);
                List<Course> courseList = (List<Course>) course;
                MeetingParticipant meetingParticipant = new MeetingParticipant(u, InviteStatus.UNKNOWN);

                if (!(meetingRepository.findByDateAndUser(u, data, time)) && !(classRepository.findByDateAndSystemUserExtraClass(u, data, time))) {

                    availableParticipant.add(meetingParticipant);

                    for (int i = 0; i < courseList.size(); i++) {

                        if (!(classRepository.findBycourseAndSystemUserinClasses(u, courseList.get(i), data, time))) {
                            if ((availableParticipant.contains(meetingParticipant))) {
                                availableParticipant.remove(meetingParticipant);
                            }
                        } else if (!(availableParticipant.contains(meetingParticipant))) {
                            availableParticipant.add(meetingParticipant);
                        }
                    }
                }
            }
        } else {
            for (SystemUser systemUser : participantList) {
                availableParticipant.add(new MeetingParticipant(systemUser, InviteStatus.UNKNOWN));
            }

        }
        meetingRepository.saveMeetingParticipant(availableParticipant);
        return availableParticipant;
    }

    public Iterable<Meeting> allMeetings() {
        return meetingRepository.findAll();
    }


    public Iterable<Meeting> allMeetingsByOwner(SystemUser systemUser) {
        return meetingRepository.allMeetingsByOwner(systemUser);
    }



    public List<SystemUser> setParticipantList(String selection, List<SystemUser> list) {
        String[] selectionFromPrompt = selection.split(" ");

        int[] numbers = new int[selectionFromPrompt.length];
        for (int i = 0; i < selectionFromPrompt.length; i++) {
            numbers[i] = Integer.parseInt(selectionFromPrompt[i]);
        }
        List<SystemUser> selectedUsers = new ArrayList<>();
        for (int index : numbers
        ) {
            selectedUsers.add(list.get(index - 1));
        }

        return selectedUsers;
    }

    public Iterable<SystemUser> allUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public Meeting cancelMeeting(Meeting meeting) {
        meeting.cancel(CurrentTimeCalendars.now());
        return meetingRepository.save(meeting);
    }

    public Iterable<Meeting> allMeetingsByUser(SystemUser authenticatedUser) {
        return meetingRepository.findMeetingsByUser(authenticatedUser);
    }

    public Iterable<MeetingParticipant> allParticipantsByMeeting(MeetingToken identity) {
        return meetingRepository.findParticipantsByMeeting(identity);
    }
}

