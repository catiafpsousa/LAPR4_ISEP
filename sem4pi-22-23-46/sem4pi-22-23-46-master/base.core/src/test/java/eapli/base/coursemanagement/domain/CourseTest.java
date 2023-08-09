package eapli.base.coursemanagement.domain;

import eapli.base.infrastructure.authz.domain.model.*;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.framework.time.util.CurrentTimeCalendars;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CourseTest {

    //COURSE

    private static final String CODE = "JAVA-1";

    private static final String CODE2 = "JAVA-2";

    private static final String NAME = "Programcao em Java";

    private static final String DESCRIPTION = "Primeira edicao do curso intensivo em Java";


    private static final int STUDENTS_MIN = 10;

    private static final int STUDENTS_MAX = 45;


    //USER

    private static final String EMAIL1 = "pcs@isep.pt";

    private static final String EMAIL2 = "ajs@isep.pt";

    private static final String EMAIL3 = "bfm@isep.pt";

    private static final String PASSWORD = "Password1";

    private static final String FULL_NAME = "Professor Teste Unitario";

    private static final String SHORT_NAME = "Teste";

    private static final String VAT_ID = "123456789";

    private static final int DAY = 12;

    private static final int MONTH = 01;

    private static final int YEAR = 1985;


    private final Set<Role> roles = new HashSet<>();

    final SystemUserBuilder userBuilder = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder());

    public static final Role TEACHER = Role.valueOf("TEACHER");


    @Test(expected = IllegalArgumentException.class)
    public void ensureCourseCantHaveCodeNull() {
        Course teste = new Course(null, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureCourseCantHaveNameNull() {
        Course teste = new Course(CODE, null, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureCourseCantHaveDescriptionNull() {
        Course teste = new Course(CODE, NAME, null, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureCourseInvalidMin() {
        Course teste = new Course(CODE, NAME, DESCRIPTION, 0, STUDENTS_MAX, CurrentTimeCalendars.now());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureCourseInvalidMax() {
        Course teste = new Course(CODE, NAME, DESCRIPTION, 400, STUDENTS_MAX, CurrentTimeCalendars.now());
    }

    @Test
    public void ensureCourseEqualsFailsForDifferentCode() {
        Course teste1 = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
        Course teste2 = new Course(CODE2, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());

        final boolean expected = teste1.equals(teste2);

        assertFalse(expected);
    }

    @Test
    public void ensureCourseEqualsTheSameInstance() {
        Course teste = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());

        final boolean expected = teste.equals(teste);

        assertTrue(expected);
    }

    @Test
    public void ensureCourseIsTheSameAsItsInstance() {
        Course teste = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());

        final boolean expected = teste.sameAs(teste);

        assertTrue(expected);
    }

    @Test
    public void ensureCourseWithDifferentCodesAreNotTheSameInstance() {
        Course teste1 = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
        Course teste2 = new Course(CODE2, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());

        final boolean expected = teste1.sameAs(teste2);

        assertFalse(expected);
    }


    @Test
    public void ensureCourseIsCLosedWhenCreated() {
        Course teste = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
        assertEquals(CourseState.CLOSED, teste.courseState());
    }

    @Test
    public void ensureCourseIsOpenAfterOpenCommand() {
        Course teste = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
        teste.open(CurrentTimeCalendars.now());
        assertEquals(CourseState.OPEN, teste.courseState());
    }

    @Test
    public void ensureCourseAcceptsEnrollsAfterEnrollCommand() {
        Course teste = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
        teste.enroll(CurrentTimeCalendars.now());
        assertEquals(CourseState.ENROLL, teste.courseState());
    }

    @Test
    public void ensureCourseIsInProgressAfterCloseEnrolls() {
        Course teste = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
        teste.inProgress(CurrentTimeCalendars.now());
        assertEquals(CourseState.IN_PROGRESS, teste.courseState());
    }

    @Test
    public void ensureCourseIsClosedAfterCloseCommand() {
        Course teste = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
        teste.open(CurrentTimeCalendars.now());
        teste.close(CurrentTimeCalendars.now());
        assertEquals(CourseState.CLOSED, teste.courseState());
    }

    @Test
    public void ensureCourseTeachersAreSetCorrectly() {

        final Set<Role> roles = new HashSet<>();
        roles.add(TEACHER);

        final Set<Role> roles2 = new HashSet<>();
        roles.add(TEACHER);

        SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();
        SystemUser user2 = userBuilder.with(EMAIL2, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles2).build();

        Teacher teacher1 = new Teacher(user1);
        Teacher teacher2 = new Teacher(user2);

        List<Teacher> courseTeachers = new ArrayList<>();
        courseTeachers.add(teacher1);
        courseTeachers.add(teacher2);

        Course teste = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
        teste.setCourseTeachers(courseTeachers);

        assertEquals(courseTeachers, teste.courseTeachers());
    }

    @Test
    public void ensureSetCourseTeachersIsTheMostRecentUpdate() {

        final Set<Role> roles = new HashSet<>();
        roles.add(TEACHER);

        final Set<Role> roles2 = new HashSet<>();
        roles.add(TEACHER);

        SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();
        SystemUser user2 = userBuilder.with(EMAIL2, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles2).build();
        SystemUser user3 = userBuilder.with(EMAIL3, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles2).build();


        Teacher teacher1 = new Teacher(user1);
        Teacher teacher2 = new Teacher(user2);

        List<Teacher> courseTeachers = new ArrayList<>();
        courseTeachers.add(teacher1);
        courseTeachers.add(teacher2);

        Course teste = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
        teste.setCourseTeachers(courseTeachers);

        Teacher teacher3 = new Teacher(user3);
        courseTeachers.add(teacher3);
        teste.setCourseTeachers(courseTeachers);

        assertEquals(courseTeachers, teste.courseTeachers());
    }

    @Test
    public void ensureSetCourseTeachersReplacesPreviousListNotAddsTo() {

        final Set<Role> roles = new HashSet<>();
        roles.add(TEACHER);

        final Set<Role> roles2 = new HashSet<>();
        roles.add(TEACHER);

        SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();
        SystemUser user2 = userBuilder.with(EMAIL2, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles2).build();
        SystemUser user3 = userBuilder.with(EMAIL3, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles2).build();


        Teacher teacher1 = new Teacher(user1);
        Teacher teacher2 = new Teacher(user2);

        List<Teacher> courseTeachers = new ArrayList<>();
        courseTeachers.add(teacher1);
        courseTeachers.add(teacher2);

        Course teste = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
        teste.setCourseTeachers(courseTeachers);

        Teacher teacher3 = new Teacher(user3);
        List<Teacher> courseTeachers2 = new ArrayList<>();
        courseTeachers2.add(teacher3);
        teste.setCourseTeachers(courseTeachers2);

        assertEquals(courseTeachers2, teste.courseTeachers());
    }

    @Test
    public void ensureSetTeacherInChargeIsCorrect() {

        final Set<Role> roles = new HashSet<>();
        roles.add(TEACHER);

        final Set<Role> roles2 = new HashSet<>();
        roles.add(TEACHER);

        SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();

        Teacher teacher1 = new Teacher(user1);


        Course teste = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
        teste.setTeacherInCharge(teacher1);

        assertEquals(teacher1, teste.teacherInCharge().get());
    }

    @Test
    public void ensureSetTeacherInChargeReplacesPrevious() {

        final Set<Role> roles = new HashSet<>();
        roles.add(TEACHER);

        final Set<Role> roles2 = new HashSet<>();
        roles.add(TEACHER);

        SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();
        SystemUser user2 = userBuilder.with(EMAIL2, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles2).build();


        Teacher teacher1 = new Teacher(user1);
        Teacher teacher2 = new Teacher(user2);

        Course teste = new Course(CODE, NAME, DESCRIPTION, STUDENTS_MIN, STUDENTS_MAX, CurrentTimeCalendars.now());
        teste.setTeacherInCharge(teacher1);
        teste.setTeacherInCharge(teacher2);

        assertEquals(teacher2, teste.teacherInCharge().get());
    }

}