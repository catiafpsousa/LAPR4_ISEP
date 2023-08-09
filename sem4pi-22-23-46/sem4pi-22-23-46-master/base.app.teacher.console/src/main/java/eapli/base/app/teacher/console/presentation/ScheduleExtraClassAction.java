package eapli.base.app.teacher.console.presentation;
import eapli.framework.actions.Action;

public class ScheduleExtraClassAction implements Action{
    @Override
    public boolean execute() {
        return new ScheduleExtraClassUI().doShow();
    }

}
