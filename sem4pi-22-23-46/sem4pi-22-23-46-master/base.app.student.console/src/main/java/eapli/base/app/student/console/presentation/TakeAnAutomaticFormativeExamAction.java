package eapli.base.app.student.console.presentation;

import eapli.framework.actions.Action;
public class TakeAnAutomaticFormativeExamAction implements Action {
    @Override
    public boolean execute() {
        return new TakeAnAutomaticFormativeExamUI().show();
    }

}
