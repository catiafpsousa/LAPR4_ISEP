package eapli.base.app.common.console.presentation.sharedboard;

import eapli.framework.actions.Action;

public class UndoPostItLastChangeAction implements Action {
    @Override
    public boolean execute() {
        return new UndoPostItLastChangeUI().doShow();
    }
}
