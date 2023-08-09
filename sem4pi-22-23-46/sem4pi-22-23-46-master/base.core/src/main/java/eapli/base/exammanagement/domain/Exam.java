package eapli.base.exammanagement.domain;

import eapli.base.coursemanagement.domain.CourseCode;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import validations.util.Preconditions;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Entity
public class Exam implements AggregateRoot<ExamTitle> {

    private static final long serialVersionUID = 1L;

    private static final String EXAM_TITLE_GRAMMAR_STRING = "Exam Title-";
    private static final String OPEN_DATE_GRAMMAR_STRING = "Open Date-";
    private static final String CLOSE_DATE_GRAMMAR_STRING = "Close Date-";
    private static final String COURSE_CODE_GRAMMAR_STRING = "Course code-";
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    @Version
    private Long version;

    private Long id;

    @Column(name = "examTitle", unique = true)
    @EmbeddedId
    private ExamTitle examTitle;

    @Embedded
    private ExamDates examDates;

    @Embedded
    private CourseCode courseCode;

    @Lob
    private String examContent;
    @Temporal(TemporalType.DATE)
    private Calendar modifiedOn;


    protected Exam(){}

    public Exam (String examContent) throws ParseException {

        this.examContent = examContent;
        this.examTitle = ExamTitle.valueOf(findContentInQuotesInExamContent(EXAM_TITLE_GRAMMAR_STRING));
        this.examDates = getDatesFromExamContent();
        this.courseCode = CourseCode.valueOf(findContentInQuotesInExamContent(COURSE_CODE_GRAMMAR_STRING));
    }

    private ExamDates getDatesFromExamContent() throws ParseException {
        String openDate = findContentWithoutQuotesInExamContent(OPEN_DATE_GRAMMAR_STRING);
        String closeDate = findContentWithoutQuotesInExamContent(CLOSE_DATE_GRAMMAR_STRING);
        Calendar calOpenDate = Calendar.getInstance();
        Calendar calCloseDate = Calendar.getInstance();
        SimpleDateFormat ptSDF = new SimpleDateFormat(DATE_FORMAT);
        calOpenDate.setTime(ptSDF.parse(openDate));
        calCloseDate.setTime(ptSDF.parse(closeDate));
        return ExamDates.valueOf(calOpenDate, calCloseDate);
    }

    private String findContentInQuotesInExamContent(String contentToFind){
        int tmpIndex = examContent.indexOf(contentToFind); //plus one because the next character is a:"
        int startIndex = examContent.indexOf("\"",tmpIndex) + 1; //plus one to go to the first character after the "
        int endIndex = examContent.indexOf("\"",startIndex);
        return examContent.substring(startIndex, endIndex); //EXAMPLE: for the line ->Exam Title-"Title Test"<- it will return ->Title Test<-
    }
    private String findContentWithoutQuotesInExamContent(String contentToFind){
        int startIndex = examContent.indexOf(contentToFind) + contentToFind.length();
        int endIndex = examContent.indexOf("\n",startIndex);
        return examContent.substring(startIndex, endIndex); //EXAMPLE: for the line ->Open Date-01/01/2000<- it will return ->01/01/2000<-
    }

    public Exam(ExamTitle examTitle, ExamDates examDates, CourseCode courseCode, String examContent) {
        Preconditions.nonNull(examTitle);
        Preconditions.nonNull(examDates);
        Preconditions.nonNull(courseCode);
        this.examTitle = examTitle;
        this.examDates = examDates;
        this.courseCode = courseCode;
        this.examContent = examContent;
    }

    public String getExamCourse(){
        return courseCode.toString();
    }

    public ExamTitle title() {

        return ExamTitle.valueOf(findContentInQuotesInExamContent(EXAM_TITLE_GRAMMAR_STRING));
    }

    public CourseCode course() {
        return CourseCode.valueOf(findContentInQuotesInExamContent(COURSE_CODE_GRAMMAR_STRING));
    }


    public ExamDates examDates() throws ParseException {
        ExamDates dates = getDatesFromExamContent();
        return dates;}

    public void update(Calendar now, String content , CourseCode course, ExamDates dates){
        examContent = content;
        courseCode = course;
        examDates =dates;
        this.modifiedOn = now;
    }

    public String examContent(){return this.examContent;}

    @Override
    public boolean hasIdentity(ExamTitle id) {
        return AggregateRoot.super.hasIdentity(id);
    }

    @Override
    public boolean sameAs(Object other) {
        return DomainEntities.areEqual(this, other);
    }

    @Override
    public ExamTitle identity() {
        return this.examTitle;
    }

    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }


}
