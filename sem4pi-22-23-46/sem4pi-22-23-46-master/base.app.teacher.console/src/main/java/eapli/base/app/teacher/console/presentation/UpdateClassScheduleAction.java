package eapli.base.app.teacher.console.presentation;

import eapli.framework.actions.Action;

public class UpdateClassScheduleAction implements Action {
    @Override
    public boolean execute() {
        return new UpdateClassScheduleUI().doShow();
    }
}
