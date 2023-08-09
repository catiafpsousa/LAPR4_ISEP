/*
 * Copyright (c) 2013-2022 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eapli.base.infrastructure.authz.application;

import eapli.base.boardmanagement.application.BoardManagementService;
import eapli.base.boardmanagement.repositories.BoardRepository;
import eapli.base.classmanagement.application.ClassManagementService;
import eapli.base.classmanagement.repositories.ClassRepository;
import eapli.base.coursemanagement.application.CourseManagementService;
import eapli.base.coursemanagement.repositories.CourseRepository;
import eapli.base.enrollmentmanagement.application.EnrollmentManagementService;
import eapli.base.enrollmentmanagement.repositories.EnrollmentRepository;
import eapli.base.exammanagement.application.ExamManagementService;
import eapli.base.exammanagement.repositories.ExamRepository;
import eapli.base.grademanagement.application.GradeManagementService;
import eapli.base.grademanagement.repositories.GradeRepository;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.meetingmanagement.application.MeetingManagementService;
import eapli.base.meetingmanagement.repositories.MeetingRepository;
import eapli.base.questionmanagement.application.QuestionManagementService;
import eapli.base.questionmanagement.repositories.QuestionRepository;
import eapli.base.questionmanagement.repositories.QuestionTypeRepository;
import eapli.base.studentmanagement.application.StudentManagementService;
import eapli.base.studentmanagement.repositories.StudentRepository;
import eapli.base.teachermanagement.application.TeacherManagementService;
import eapli.base.teachermanagement.repositories.TeacherRepository;
import eapli.framework.util.Utility;
import eapli.framework.validations.Invariants;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Registry of all authz service objects. Helper class for scenarios without
 * spring Dependency Injection. In order to properly use the registry it is
 * necessary to call its
 * {@link AuthzRegistry#configure(UserRepository, PasswordPolicy, PasswordEncoder, TeacherRepository, StudentRepository, CourseRepository, EnrollmentRepository, MeetingRepository , BoardRepository, ClassRepository, ExamRepository, QuestionRepository,QuestionTypeRepository, GradeRepository)
 * configure} method in the start of the application to inject the proper
 * {@link UserRepository
 * UserRepository} implementation.
 * <p>
 *
 * For instance,
 *
 * <pre>
 * <code>
 *    public static void main(final String[] args) {
 *       AuthzRegistry.configure(PersistenceContext.repositories().users(),
 *               new CafeteriaPasswordPolicy(), new PlainTextEncoder());
 *
 *       new ECafeteriaBackoffice().run(args);
 *   }
 * </code>
 * </pre>
 *
 * Afterwards, in order to use these objects, you just need to grab the
 * singleton from the registry, e.g.
 *
 * <pre>
 * <code>
 * public class RegisterDishTypeController implements Controller {
 *
 *   private final AuthorizationService authz = AuthzRegistry.authorizationService();
 *   private final DishTypeRepository repository = PersistenceContext.repositories().dishTypes();
 *
 *   public DishType registerDishType(final String acronym, final String description) {
 *       authz.ensureAuthenticatedUserHasAnyOf(CafeteriaRoles.POWER_USER,
 *               CafeteriaRoles.MENU_MANAGER);
 *
 *       final DishType newDishType = new DishType(acronym, description);
 *       return this.repository.save(newDishType);
 *   }
 * }
 *
 * </code>
 * </pre>
 *
 * @author Paulo Gandra de Sousa
 */
@Utility
public final class AuthzRegistry {

    private static AuthorizationService authorizationSvc;
    private static AuthenticationService authenticationService;
    private static UserManagementService userService;
    private static ListAndFindService listfind;
    private static CourseManagementService courseService;
    private static TeacherManagementService teacherService;
    private static StudentManagementService studentService;
    private static BoardManagementService boardService;
    private static EnrollmentManagementService enrollmentService;
    private static MeetingManagementService meetingService;
    private static ClassManagementService classService;
    private static ExamManagementService examService;
    private static QuestionManagementService questionService;
    private static GradeManagementService gradeService;


    private AuthzRegistry() {
        // ensure utility
    }

