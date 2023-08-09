package eapli.base.app.common.console.presentation.meeting;

import eapli.base.app.common.console.presentation.meeting.ScheduleMeetingUI;
import eapli.framework.actions.Action;

public class ScheduleMeetingAction implements Action {


    @Override
    public boolean execute() {
        return new ScheduleMeetingUI().doShow();
    }
}
