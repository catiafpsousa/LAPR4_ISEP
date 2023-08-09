//package eapli.base.boardmanagement.domain;
//import eapli.base.infrastructure.authz.domain.model.*;
//import org.junit.Test;
//import java.util.HashSet;
//import java.util.Set;
//import static org.junit.Assert.*;
//
//public class PostItTest {
//
//    private static final String EMAIL1 = "pcs@isep.pt";
//
//    private static final String PASSWORD = "Password1";
//
//    private static final String FULL_NAME = "Professor Teste Unitario";
//
//    private static final String SHORT_NAME = "Teste";
//
//    private static final String VAT_ID = "123456789";
//
//    private static final int DAY = 12;
//
//    private static final int MONTH = 01;
//
//    private static final int YEAR = 1985;
//
//
//    private final Set<Role> roles = new HashSet<>();
//
//    final SystemUserBuilder userBuilder = new SystemUserBuilder(new NilPasswordPolicy(), new PlainTextEncoder());
//
//    public static final Role TEACHER = Role.valueOf("TEACHER");
//
//
//    @Test(expected = IllegalArgumentException.class)
//    public void ensureCreatePostItCantHaveContentNull() {
//        final Set<Role> roles = new HashSet<>();
//        roles.add(TEACHER);
//        SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH, YEAR).withRoles(roles).build();
//        Content content = new Content(null);
//        new PostIt(user1, content);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void ensureCreatePostItCantHaveNullOwner() {
//        Content content = new Content("conteudo");
//        new PostIt((SystemUser) null, content);
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void ensureCreatePostItCantHaveNullOwnerNeitherNullContent() {
//        Content content = new Content(null);
//        new PostIt((SystemUser) null, content);
//    }
//
////    @Test(expected = IllegalArgumentException.class)
////    public void ensureUpdatePostItCantHaveParentNull() {
////        new PostIt((SystemUser) null, new Content("conteudo"));
////    }
////
////    @Test(expected = IllegalArgumentException.class)
////    public void ensureUpdatePostItCantHaveNewContentNull() {
////        final Set<Role> roles = new HashSet<>();
////        roles.add(TEACHER);
////        SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH, YEAR).withRoles(roles).build();
////        PostIt parent = new PostIt(user1, new Content("conteudo"));
////        new PostIt(parent, null);
////    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void ensureUndoPostItCantHaveParentNull() {
//        new PostIt(null);
//    }
//
//    @Test
//    public void ensurePostItIsNotDeletedWhenCreated() {
//        final Set<Role> roles = new HashSet<>();
//        roles.add(TEACHER);
//        SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH, YEAR).withRoles(roles).build();
//
//        PostIt postIt = new PostIt(user1, new Content("conteudo"));
//
//        assertFalse(postIt.isDeleted());
//    }
//
//    @Test
//    public void ensurePostItNextIsNullWhenCreated() {
//        final Set<Role> roles = new HashSet<>();
//        roles.add(TEACHER);
//        SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH, YEAR).withRoles(roles).build();
//
//        PostIt postIt = new PostIt(user1, new Content("conteudo"));
//        assertNull(postIt.getNext());
//    }
//
//    @Test
//    public void ensurePostItParentIsNullWhenCreated() {
//        final Set<Role> roles = new HashSet<>();
//        roles.add(TEACHER);
//        SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH, YEAR).withRoles(roles).build();
//
//        PostIt postIt = new PostIt(user1, new Content("conteudo"));
//        assertNull(postIt.parent());
//    }
//
//    @Test
//    public void ensurePostItIsDeletedAfterDeleteCommand() {
//        final Set<Role> roles = new HashSet<>();
//        roles.add(TEACHER);
//        SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH, YEAR).withRoles(roles).build();
//
//        PostIt postIt = new PostIt(user1, new Content("conteudo"));
//
//        postIt.deletePostIt();
//
//        assertTrue(postIt.isDeleted());
//    }
//
//    @Test
//    public void ensureNextPostItIsCorrectlyChangedAfterCommand() {
//        final Set<Role> roles = new HashSet<>();
//        roles.add(TEACHER);
//        SystemUser user1 = userBuilder.with(EMAIL1, PASSWORD, FULL_NAME, SHORT_NAME, VAT_ID, DAY, MONTH, YEAR).withRoles(roles).build();
//
//        PostIt postIt = new PostIt(user1, new Content("conteudo"));
//        PostIt next = new PostIt(user1, new Content("conteudo2"));
//
//        postIt.changeNextPostIt(next);
//
//        assertEquals(postIt.getNext(), next);
//    }
//
//}
//
