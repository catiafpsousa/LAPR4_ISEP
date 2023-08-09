package eapli.base.app.common.console.presentation.meeting;

import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.meetingmanagement.application.CancelMeetingController;
import eapli.base.meetingmanagement.domain.Meeting;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CancelMeetingUI extends AbstractUI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleMeetingUI.class);

    private final CancelMeetingController theController = new CancelMeetingController();

    final SystemUser systemUser = theController.systemUser();

    @Override
    protected boolean doShow() {

        final List<Meeting> list = new ArrayList<>();
        final Iterable<Meeting> meetingsIterable = this.theController.allMeetingsByOwner(systemUser);
        if (!meetingsIterable.iterator().hasNext()) {
            System.out.println("There is no registered Meeting");
        } else {
            int cont = 1;
            System.out.println("SELECT Meeting to Cancel\n");
            // IMPRIMIR MEETING


            for (final Meeting meeting : meetingsIterable) {
                System.out.printf("%-2d.", cont);
                MeetingPrinter printer = new MeetingPrinter();
                printer.visit(meeting);
                list.add(meeting);
                cont++;
            }

            final Integer selection = Console.readInteger("\nEnter selection: ");
            if (selection == null) {
                System.out.println("No Meeting selected");
            } else {
                try {

                    this.theController.cancelMeeting(list.get(selection - 1));
                    System.out.println("**Meeting Successfully Cancel**");
                } catch (IntegrityViolationException | ConcurrencyException ex) {
                    LOGGER.error("Error performing the operation", ex);
                    System.out.println(
                            "Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");

                }

            }
        }
        return false;

    }


    @Override
    public String headline() {
        return "CANCEL MEETING";
    }
}
