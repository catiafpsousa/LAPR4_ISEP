package eapli.base.app.teacher.console.presentation;

import eapli.framework.actions.Action;

public class UpdateExamAction implements Action {

    @Override
    public boolean execute() {
        return new UpdateExamUI().doShow();
    }
}
