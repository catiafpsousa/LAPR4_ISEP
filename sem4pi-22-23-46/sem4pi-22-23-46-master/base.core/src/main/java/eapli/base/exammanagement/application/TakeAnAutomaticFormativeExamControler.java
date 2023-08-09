package eapli.base.exammanagement.application;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.enrollmentmanagement.application.EnrollmentManagementService;
import eapli.base.exammanagement.domain.AutomaticFormativeExam;
import eapli.base.exammanagement.domain.Exam;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.usermanagement.domain.EcourseRoles;
import eapli.base.utils.ExamFileManipulation;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;

public class TakeAnAutomaticFormativeExamControler {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final ExamManagementService examSvc = AuthzRegistry.examService();
    private final EnrollmentManagementService enrollmentManagementSvc = AuthzRegistry.enrollmentService();

    public void saveExamToBeAnswer(String examContent, String path) throws IOException {
        String generateExam = examSvc.generateAutomaticExam(examContent);
        String examToBeAnswered = examSvc.convertExamToExamToBeAnswered(generateExam);
        ExamFileManipulation.saveStringToTxtFileInPath(examToBeAnswered, path);
    }

    public String takeAnAutomaticFormativeExam(String examContent, String answerPath) throws IOException, ParseException {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        String generateExam = examSvc.generateAutomaticExam(examContent);
        Double grade = examSvc.getGradeSubmitedExam(generateExam, answerPath);
        return examSvc.generateFeedback(generateExam, grade);
    }


    public Iterable<Course> allCoursesByStudent() {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        Iterable<Course> courseList = enrollmentManagementSvc.findAllCourseByStudent(authz.session().get().authenticatedUser());
        return courseList;
    }

    public Iterable<AutomaticFormativeExam> allExamsByCourse(CourseCode courseCode) {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.STUDENT);
        Iterable<AutomaticFormativeExam> examList = examSvc.findFormativeExamsByCourse(courseCode);
        return examList;
    }


}
