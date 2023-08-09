package eapli.base.app.common.console.presentation.meeting;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.exammanagement.application.ListFutureExamsController;
import eapli.base.exammanagement.domain.AutomaticFormativeExam;
import eapli.base.exammanagement.domain.Exam;
import eapli.base.meetingmanagement.application.ListParticipantsInMeetingController;
import eapli.base.meetingmanagement.domain.Meeting;
import eapli.base.meetingmanagement.domain.MeetingParticipant;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ListParticipantsInMeetingUI extends AbstractUI {

    private ListParticipantsInMeetingController controller = new ListParticipantsInMeetingController();
    private static final Logger LOGGER = LoggerFactory.getLogger(ListParticipantsInMeetingUI.class);

    @Override
    protected boolean doShow() {
        final List<Meeting> meetings = new ArrayList<>();
        final Iterable<Meeting> iterable = this.controller.allMeetingsByUser();
        if(!iterable.iterator().hasNext()){
            System.out.println("There are no meetings available");
        }else{
            int cont = 1;
            System.out.println("SELECT Meeting to list Participants\n");
            System.out.printf("%-6s%-20s%-20s%-30s%-30s%n", "No.:", "DATE", "DURATION", "START_TIME", "END_TIME");
            for(final Meeting meeting : iterable) {
                meetings.add(meeting);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = dateFormat.format(meeting.date().getTime());
                System.out.printf("%-6d%-20s%-20s%-30s%-30s%n", cont, formattedDate, meeting.duration(), meeting.timestart(), meeting.timeend());
                cont++;
            }
            final int option = Console.readInteger("Choose one meeting to list participants or 0 to finish:");
            if(option == 0){
                System.out.println("No meeting selected");
            }else{
                try {
                    final List<MeetingParticipant> participants = new ArrayList<>();
                    final Iterable<MeetingParticipant> iterable3 = controller.allParticipantsByMeeting(meetings.get(option-1).identity());
                    System.out.println("Participants:");
                    if (!iterable3.iterator().hasNext()) {
                        System.out.println("There are no participants for the selected meeting!");
                    }else{
                        int cont2 = 1;
                        System.out.printf("%-6s%-30s%-40s%-20s%n", "No.:", "NAME", "EMAIL", "STATUS");
                        for(final MeetingParticipant participant : iterable3) {
                            participants.add(participant);
                            System.out.printf("%-6d%-30s%-40s%-20s%n", cont2, participant.list().shortName(), participant.list().identity(), participant.getInviteStatus());
                            cont2++;
                            }
                    }
                } catch(IntegrityViolationException | ConcurrencyException e){
                    LOGGER.error("Error performing the operation.", e);
                    System.out.println("Something went wrong.");
                }
            }
        }
        return false;
    }

    @Override
    public String headline() {
        return "List Participants in Meeting";
    }
}