    /**
     * Helper method to initialize the registry in case you are not using Spring Dependency
     * Injection. This method should be called <strong>only once</strong> on application
     * startup.
     *
     * @param userRepo
     * @param policy
     * @param encoder
     */
    public static void configure(final UserRepository userRepo, final PasswordPolicy policy,
                                 final PasswordEncoder encoder, TeacherRepository teacherRepository, StudentRepository studentRepository, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository, MeetingRepository meetingRepository, BoardRepository boardRepository, ClassRepository classRepository, ExamRepository examRepository, QuestionRepository questionRepository, QuestionTypeRepository questionTypeRepository, GradeRepository gradeRepository) {
        authorizationSvc = new AuthorizationService();
        authenticationService = new AuthenticationService(userRepo, authorizationSvc, policy, encoder);
        userService = new UserManagementService(userRepo, policy, encoder, teacherRepository, studentRepository);
        listfind = new ListAndFindService(policy, encoder, teacherRepository, studentRepository,enrollmentRepository);
        courseService = new CourseManagementService(userRepo, policy, encoder, teacherRepository, studentRepository, courseRepository,enrollmentRepository);
        teacherService = new TeacherManagementService(teacherRepository, policy, encoder);
        studentService = new StudentManagementService(studentRepository, policy, encoder);
        enrollmentService = new EnrollmentManagementService(enrollmentRepository);
        boardService = new BoardManagementService(userRepo, boardRepository);
        meetingService = new MeetingManagementService( userRepo, policy, encoder,teacherRepository, studentRepository,meetingRepository, classRepository, enrollmentRepository);
        classService = new ClassManagementService(userRepo, classRepository);
        examService = new ExamManagementService(examRepository, questionRepository, courseRepository, gradeRepository);
        questionService = new QuestionManagementService(questionRepository, questionTypeRepository);
        gradeService = new GradeManagementService(gradeRepository, courseRepository);
    }

    /**
     * @return the {@link UserManagementService}
     */
    public static UserManagementService userService() {
        Invariants.nonNull(userService);
        return userService;
    }

    /**
     * @return the {@link ListAndFindService}
     */
    public static ListAndFindService listAndFindService() {
        Invariants.nonNull(listfind);
        return listfind;
    }


    /**
     * @return the {@link CourseManagementService}
     */
    public static CourseManagementService courseService() {
        Invariants.nonNull(courseService);
        return courseService;
    }

    /**
     * @return the {@link TeacherManagementService}
     */
    public static TeacherManagementService teacherService() {
        Invariants.nonNull(teacherService);
        return teacherService;
    }

    /**
     * @return the {@link StudentManagementService}
     */
    public static StudentManagementService studentService() {
        Invariants.nonNull(studentService);
        return studentService;
    }
    /**
     * @return the {@link AuthenticationService}
     */
    public static AuthenticationService authenticationService() {
        Invariants.nonNull(authenticationService);
        return authenticationService;
    }

    /**
     * @return the {@link AuthorizationService}
     */
    public static AuthorizationService authorizationService() {
        Invariants.nonNull(authorizationSvc);
        return authorizationSvc;
    }

    /**
     * @return the {@link EnrollmentManagementService}
     */
    public static EnrollmentManagementService enrollmentService() {
        Invariants.nonNull(enrollmentService);
        return enrollmentService;
    }

    /**
     *
     * @return the {@link MeetingManagementService}
     */
    public static MeetingManagementService meetingService() {
        Invariants.nonNull(meetingService);
        return meetingService;
    }

    /**
     *
     * @return the {@link BoardManagementService}
     */

    public static BoardManagementService boardService() {
        Invariants.nonNull(boardService);
        return boardService;
    }

    /**
     *
     * @return the {@link ExamManagementService}
     */
    public static ExamManagementService examService() {
        Invariants.nonNull(examService);
        return examService;
    }

    /**
     *
     * @return the {@link QuestionManagementService}
     */

    public static QuestionManagementService questionService() {
        Invariants.nonNull(questionService);
        return questionService;
    }

    /**
     *
     * @return the {@link GradeManagementService}
     */

    public static GradeManagementService gradeService() {
        Invariants.nonNull(gradeService);
        return gradeService;
    }
}
