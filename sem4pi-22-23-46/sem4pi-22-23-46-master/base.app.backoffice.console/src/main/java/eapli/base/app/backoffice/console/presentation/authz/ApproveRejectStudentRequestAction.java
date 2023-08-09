package eapli.base.app.backoffice.console.presentation.authz;
import eapli.framework.actions.Action;

/**
 *
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */

public class ApproveRejectStudentRequestAction implements Action{
    @Override
    public boolean execute() {
        return new ApproveRejectStudentRequestUI().show();
    }
}
