package eapli.base.app.student.console.presentation;

import eapli.framework.actions.Action;

public class ListMyGradesAction implements Action {
    @Override
    public boolean execute() {
        return new ListMyGradesUI().show();
    }
}
