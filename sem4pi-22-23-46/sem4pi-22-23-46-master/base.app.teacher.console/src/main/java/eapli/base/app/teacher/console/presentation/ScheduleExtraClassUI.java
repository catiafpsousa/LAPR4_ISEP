package eapli.base.app.teacher.console.presentation;

import eapli.base.classmanagement.application.ScheduleExtraClassController;
import eapli.base.classmanagement.domain.ExtraordinaryClass;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.util.UiUtils;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ScheduleExtraClassUI extends AbstractUI {
    private final ScheduleExtraClassController ctrl=new ScheduleExtraClassController();

    @Override
    protected boolean doShow() {
        final Iterable<Course> iterable = this.ctrl.listCourses();
        if (!iterable.iterator().hasNext()) {
            System.out.println("No courses found, unable to schedule any classes.");
        } else {
            try {
                final Course course = (Course) UiUtils.showAndSelectOne((List) iterable,"Select course");
                final String title = Console.readLine("Enter title of extraordinary class");
                final String inputDate = Console.readLine("Enter intended date (DD/MM/YYYY)");
                final Calendar date =UiUtils.selectDate(inputDate);
                final String inputTime = Console.readLine("Enter intended hour (HH:MM)");
                final LocalTime time = UiUtils.selectTime(inputTime);
                final int duration = Console.readInteger("Enter class duration in minutes");

                //Check Availability
                if (!ctrl.checkAvailability(date, time, duration)) {
                    System.out.println("The time and date chosen are overlapping with an already scheduled class!");
                    return false;
                }

                final Iterable<Student> students = this.ctrl.listStudents(course);
                UiUtils.showList((List) students, "Select several students (SEPARATED BY SPACES):");
                final String selection=Console.readLine("Enter selection");
                List <Student> participants = selectParticipants(selection,(List) students);
                final ExtraordinaryClass classExtra = this.ctrl.schedulesExtraClass(title,duration,date, time,participants, course);
                System.out.println("**Schedules extraordinary class successfully**");
                System.out.println(classExtra.toString());
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                System.out.println(
                        "Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");
            }
        }

        return false;
    }

    public List <Student> selectParticipants(String selection, List<Student> allStudents) {
        String[] selectionFromPrompt = selection.split(" ");

        int[] numbers = new int[selectionFromPrompt.length];
        for (int i = 0; i < selectionFromPrompt.length; i++) {
            numbers[i] = Integer.parseInt(selectionFromPrompt[i]);
        }
        List<Student> selectedStudents = new ArrayList<>();
        for (int index : numbers
        ) {
            selectedStudents.add(allStudents.get(index - 1));
        }
        return selectedStudents;
    }


    @Override
    public String headline() {
        return "Schedule Extraordinary Class";
    }
}


