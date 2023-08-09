package eapli.base.exammanagement.application;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.coursemanagement.repositories.CourseRepository;
import eapli.base.exammanagement.*;
import eapli.base.exammanagement.application.antlr.AutomaticExamGenerateQuestionsVisitor;
import eapli.base.exammanagement.application.antlr.ConvertExamIntoSolutionsListener;
import eapli.base.exammanagement.application.antlr.ConvertIntoExamReadyToBeAnsweredVisitor;
import eapli.base.exammanagement.application.antlr.GradeAnsweredExamVisitor;
import eapli.base.exammanagement.domain.AutomaticFormativeExam;
import eapli.base.exammanagement.domain.Exam;
import eapli.base.exammanagement.domain.ExamDates;
import eapli.base.exammanagement.repositories.ExamRepository;
import eapli.base.grademanagement.repositories.GradeRepository;
import eapli.base.questionmanagement.repositories.QuestionRepository;
import eapli.base.utils.ExamFileManipulation;
import eapli.framework.time.util.CurrentTimeCalendars;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class ExamManagementService {

    private final ExamRepository examRepository;

    private final QuestionRepository questionRepository;
    private final CourseRepository courseRepository;
    private final GradeRepository gradeRepository;

    public ExamManagementService(final ExamRepository examRepo, final QuestionRepository questionRepo, final CourseRepository courseRepo, final GradeRepository gradeRepo) {
        this.examRepository = examRepo;
        this.questionRepository = questionRepo;
        this.courseRepository = courseRepo;
        this.gradeRepository = gradeRepo;
    }

    public ExamManagementService(){
        this.examRepository = null;
        this.questionRepository = null;
        this.courseRepository = null;
        this.gradeRepository = null;
    }

    public void verifyExam(String filePath) throws IOException {
        ExamLexer lexer = new ExamLexer(CharStreams.fromFileName(filePath));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExamParser parser = new ExamParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());
        ParseTree tree = parser.prog_exam();
    }

    public Map<String, String> getListOfQuestionsFromFile(String filePath) throws IOException {
        ExamLexer lexer = new ExamLexer(CharStreams.fromFileName(filePath));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExamParser parser = new ExamParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());
        ParseTree tree = parser.prog_question();

        return ExamFileManipulation.getListOfQuestionsOnFile(filePath);
    }

    public void verifyAutomaticFormativeExam(String filePath) throws IOException {
        AutomaticExamLexer lexer = new AutomaticExamLexer(CharStreams.fromFileName(filePath));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AutomaticExamParser parser = new AutomaticExamParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());
        ParseTree tree = parser.examTitle();
    }


    public String generateAutomaticExam(String examContent) {
        AutomaticExamLexer lexer = new AutomaticExamLexer(CharStreams.fromString(examContent));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AutomaticExamParser parser = new AutomaticExamParser(tokens);
        ParseTree tree = parser.examTitle();
        AutomaticExamGenerateQuestionsVisitor genQuestionsVisitor = new AutomaticExamGenerateQuestionsVisitor();
        return genQuestionsVisitor.visit(tree);
    }


    public String convertExamToExamToBeAnswered(String examToBeConverted) {
        String examOnlyQuestions = examToBeConverted.substring(examToBeConverted.indexOf("[Sections]"));
        ExamLexer lexer = new ExamLexer(CharStreams.fromString(examOnlyQuestions));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExamParser parser = new ExamParser(tokens);
        ParseTree sectionsTree = parser.sectionsGroup();

        ConvertIntoExamReadyToBeAnsweredVisitor examToBeAnsweredVisitor = new ConvertIntoExamReadyToBeAnsweredVisitor();
        String questionsReadyToBeAnswered = examToBeAnsweredVisitor.visit(sectionsTree);
        String headerOfExam = examToBeConverted.substring(0, examToBeConverted.indexOf("[Sections"));
        return headerOfExam + questionsReadyToBeAnswered;
    }

    public Double getGradeSubmitedExam(String examWithSolutions, String filePathAnsweredExam) throws IOException {
        //Generate the list with the solutions to be passed into the visitor that validates the answerd exam
        String examOnlyQuestions = examWithSolutions.substring(examWithSolutions.indexOf("[Sections]"));
        ConvertExamIntoSolutionsListener convertedExam = getConvertedExamListenerResult(examOnlyQuestions);
        LinkedList<String> solutionsOfExam = convertedExam.getExamAnswers();

        //Validate the answerd exam with the solutions of the exam generated on the previous listener
        String answerOnlyQuestions = String.valueOf(CharStreams.fromFileName(filePathAnsweredExam));
        answerOnlyQuestions = answerOnlyQuestions.substring(answerOnlyQuestions.indexOf("[Sections]"));

        AnswersLexer answerd_lexer = new AnswersLexer(CharStreams.fromString(answerOnlyQuestions));
        CommonTokenStream answerd_tokens = new CommonTokenStream(answerd_lexer);
        AnswersParser answerd_parser = new AnswersParser(answerd_tokens);
        ParseTree answerd_tree = answerd_parser.sectionsGroup();
        GradeAnsweredExamVisitor gradeExamVisitor = new GradeAnsweredExamVisitor();
        gradeExamVisitor.setExamSolution(solutionsOfExam);
        Double studentGrade = gradeExamVisitor.visit(answerd_tree);
        for (String corrLine : gradeExamVisitor.getCorrectionPrint()) System.out.println(corrLine);
        return studentGrade;
    }

    public String generateFeedback(String examWithSolutions, Double examGrade) {
        // get total score from examWithSolutions
        String examOnlyQuestions = examWithSolutions.substring(examWithSolutions.indexOf("[Sections]"));
        ConvertExamIntoSolutionsListener convertedExam = getConvertedExamListenerResult(examOnlyQuestions);
        double maxGrade = convertedExam.getMaxGrade();

        String feedback = "Grade: " + examGrade + " of " + maxGrade + " maximum points.\nFeedback: ";
        if (examGrade > 2 * maxGrade / 3) {
            feedback = feedback + "Well done, Congratulations!";
        } else if (examGrade < maxGrade / 3) {
            feedback = feedback + "Your performance on this exam needs improvement.";
        } else {
            feedback = feedback + "You have made some progress in this exam, but there is room for improvement.";
        }
        return feedback;
    }

    public ConvertExamIntoSolutionsListener getConvertedExamListenerResult(String examOnlyQuestions) {
        ExamLexer lexer = new ExamLexer(CharStreams.fromString(examOnlyQuestions));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ExamParser parser = new ExamParser(tokens);
        ParseTree examSolutionsTree = parser.sectionsGroup();

        ParseTreeWalker walker = new ParseTreeWalker();
        ConvertExamIntoSolutionsListener convertIntoSolutionsListener = new ConvertExamIntoSolutionsListener();
        walker.walk(convertIntoSolutionsListener, examSolutionsTree);
        return convertIntoSolutionsListener;
    }

    Iterable<Exam> findAll() {
        return examRepository.findAllExam();
    }

    @Transactional
    public void updateExam(AutomaticFormativeExam exam, AutomaticFormativeExam examNew) {
        String c = examNew.examContent();
        CourseCode course = examNew.course();
        if (verifyCourse(course)) {
            exam.update(CurrentTimeCalendars.now(), c, course);
            examRepository.save(exam);
        } else {
            throw new IllegalArgumentException("!!!The Course doesn't exist!!!");
        }

    }

    @Transactional
    public void updateNormalExam(Exam exam, Exam examNew) throws ParseException {
        String c = examNew.examContent();
        CourseCode course = examNew.course();
        ExamDates dates = examNew.examDates();
        if (verifyCourse(course)) {
            exam.update(CurrentTimeCalendars.now(), c, course, dates);
            examRepository.save(exam);
        } else {
            throw new IllegalArgumentException("!!!The Course doesn't exist!!!");
        }

    }


    Iterable<AutomaticFormativeExam> findAllAutomaticFormativeExam() {
        return examRepository.findAllAutomaticFormativeExam();
    }

    Iterable<Exam> findExamsByCourses(List<Course> courses) {
        List<CourseCode> coursesCodes = new ArrayList<>();
        for (Course course : courses) {
            coursesCodes.add(course.courseCode());
        }
        return examRepository.findExamsByCourses(coursesCodes);
    }

    Iterable<Exam> findExamsByCourseAndDate(CourseCode course, Calendar calendar) {
        return examRepository.findExamsByCourseAndDates(course, calendar);
    }

    Iterable<Exam> findExamsByCourse(CourseCode course) {
        return examRepository.findExamsByCourse(course);
    }

    public Iterable<AutomaticFormativeExam> findFormativeExamsByCourse(CourseCode code) {
        return examRepository.findFormativeExamsByCourse(code);
    }

    public boolean verifyCourse(CourseCode courseCode) {
        if (courseRepository.checkCourseCode(courseCode)) {
            return true;
        }
        return false;
    }
}
