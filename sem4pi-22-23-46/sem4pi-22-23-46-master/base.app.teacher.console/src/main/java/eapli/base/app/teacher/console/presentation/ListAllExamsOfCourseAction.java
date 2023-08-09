package eapli.base.app.teacher.console.presentation;

import eapli.framework.actions.Action;

public class ListAllExamsOfCourseAction implements Action {
    @Override
    public boolean execute() {
        return new ListAllExamsOfCourseUI().show();
    }
}
