package eapli.base.app.common.console.presentation.sharedboard;

import eapli.framework.actions.Action;

public class ViewBoardAction implements Action {
    @Override
    public boolean execute() {
        return new ViewBoardUI().doShow();
    }
}
