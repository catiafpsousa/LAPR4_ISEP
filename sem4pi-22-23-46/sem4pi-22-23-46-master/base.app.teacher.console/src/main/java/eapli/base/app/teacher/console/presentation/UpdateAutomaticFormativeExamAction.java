package eapli.base.app.teacher.console.presentation;
import eapli.framework.actions.Action;

public class UpdateAutomaticFormativeExamAction implements Action {

    @Override
    public boolean execute() {
        return new UpdateAutomaticFormativeExamUI().doShow();
    }
}
