package eapli.base.exammanagement.domain;

import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.meetingmanagement.domain.MeetingStatus;
import eapli.base.questionmanagement.domain.Question;
import eapli.base.questionmanagement.domain.QuestionType;

import javax.persistence.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

@Entity
public class AutomaticFormativeExam extends Exam {
    private static final long serialVersionUID = 1L;


    private static final String EXAM_TITLE_GRAMMAR_STRING = "Exam Title-";
    private static final String COURSE_CODE_GRAMMAR_STRING = "Course code-";

    @Version
    private Long version;

    private Long id;

    @Column(name = "examTitle", unique = true)
    @EmbeddedId
    private ExamTitle examTitle;

    @Embedded
    private CourseCode courseCode;

    private String examContent;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Question> examQuestion;

    @Temporal(TemporalType.DATE)
    private Calendar modifiedOn;


    protected AutomaticFormativeExam() {
    }

    public AutomaticFormativeExam(ExamTitle examTitle, CourseCode courseCode, String examContent) {
        this.examTitle = examTitle;
        this.courseCode = courseCode;
        this.examContent = examContent;
    }

    public AutomaticFormativeExam(String examContent, List<Question> examQuestion) throws ParseException {
        this.examContent = examContent;
        this.examTitle = ExamTitle.valueOf(findContentInQuotesInExamContent(EXAM_TITLE_GRAMMAR_STRING));
        this.courseCode = CourseCode.valueOf(findContentInQuotesInExamContent(COURSE_CODE_GRAMMAR_STRING));
        this.examQuestion = examQuestion;
    }

    public AutomaticFormativeExam(String examContent) throws ParseException {
        this.examContent = examContent;
        this.examTitle = ExamTitle.valueOf(findContentInQuotesInExamContent(EXAM_TITLE_GRAMMAR_STRING));
        this.courseCode = CourseCode.valueOf(findContentInQuotesInExamContent(COURSE_CODE_GRAMMAR_STRING));

    }


    private String findContentInQuotesInExamContent(String contentToFind) {
        int tmpIndex = examContent.indexOf(contentToFind); //plus one because the next character is a:"
        int startIndex = examContent.indexOf("\"", tmpIndex) + 1; //plus one to go to the first character after the "
        int endIndex = examContent.indexOf("\"", startIndex);
        return examContent.substring(startIndex, endIndex); //EXAMPLE: for the line ->Exam Title-"Title Test"<- it will return ->Title Test<-
    }

    private String findContentWithoutQuotesInExamContent(String contentToFind) {
        int startIndex = examContent.indexOf(contentToFind) + contentToFind.length();
        int endIndex = examContent.indexOf("\n", startIndex);
        return examContent.substring(startIndex, endIndex); //EXAMPLE: for the line ->Open Date-01/01/2000<- it will return ->01/01/2000<-
    }

    public AutomaticFormativeExam(String examTitle, String courseCode, String examContent) {
        this.examTitle = new ExamTitle(examTitle);
        this.courseCode = CourseCode.valueOf(courseCode);
        this.examContent = examContent;

    }

    public void update(Calendar now, String content, CourseCode course) {
        examContent = content;
        courseCode = course;
        this.modifiedOn = now;
    }

    public ExamTitle title() {

        return ExamTitle.valueOf(findContentInQuotesInExamContent(EXAM_TITLE_GRAMMAR_STRING));
    }

    public CourseCode course() {
        return CourseCode.valueOf(findContentInQuotesInExamContent(COURSE_CODE_GRAMMAR_STRING));
    }

    public String examContent() {
        return this.examContent;
    }


    public Long getId() {
        return id;
    }
}
