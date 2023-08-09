package eapli.base.app.student.console.presentation;

import eapli.framework.actions.Action;

public class RequestEnrollmentAction implements Action {
    @Override
    public boolean execute() {
        return new RequestEnrollmentUI().show();
    }


}
