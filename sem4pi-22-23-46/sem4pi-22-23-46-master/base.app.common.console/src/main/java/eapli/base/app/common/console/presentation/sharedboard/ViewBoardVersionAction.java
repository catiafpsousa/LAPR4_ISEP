package eapli.base.app.common.console.presentation.sharedboard;

import eapli.framework.actions.Action;

public class ViewBoardVersionAction implements Action {
    @Override
    public boolean execute() {
        return new ViewBoardVersionUI().doShow();
    }
}
