package eapli.base.app.teacher.console.presentation;
import eapli.framework.actions.Action;

public class AddExamQuestionAction implements Action {
    @Override
    public boolean execute() {
        return new AddExamQuestionsUI().show();
    }
}
