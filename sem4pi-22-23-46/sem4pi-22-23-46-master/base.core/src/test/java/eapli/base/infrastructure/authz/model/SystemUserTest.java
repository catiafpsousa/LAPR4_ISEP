package eapli.base.infrastructure.authz.model;

import eapli.base.infrastructure.authz.domain.model.*;
import eapli.framework.time.util.CurrentTimeCalendars;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SystemUserTest {
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

    public static final Role ADMIN = Role.valueOf("ADMIN");

    @Test(expected = IllegalArgumentException.class)
    public void ensureUserCantHaveEmailNull() {
        roles.add(ADMIN);
        userBuilder.with(null, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureUserCantHaveFullNameNull() {
        roles.add(ADMIN);
        userBuilder.with(EMAIL1, PASSWORD, null, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureUserCantHaveShortNameNull() {
        roles.add(ADMIN);
        userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, null, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void ensureUserCantHavePasswordNull() {
        roles.add(ADMIN);
        userBuilder.with(EMAIL1, null, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();
    }

    @Test
    public void ensureUserCanHaveRoleSetNull() {
        SystemUser result = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).build();
        assertNotEquals(IllegalArgumentException.class, result);
    }

    @Test
    public void ensureUserEqualsFailsForDifferenteEmail() throws Exception {
        final Set<Role> roles = new HashSet<>();
        roles.add(ADMIN);

        final Set<Role> roles2 = new HashSet<>();
        roles.add(ADMIN);

        SystemUser user1 =  userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();

        SystemUser user2 =  userBuilder.with(EMAIL2, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles2).build();

        final boolean expected = user1.equals(user2);

        assertFalse(expected);
    }

    @Test
    public void ensureUserEqualsTheSameInstance() throws Exception {
        final Set<Role> roles = new HashSet<>();
        roles.add(ADMIN);

        SystemUser user1 =  userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();

        final boolean expected = user1.equals(user1);

        assertTrue(expected);
    }

    @Test
    public void ensureUserIsTheSameAsItsInstance() throws Exception {
        final Set<Role> roles = new HashSet<>();
        roles.add(ADMIN);

        SystemUser user1 =  userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();

        final boolean expected = user1.sameAs(user1);

        assertTrue(expected);
    }

    @Test
    public void ensureUserWithDifferentEmailsAreNotTheSame() throws Exception {
        final Set<Role> roles = new HashSet<>();
        roles.add(ADMIN);

        final Set<Role> roles2 = new HashSet<>();
        roles.add(ADMIN);

        SystemUser user1 =  userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();

        SystemUser user2 =  userBuilder.with(EMAIL2, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles2).build();

        final boolean expected = user1.sameAs(user2);

        assertFalse(expected);
    }

    @Test
    public void ensureUserStatusIsActiveAfterCreation() throws Exception {
        final Set<Role> roles = new HashSet<>();
        roles.add(ADMIN);

        SystemUser user1 =  userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();

        final boolean expected = user1.isActive();

        assertTrue(expected);
    }

    @Test
    public void ensureUserStatusIsDectivatedAfterDeactivation() throws Exception {
        final Set<Role> roles = new HashSet<>();
        roles.add(ADMIN);

        SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();

        user1.deactivate(CurrentTimeCalendars.now());

        final boolean expected = user1.isActive();
        assertFalse(expected);
    }

    @Test
    public void ensureUserStatusIsActivatedAfterActivation() throws Exception {
        final Set<Role> roles = new HashSet<>();
        roles.add(ADMIN);

        SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH,YEAR).withRoles(roles).build();

        user1.deactivate(CurrentTimeCalendars.now());

        user1.activate(CurrentTimeCalendars.now());

        final boolean expected = user1.isActive();
        assertTrue(expected);
    }

}