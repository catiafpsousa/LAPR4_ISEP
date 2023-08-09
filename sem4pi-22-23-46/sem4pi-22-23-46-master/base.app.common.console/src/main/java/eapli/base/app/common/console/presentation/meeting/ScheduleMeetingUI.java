/*
 * Copyright (c) 2013-2023 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package eapli.base.app.common.console.presentation.meeting;

import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.meetingmanagement.application.ScheduleMeetingController;
import eapli.base.meetingmanagement.domain.Meeting;
import eapli.base.meetingmanagement.domain.MeetingDuration;
import eapli.base.meetingmanagement.domain.MeetingParticipant;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */
public class ScheduleMeetingUI extends AbstractUI {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleMeetingUI.class);

    private final ScheduleMeetingController theController = new ScheduleMeetingController();

    final SystemUser systemUser = theController.systemUser();
    @Override
    protected boolean doShow() {

        final String inputDate = Console.readLine("Enter intended date (DD/MM/YYYY)");
        final Calendar date = selectDate(inputDate);
        final String inputTime = Console.readLine("Enter intended hour (HH:MM)");
        final LocalTime time = selectTime(inputTime);


        final int durationTime = Console.readInteger("Duration: (min)");
        final MeetingDuration duration = MeetingDuration.valueOf( durationTime);

        final List<SystemUser> list = new ArrayList<>();
        final Iterable<SystemUser> systemUserIterable = this.theController.allUsers();
        if (!systemUserIterable.iterator().hasNext()) {
            System.out.println("There is no registered User");
        } else {
            int cont = 1;
            System.out.println("SELECT User to the Meeting\n");
            // FIXME use select widget, see, ChangeDishTypeUI
            System.out.printf("%-50s%-60s%-20s%n", "Email", "FullName", "ShortName");
            for (final SystemUser user : systemUserIterable) {
                list.add(user);
                System.out.printf("%-6d%-50s%-60s%-20s%n", cont, user.identity(), user.fullName(),
                        user.shortName());
                cont++;
            }

            final String selection = Console.readLine("\nEnter selection: ");
            if (selection == "") {
                System.out.println("No Users selected");
            } else {
                try {
                   List<MeetingParticipant> print= this.theController.availableMeetingParticipant(selection,list,date,time, duration);
                    if (print.isEmpty()){
                        System.out.println("There's no participant available!");
                    }else if (print.size()==1) {
                        System.out.println("There's only one participant available!");
                    }else{
                        System.out.println("List of available Participants:");
                        for (int i = 0; i < print.size(); i++) {
                            System.out.println( print.get(i).list().identity());
                        }
                        Meeting meeting =  this.theController.scheduleMeeting(systemUser,print, date,time, duration);
                        MeetingPrinter printer = new MeetingPrinter();
                        printer.visit(meeting);
                        System.out.println("**Meeting Successfully Schedule**");
                    }

                } catch (IntegrityViolationException | ConcurrencyException ex) {
                    LOGGER.error("Error performing the operation", ex);
                    System.out.println(
                            "Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");

                }

            }
        }


        return false;

    }


    public final Calendar selectDate(String inputDate){
        final String[] dateParts = inputDate.split("/");
        final int day = Integer.parseInt(dateParts[0]);
        final int month = Integer.parseInt(dateParts[1]) - 1;  // Months in Calendar class are zero-based
        final int year = Integer.parseInt(dateParts[2]);

        Calendar date = Calendar.getInstance();
        date.set(year, month, day);
        return date;
    }
    public final LocalTime selectTime(String inputTime){
        final String[] timeParts = inputTime.split(":");
        final int hour = Integer.parseInt(timeParts[0]);
        final int minute = Integer.parseInt(timeParts[1]);
        return LocalTime.of(hour, minute);
    }

    @Override
    public String headline() {
        return "SCHEDULE MEETING";
    }

}
