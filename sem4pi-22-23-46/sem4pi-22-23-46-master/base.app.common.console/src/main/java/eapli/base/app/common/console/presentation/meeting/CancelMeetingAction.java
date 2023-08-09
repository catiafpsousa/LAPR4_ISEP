package eapli.base.app.common.console.presentation.meeting;

import eapli.framework.actions.Action;

public class CancelMeetingAction implements Action {

    @Override
    public boolean execute() {
        return new CancelMeetingUI().doShow();
    }
}
