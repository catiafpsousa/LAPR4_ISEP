package eapli.base.app.common.console.presentation.meeting;

import eapli.framework.actions.Action;

public class AcceptOrRejectMeetingRequestAction implements Action {

    @Override
    public boolean execute() {
        return new AcceptOrRejectMeetingRequestUI().doShow();
    }
}
