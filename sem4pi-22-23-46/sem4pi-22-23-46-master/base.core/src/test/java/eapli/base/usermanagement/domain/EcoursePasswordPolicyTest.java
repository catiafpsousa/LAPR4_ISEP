package eapli.base.usermanagement.domain;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import org.junit.Test;
import eapli.base.usermanagement.domain.EcoursePasswordPolicy.PasswordStrength;



public class EcoursePasswordPolicyTest  {

    private final EcoursePasswordPolicy subject = new EcoursePasswordPolicy();

    @Test
    public void ensurePasswordHasAtLeastOneDigitOneCapitalAnd6CharactersLong() {
        assertTrue(subject.isSatisfiedBy("abCfefgh1"));
    }

    @Test
    public void ensurePasswordsSmallerThan6CharactersAreNotAllowed() {
        assertFalse(subject.isSatisfiedBy("ab1c"));
    }

    @Test
    public void ensurePasswordsWithoutDigitsAreNotAllowed() {
        assertFalse(subject.isSatisfiedBy("abcefghi"));
    }

    @Test
    public void ensurePasswordsWithoutCapitalLetterAreNotAllowed() {
        assertFalse(subject.isSatisfiedBy("abcefghi1"));
    }

    @Test
    public void testWeakPassword1() {
        assertEquals(PasswordStrength.WEAK, subject.strength("A23456"));
    }

    @Test
    public void testWeakPassword2() {
        assertEquals(PasswordStrength.WEAK, subject.strength("A234567"));
    }

    @Test
    public void testGoodPassword1() {
        assertEquals(PasswordStrength.GOOD, subject.strength("A2345678"));
    }

    @Test
    public void testGoodPassword2() {
        assertEquals(PasswordStrength.GOOD, subject.strength("A23456789"));
    }

    @Test
    public void testExcelentPassword1() {
        assertEquals(PasswordStrength.EXCELENT, subject.strength("123456789ABC"));
    }

    @Test
    public void testExcelentPassword2() {
        assertEquals(PasswordStrength.EXCELENT, subject.strength("123456789ABCD"));
    }

    @Test
    public void testExcelentPassword3() {
        assertEquals(PasswordStrength.EXCELENT, subject.strength("A234$5678"));
    }

    @Test
    public void testExcelentPassword4() {
        assertEquals(PasswordStrength.EXCELENT, subject.strength("A234#5678"));
    }

    @Test
    public void testExcelentPassword5() {
        assertEquals(PasswordStrength.EXCELENT, subject.strength("A234!5678"));
    }

    @Test
    public void testExcelentPassword6() {
        assertEquals(PasswordStrength.EXCELENT, subject.strength("A234?5678"));
    }

}