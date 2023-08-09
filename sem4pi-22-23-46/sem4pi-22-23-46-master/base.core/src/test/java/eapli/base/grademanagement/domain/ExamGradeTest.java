package eapli.base.grademanagement.domain;

import eapli.base.exammanagement.application.ExamManagementService;
import eapli.base.exammanagement.domain.Exam;
import eapli.base.infrastructure.authz.domain.model.NilPasswordPolicy;
import eapli.base.infrastructure.authz.domain.model.PlainTextEncoder;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.model.SystemUserBuilder;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;

public class ExamGradeTest {

    private static final String EMAIL = "student@isep.pt";

    private static final String PASSWORD = "Password1";

    private static final String FULL_NAME = "Student Teste Unitario";

    private static final String SHORT_NAME = "Teste";

    private static final String VAT_ID = "123456789";

    private static final int DAY = 12;

    private static final int MONTH = 01;

    private static final int YEAR = 1985;


    final SystemUserBuilder userBuilder = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder());


    String examContent = "Exam Title-\"Primeiro Exame de java\"\n" +
            "Exam Description-\"Teste primeiro semestre APROG\"\n" +
            "Open Date-10/06/2023\n" +
            "Close Date-25/06/2023\n" +
            "[Exam Header]\n" +
            "Header Description-\"SEM1 DEI ISEP\"\n" +
            "FeedbackType-$ONSUBMISSION$\n" +
            "GradeType-$ONSUBMISSION$\n" +
            "[Sections]\n" +
            "Section 1-\"Verdadeiro ou Falso\"\n" +
            "Section Description-\"Diga se as expressoes estao corretas\"\n" +
            "**TRUE_OR_FALSE**\n" +
            "score-1\n" +
            "Question-\"int x = x?\"\n" +
            "FALSE" + "[/Sections]" +
            "Course code-\"ESOFT1\"";

    final String java1Exam = "Exam Title-\"Primeiro Exame de JAVA1\"\n" +
            "Exam Description-\"primeiro semestre JAVA1\"\n" +
            "Open Date-10/06/2023\n" +
            "Close Date-25/06/2023\n" +
            "[Exam Header]\n" +
            "Header Description-\"SEM1 DEI ISEP\"\n" +
            "FeedbackType-$ONSUBMISSION$\n" +
            "GradeType-$ONSUBMISSION$\n" +
            "[Sections]\n" +
            "Section 1-\"Verdadeiro ou Falso\"\n" +
            "Section Description-\"Diga se as expressoes estao corretas\"\n" +
            "**TRUE_OR_FALSE**\n" +
            "score-2\n" +
            "Question-\"int x = x?\"\n" +
            "FALSE\n" +
            "Question-\"int exemplo(valor1, valor2)\"\n" +
            "TRUE\n" +
            "Section 2-\"Multiple choice\"\n" +
            "Section Description-\"Escolha a alinea correta\"\n" +
            "**MULTIPLE_CHOICE**\n" +
            "score-1.5\n" +
            "Question-\"Qual dos for esta correto?\"\n" +
            "\"for()\"\n" +
            "\"for(int i; i++)\"\n" +
            "*\"for(int i; i<10; i++)\"\n" +
            "\"for(int i; i<10)\"\n" +
            "**MATCHING**\n" +
            "score-3\n" +
            "Question-\"Match the code to the language\"\n" +
            "Left_column:\n" +
            "\"numbers_list = [2, 5, 62, 5, 42, 52, 48, 5]\"\n" +
            "\"int[] age = {12, 4, 5};\"\n" +
            "\"int numbers[5] = {7, 5, 6, 12, 35};\"\n" +
            "Right_column:\n" +
            "\"C/C++\"\n" +
            "\"JavaScript\"\n" +
            "\"Python\"\n" +
            "\"PHP\"\n" +
            "Solution:\n" +
            "\"int[] age = {12, 4, 5};\"--\"JavaScript\"\n" +
            "\"int numbers[5] = {7, 5, 6, 12, 35};\"--\"C/C++\"\n" +
            "\"numbers_list = [2, 5, 62, 5, 42, 52, 48, 5]\"--\"Python\"\n" +
            "**SHORT_ANSWER**\n" +
            "score-3\n" +
            "Question-\"2+2?\"\n" +
            "\"quatro\"\n" +
            "**NUMERICAL**\n" +
            "score-2\n" +
            "Question-\"2/3?\"\n" +
            "0.66\n" +
            "**SELECT_MISSING_WORD**\n" +
            "score-2.5\n" +
            "\"Em portugal há\"$18$\"distritos em Portugal.\"\n" +
            "\"A cidade do\"$Porto$\"tem as\"$melhores$\"asd\"\n" +
            "[/Sections]\n" +
            "Course code-\"JAVA1\"";

    Exam exam = new Exam(examContent);
    double score = 10.5;

    public ExamGradeTest() throws ParseException {
    }


    @Test(expected = IllegalArgumentException.class)
    public void ensureExamGradeCantHaveExamNull() {

        SystemUser user = userBuilder.with(EMAIL, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH, YEAR).build();

        ExamGrade teste = new ExamGrade(user, null, score, Calendar.getInstance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureExamGradeCantHaveUserNull() {

        ExamGrade teste = new ExamGrade(null, exam, score, Calendar.getInstance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureExamGradeCantHaveDateNull() {

        SystemUser user = userBuilder.with(EMAIL, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH, YEAR).build();

        ExamGrade teste = new ExamGrade(user, exam, score, null);
    }

    @Test
    public void examWithoutAnswersShouldReturnGradeOfZero() throws IOException {
        ExamManagementService examManSer = new ExamManagementService();
        String filePathAnsweredExam = "src/test/FilesForTesting/not_answered_java1_exam.txt";
        Double gradeOfAnsweredExam = examManSer.getGradeSubmitedExam(java1Exam, filePathAnsweredExam);
        assertEquals(0.0, gradeOfAnsweredExam,0.1);
    }
}