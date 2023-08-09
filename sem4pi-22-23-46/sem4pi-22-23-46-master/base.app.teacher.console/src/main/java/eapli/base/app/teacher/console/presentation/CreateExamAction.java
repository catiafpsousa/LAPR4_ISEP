package eapli.base.app.teacher.console.presentation;

import eapli.framework.actions.Action;

public class CreateExamAction implements Action {
    @Override
    public boolean execute() {
        return new CreateExamUI().doShow();
    }
}