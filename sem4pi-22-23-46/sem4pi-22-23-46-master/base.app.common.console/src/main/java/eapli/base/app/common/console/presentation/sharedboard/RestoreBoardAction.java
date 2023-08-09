package eapli.base.app.common.console.presentation.sharedboard;

import eapli.framework.actions.Action;

public class RestoreBoardAction implements Action {
    @Override
    public boolean execute() {
        return new RestoreBoardUI().doShow();
    }}

