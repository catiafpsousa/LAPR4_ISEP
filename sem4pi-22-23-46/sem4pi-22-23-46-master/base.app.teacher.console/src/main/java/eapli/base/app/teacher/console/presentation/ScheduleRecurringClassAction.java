package eapli.base.app.teacher.console.presentation;

import eapli.framework.actions.Action;

public class ScheduleRecurringClassAction implements Action {
    @Override
    public boolean execute() {
        return new ScheduleRecurringClassUI().doShow();
    }
}
