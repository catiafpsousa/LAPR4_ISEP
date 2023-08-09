package eapli.base.app.student.console.presentation;


import eapli.framework.actions.Action;

public class TakeAnExamAction implements Action {
    public boolean execute() {
        return new TakeAnExamUI().show();
    }
}
