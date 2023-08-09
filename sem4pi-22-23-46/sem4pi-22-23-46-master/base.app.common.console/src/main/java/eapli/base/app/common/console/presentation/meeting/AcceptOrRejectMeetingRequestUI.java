package eapli.base.app.common.console.presentation.meeting;

import eapli.base.meetingmanagement.application.AcceptOrRejectMeetingRequestController;
import eapli.base.meetingmanagement.domain.InviteStatus;
import eapli.base.meetingmanagement.domain.Meeting;
import eapli.base.util.UiUtils;
import eapli.framework.presentation.console.AbstractUI;

import java.util.ArrayList;
import java.util.List;



public class AcceptOrRejectMeetingRequestUI extends AbstractUI {
    private static final int ACCEPT =1;
    private static final int REJECT =2;
    private final AcceptOrRejectMeetingRequestController controller = new AcceptOrRejectMeetingRequestController();

    @Override
    protected boolean doShow() {
        try {
            final Iterable<Meeting> meetings = controller.findPendingInvitations();
            if(!meetings.iterator().hasNext()){
                System.out.println("You have no pending meeting requests");
            } else {
                final Meeting meeting = (Meeting) UiUtils.showAndSelectOne((List) meetings, "Select a meeting to respond invitation");

                boolean validOption = false;
                while (!validOption) {
                    final int choice = UiUtils.readIntegerFromConsole("Enter 1 to ACCEPT or 2 to REJECT the meeting request");
                    if (choice == ACCEPT) {
                        controller.acceptOrRejectMeetingRequest(meeting, InviteStatus.ACCEPT);
                        validOption = true;
                    } else if (choice == REJECT) {
                        controller.acceptOrRejectMeetingRequest(meeting, InviteStatus.REJECT);
                        validOption = true;
                    }
                }
                System.out.println("Accepted/reject meeting request successfully");
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            System.out.println("Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");
        }
        return false;
    }

    @Override
    public String headline() {
        return "Accept or Reject Meeting Request";
    }
}
