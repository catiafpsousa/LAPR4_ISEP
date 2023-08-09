package eapli.base.app.common.console.presentation.meeting;

import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.meetingmanagement.domain.Meeting;
import eapli.framework.visitor.Visitor;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.framework.visitor.Visitor;

import java.text.SimpleDateFormat;

public class MeetingPrinter implements Visitor<Meeting> {


    @Override
    public void visit(final Meeting visitee) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(visitee.date().getTime());
        System.out.printf("\n Meeting:\n");
        System.out.printf("Date: %s\n", formattedDate);
        System.out.printf("Hour: %s\n",visitee.timestart() );
        System.out.printf("Duration: %d min\n", visitee.duration() );
        System.out.printf("Meeting Status: %s\n", visitee.meetingStatus());
        System.out.printf("Participant List:\n");
        for (int i = 0; i < visitee.list().size(); i++) {
            System.out.printf("Email: %-20s Full Name: %-20s\n", visitee.list().get(i).list().identity(), visitee.list().get(i).list().fullName());
        }

    }


}

