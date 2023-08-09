package eapli.base.app.student.console.presentation;

import eapli.framework.actions.Action;

public class ListFutureExamsAction implements Action {
    @Override
    public boolean execute() {
        return new ListFutureExamsUI().show();
    }
}
