package eapli.base.app.teacher.console.presentation;

import eapli.framework.actions.Action;

public class CreateAutomaticFormativeExamAction implements Action {
    @Override
    public boolean execute() {
        return new CreateAutomaticFormativeExamUI().doShow();
    }
}
