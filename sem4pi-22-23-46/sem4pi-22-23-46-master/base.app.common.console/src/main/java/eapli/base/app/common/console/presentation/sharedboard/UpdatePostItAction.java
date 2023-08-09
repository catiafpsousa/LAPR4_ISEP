package eapli.base.app.common.console.presentation.sharedboard;

import eapli.framework.actions.Action;

public class UpdatePostItAction implements Action {
    @Override
    public boolean execute() {
        return new UpdatePostItUI().doShow();
    }
}
