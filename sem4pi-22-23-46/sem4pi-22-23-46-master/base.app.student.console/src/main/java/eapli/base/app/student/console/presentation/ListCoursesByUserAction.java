package eapli.base.app.student.console.presentation;

import eapli.framework.actions.Action;

public class ListCoursesByUserAction implements Action {
    @Override
    public boolean execute() {
        return new ListCoursesByUserUI().show();
    }
}
