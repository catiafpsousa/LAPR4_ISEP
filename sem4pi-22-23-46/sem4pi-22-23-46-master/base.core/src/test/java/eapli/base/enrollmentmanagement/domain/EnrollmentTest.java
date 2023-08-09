package eapli.base.enrollmentmanagement.domain;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.infrastructure.authz.domain.model.*;
import eapli.base.studentmanagement.domain.Student;
import eapli.framework.time.util.CurrentTimeCalendars;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class EnrollmentTest {

    private final static int MAX_STUDENTS = 20;
    private final static int MIN_STUDENTS = 1;

    private static final String EMAIL1 = "pcs@isep.pt";

    private static final String PASSWORD = "Password1";

    private static final String FULL_NAME = "Professor Teste Unitario";

    private static final String SHORT_NAME = "Teste";

    private static final String VAT_ID = "123456789";

    private static final int DAY = 12;

    private static final int MONTH = 01;

    private static final int YEAR = 1985;
    private final static long repoSize = 10;
    private final Calendar date = CurrentTimeCalendars.now();

   private final  Course course = new Course("Java-1", "JAVA", "Java for Begginers", MIN_STUDENTS, MAX_STUDENTS, date);

    private final SystemUserBuilder userBuilder = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder());

    private final SystemUser user = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).build();
    private final Student student = new Student(user, repoSize);

    @Test
    public void ensureEnrollmentHasStudent(){
    assertThrows(IllegalArgumentException.class, () ->
        new Enrollment(null, course, EnrollmentStatus.REQUESTED, date));
    }

    @Test
    public void ensureEnrollmentHasCourse() {
        assertThrows(IllegalArgumentException.class, () ->
            new Enrollment(student, null, EnrollmentStatus.REQUESTED, date));
    }

    @Test
    public void ensureEnrollmentHasToken() {
        final Enrollment e = new Enrollment(student, course, EnrollmentStatus.REQUESTED, date);
        assertNotNull(e.identity());
    }

}
