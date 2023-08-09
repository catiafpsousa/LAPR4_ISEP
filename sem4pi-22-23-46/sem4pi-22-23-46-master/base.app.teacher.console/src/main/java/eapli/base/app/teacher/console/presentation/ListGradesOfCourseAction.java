package eapli.base.app.teacher.console.presentation;
import eapli.framework.actions.Action;

public class ListGradesOfCourseAction implements Action {
    @Override
    public boolean execute() {
        return new ListGradesOfCourseUI().show();
    }

}
