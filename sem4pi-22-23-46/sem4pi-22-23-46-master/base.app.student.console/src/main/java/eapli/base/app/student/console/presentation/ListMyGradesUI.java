package eapli.base.app.student.console.presentation;

import eapli.base.grademanagement.application.ListMyGradesController;
import eapli.base.grademanagement.domain.ExamGrade;
import eapli.framework.presentation.console.AbstractUI;

import java.util.ArrayList;
import java.util.List;

public class ListMyGradesUI extends AbstractUI {

    private ListMyGradesController controller = new ListMyGradesController();

    @Override
    protected boolean doShow() {

        final List<ExamGrade> grades = new ArrayList<>();


        final Iterable<ExamGrade> iterable = controller.allGradesByStudent();
        System.out.println("Grades:");
        if (!iterable.iterator().hasNext()) {
            System.out.println("There are no grades available!");
        } else {
            int cont2 = 1;
            System.out.printf("%-6s%-30s%-10s%-10s%n", "No.:", "EXAM", "GRADE", "DATE");
            for (final ExamGrade grade : iterable) {
                grades.add(grade);

                System.out.printf("%-6s%-30s%-10s%-10s%n", cont2, grade.exam().title(), grade.score(), grade.date().getTime());
                cont2++;
            }
        }


        return false;
    }

    @Override
    public String headline() {
        return "List My Grades";
    }

}
