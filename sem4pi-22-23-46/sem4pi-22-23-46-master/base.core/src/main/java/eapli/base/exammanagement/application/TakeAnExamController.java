package eapli.base.exammanagement.application;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.enrollmentmanagement.application.EnrollmentManagementService;
import eapli.base.exammanagement.domain.Exam;
import eapli.base.grademanagement.application.GradeManagementService;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.usermanagement.domain.EcourseRoles;
import eapli.base.utils.ExamFileManipulation;
import org.antlr.v4.runtime.CharStreams;

import java.io.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.LinkedList;

public class TakeAnExamController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final ExamManagementService examSvc = AuthzRegistry.examService();
    private final EnrollmentManagementService enrollmentManagementSvc = AuthzRegistry.enrollmentService();

    private final GradeManagementService gradeSvc = AuthzRegistry.gradeService();

    public Iterable<Course> allCoursesByStudent() {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        Iterable<Course> courseList = enrollmentManagementSvc.findAllCourseByStudent(authz.session().get().authenticatedUser());
        return courseList;
    }

    public Iterable<Exam> allExamsByCourseAndDate(CourseCode courseCode, Calendar calendar) {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        Iterable<Exam> examList = examSvc.findExamsByCourseAndDate(courseCode, calendar);
        return examList;
    }

    public void saveExamToBeAnsweredOnPath(String selectedExam, String filePath) throws IOException {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        String examToBeAnswered = examSvc.convertExamToExamToBeAnswered(selectedExam);
        ExamFileManipulation.saveStringToTxtFileInPath(examToBeAnswered, filePath);
    }

    public String gradeSubmitedExam(Exam examWithSolutions, String filePathAnsweredExam) throws IOException {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        Double examGrade = examSvc.getGradeSubmitedExam(examWithSolutions.examContent(), filePathAnsweredExam);
        SystemUser systemUser = authz.session().get().authenticatedUser();
        gradeSvc.saveGrade(systemUser, examWithSolutions, examGrade);
        return examSvc.generateFeedback(examWithSolutions.examContent(), examGrade);
    }
}
