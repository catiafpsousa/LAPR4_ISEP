package eapli.base.app.backoffice.console.presentation.authz;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.framework.visitor.Visitor;

public class CoursePrinter implements Visitor<Course> {

    @Override
    public void visit(final Course visitee) {
        if (!visitee.teacherInCharge().isPresent()){
            System.out.printf("%-15s%-35s%-15s%-30s%-10s", visitee.identity(), visitee.courseName(), visitee.courseTeachers().size(), null, visitee.courseState());
        }else{
            System.out.printf("%-15s%-35s%-15s%-30s%-10s", visitee.identity(), visitee.courseName(), visitee.courseTeachers().size(), visitee.teacherInCharge().get().identity(), visitee.courseState());
        }
    }
}
