package eapli.base.app.common.console.presentation.sharedboard;

import eapli.base.app.common.console.presentation.sharedboard.CreateBoardUI;
import eapli.framework.actions.Action;

public class CreateBoardAction implements Action {
    @Override
    public boolean execute() {
        return new CreateBoardUI().doShow();
    }
}
