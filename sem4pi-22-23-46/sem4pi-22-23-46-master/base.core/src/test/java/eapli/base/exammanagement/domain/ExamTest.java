package eapli.base.exammanagement.domain;


import eapli.base.coursemanagement.domain.CourseCode;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ExamTest {


    private final static String examtitle = "Titulo do exame";

    private final static ExamTitle examTitle1 = new ExamTitle(examtitle);


    private final static String examContent = "exam content ";

    private final static CourseCode courseCode = new CourseCode("code");


    @Test(expected = IllegalArgumentException.class)
    public void ensureExamCantHaveExamTitleNull() {
        Calendar openDate = Calendar.getInstance();
        Calendar closeDate = Calendar.getInstance();
        closeDate.add(Calendar.DAY_OF_MONTH, 7); // Set close date 7 days after open date
        ExamDates examDates = new ExamDates(openDate, closeDate);
        Exam teste = new Exam(null, examDates, courseCode, examContent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureExamCantHaveExamDatesNull() {

        Exam teste = new Exam(examTitle1, null, courseCode, examContent);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureExamCantHaveCourseCodeNull() {
        Calendar openDate = Calendar.getInstance();
        Calendar closeDate = Calendar.getInstance();
        closeDate.add(Calendar.DAY_OF_MONTH, 7); // Set close date 7 days after open date
        ExamDates examDates = new ExamDates(openDate, closeDate);
        Exam teste = new Exam(examTitle1, examDates, null, examContent);

    }

    @Test
    public void testConstructorWithValidDates() {
        Calendar openDate = Calendar.getInstance();
        Calendar closeDate = Calendar.getInstance();
        closeDate.add(Calendar.DAY_OF_MONTH, 7); // Set close date 7 days after open date

        ExamDates examDates = new ExamDates(openDate, closeDate);

        assertEquals(openDate, examDates.openDate());
        assertEquals(closeDate, examDates.closeDate());
    }

    @Test
    public void testConstructorWithInvalidDates() {
        Calendar openDate = Calendar.getInstance();
        Calendar closeDate = Calendar.getInstance();
        closeDate.add(Calendar.DAY_OF_MONTH, -7); // Set close date 7 days before open date

        assertThrows(IllegalArgumentException.class, () -> {
            ExamDates examDates = new ExamDates(openDate, closeDate);
        });
    }
}