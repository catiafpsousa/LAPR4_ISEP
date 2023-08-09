package eapli.base.app.student.console.presentation;

import eapli.base.exammanagement.domain.Exam;
import eapli.framework.visitor.Visitor;

public class FutureExamsPrinter implements Visitor<Exam> {

    @Override
    public void visit(final Exam visitee) {
        System.out.printf("%-20s%%n", visitee.title());
    }
}
