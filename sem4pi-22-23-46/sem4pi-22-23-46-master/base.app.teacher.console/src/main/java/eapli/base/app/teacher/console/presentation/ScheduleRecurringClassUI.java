package eapli.base.app.teacher.console.presentation;

import eapli.base.classmanagement.application.ScheduleRecurringClassController;
import eapli.base.classmanagement.domain.RecurringClass;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.util.UiUtils;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

public class ScheduleRecurringClassUI extends AbstractUI {

    private final ScheduleRecurringClassController ctrl = new ScheduleRecurringClassController();


    @Override
    protected boolean doShow() {
        final Iterable<Course> listOfCourses = this.ctrl.listCourses();
        if (!listOfCourses.iterator().hasNext()) {
            System.out.println("No courses found, unable to schedule any classes.");
        } else {
            try {
                final Course course = (Course) UiUtils.showAndSelectOne((List) listOfCourses,"Select course");
                final String title = Console.readLine("Enter title of the recurring class");
                final String inputDate = Console.readLine("Enter intended date (DD/MM/YYYY)");
                final Calendar date = UiUtils.selectDate(inputDate);

                final String inputTime = Console.readLine("Enter intended hour (HH:MM)");
                final LocalTime time = UiUtils.selectTime(inputTime);

                final int duration = Console.readInteger("Enter class duration in minutes");

                //Check Availability
                if (!ctrl.checkAvailability(date, time, duration)) {
                    System.out.println("!!!The time and date chosen are overlapping with an already scheduled class!!!");
                    return false;
                }

                final RecurringClass recurringClass = this.ctrl.scheduleRecurringClass(title,duration,date, time, course);
                System.out.println("**Recurring Class scheduled successfully**");
                System.out.println(recurringClass);

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println(
                        "Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");
            }
        }

        return false;
    }


    @Override
    public String headline() {
        return "Schedule a recurring Class";
    }
}
