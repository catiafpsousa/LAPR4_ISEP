package eapli.base.app.teacher.console.presentation;

import eapli.base.classmanagement.application.UpdateClassScheduleController;
import eapli.base.classmanagement.domain.RecurringClass;
import eapli.base.util.UiUtils;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;

import java.text.DateFormatSymbols;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

public class UpdateClassScheduleUI extends AbstractUI {

    private final UpdateClassScheduleController ctrl = new UpdateClassScheduleController();


    @Override
    protected boolean doShow() {
        final Iterable<RecurringClass> listOfRecurringClasses = this.ctrl.listRecurringClasses();
        if (!listOfRecurringClasses.iterator().hasNext()) {
            System.out.println("No courses found, unable to schedule any classes.");
        } else {
            try {
                final RecurringClass chosenClass = (RecurringClass) UiUtils.showAndSelectOne((List) listOfRecurringClasses, "Select the recurring class");


                boolean isValidDate;
                Calendar classDateToChange;

                String[] dayNames = new DateFormatSymbols().getWeekdays();
                String weekDayOfClass = dayNames[chosenClass.day().get(Calendar.DAY_OF_WEEK)];
                System.out.println("[This class takes place every " + weekDayOfClass + "]");

                do {
                    String inputDate = Console.readLine("Enter the date of the class to be rescheduled (DD/MM/YYYY)\nEnter 0 to exit");

                    if (inputDate.trim().equals("0")) return false;

                    classDateToChange = UiUtils.selectDate(inputDate);
                    isValidDate = chosenClass.day().get(Calendar.DAY_OF_WEEK) == classDateToChange.get(Calendar.DAY_OF_WEEK);
                    if (!isValidDate){
                        System.out.println("The date chosen should be on a " + weekDayOfClass);
                    }
                } while (!isValidDate);

                final String inputDate = Console.readLine("Enter the new date of the selected class (DD/MM/YYYY)");
                final Calendar newClassDate =UiUtils.selectDate(inputDate);
                final String inputTime = Console.readLine("Enter intended hour (HH:MM)");
                final LocalTime time = UiUtils.selectTime(inputTime);
                final int duration = Console.readInteger("Enter class duration in minutes");

                if (!ctrl.checkAvailability(newClassDate, time, duration)) {
                    System.out.println("!!!The time and date chosen are overlapping with an already scheduled class!!!");
                    return false;
                }

                try {
                    ctrl.updateClassSchedule(chosenClass, classDateToChange, newClassDate, time, duration);
                } catch (IntegrityViolationException | ConcurrencyException ex) {
                    System.out.println("!!!Error rescheduling class!!!");
                }

                System.out.println("**Class schedule updated successfully**");

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println("Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");
            }
        }
        return false;
    }

    @Override
    public String headline() {
        return "Update the schedule of a class";
    }
}
