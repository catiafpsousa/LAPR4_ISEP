package eapli.base.app.student.console.presentation;
import eapli.framework.actions.Action;


public class ViewMyEnrollmentRequestsAction implements Action {
    @Override
    public boolean execute() {
        return new ViewMyEnrollmentRequestsUI().show();
    }
}