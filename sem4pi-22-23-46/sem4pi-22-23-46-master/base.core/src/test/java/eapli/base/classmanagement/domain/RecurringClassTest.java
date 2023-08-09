package eapli.base.classmanagement.domain;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.infrastructure.authz.domain.model.*;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.framework.time.util.CurrentTimeCalendars;
import org.junit.Test;
import java.time.LocalTime;
import java.util.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
public class RecurringClassTest{
    //USER
    private static final String EMAIL1 = "pcs@isep.pt";
    private static final String PASSWORD = "Password1";
    private static final String FULL_NAME = "Professor Teste Unitario";
    private static final String SHORT_NAME = "Teste";
    private static final String VAT_ID = "123456789";
    private static final int DAY = 12;
    private static final int MONTH = 9;
    private static final int YEAR = 1985;
    //Extraordinary Class
    private static final LocalTime TIME = LocalTime.of(9, 30);
    private static Calendar valid_date = Calendar.getInstance();
    private static int DURATION = 30;
    private static final String TITLE="Title";
    private static int YEAR1=2024;

    //COURSE
    private static final String CODE = "JAVA-1";
    private static final String CODE2 = "JAVA-2";
    private static final String NAME = "Programcao em Java";
    private static final String DESCRIPTION = "Primeira edicao do curso intensivo em Java";
    private static final int STUDENTS_MIN = 10;
    private static final int STUDENTS_MAX = 45;
    final Course course = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
    private final Set<Role> roles = new HashSet<>();
    final SystemUserBuilder userBuilder = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder());
    final SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();
    final Teacher teacher1 = new Teacher(user1);
    final RecurringClassBuilder classBuilder = new RecurringClassBuilder();

    @Test
    public void ensureHasTeacher(){
        System.out.println("Class has teacher");
        valid_date.set(YEAR1, MONTH, DAY);
        assertThrows(IllegalArgumentException.class, () -> classBuilder.withDuration(DURATION).withTitle(TITLE).withDay(valid_date).withTeacher(null).withTime(TIME).withCourse(course).build());
    }
    @Test
    public void ensureHasDuration(){
        System.out.println("Class has duration");
        valid_date.set(YEAR1, MONTH, DAY);
        assertThrows(IllegalArgumentException.class, () -> classBuilder.withDuration(-1).withTitle(TITLE).withDay(valid_date).withTeacher(teacher1).withTime(TIME).withCourse(course).build());
    }
    @Test
    public void ensureHasDate(){
        System.out.println("Class has valid date");
        assertThrows(NullPointerException.class, () -> classBuilder.withDuration(DURATION).withTitle(TITLE).withDay(null).withTeacher(teacher1).withTime(TIME).withCourse(course).build());
    }
    @Test
    public void ensureDateIsInFuture(){
        System.out.println("Class date is in future");
        valid_date.set(YEAR, MONTH, DAY);
        assertThrows(IllegalArgumentException.class, () -> classBuilder.withDuration(DURATION).withTitle(TITLE).withDay(valid_date).withTeacher(teacher1).withTime(TIME).withCourse(course).build());
    }
    @Test
    public void ensureHasCourse(){
        System.out.println("Class has course");
        valid_date.set(YEAR1, MONTH, DAY);
        assertThrows(IllegalArgumentException.class, () -> classBuilder.withDuration(DURATION).withTitle(TITLE).withDay(valid_date).withTeacher(teacher1).withTime(TIME).withCourse(null).build());
    }

    @Test
    public void ensureHasTitle(){
        System.out.println("Class has course");
        valid_date.set(YEAR1, MONTH, DAY);
        assertThrows(IllegalArgumentException.class, () -> classBuilder.withDuration(DURATION).withTitle("").withDay(valid_date).withTeacher(teacher1).withTime(TIME).withCourse(course).build());
    }
    @Test
    public void ensureIsValid(){
        System.out.println("Class is valid");
        valid_date.set(YEAR1, MONTH, DAY);
        RecurringClass extra= classBuilder.withDuration(DURATION).withTitle(TITLE).withDay(valid_date).withTeacher(teacher1).withTime(TIME).withCourse(course).build();
        assertNotNull(extra);
    }
}