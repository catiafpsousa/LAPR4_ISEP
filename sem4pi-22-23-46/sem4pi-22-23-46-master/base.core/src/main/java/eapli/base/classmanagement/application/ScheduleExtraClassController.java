package eapli.base.classmanagement.application;

import eapli.base.classmanagement.domain.ExtraClassBuilder;
import eapli.base.classmanagement.domain.ExtraordinaryClass;
import eapli.base.classmanagement.repositories.ClassRepository;
import eapli.base.coursemanagement.application.CourseManagementService;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.enrollmentmanagement.application.EnrollmentManagementService;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.studentmanagement.repositories.StudentRepository;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.base.teachermanagement.repositories.TeacherRepository;
import eapli.base.usermanagement.domain.EcourseRoles;
import eapli.framework.application.UseCaseController;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@UseCaseController
public class ScheduleExtraClassController {

    private final AuthorizationService authz= AuthzRegistry.authorizationService();
    private final CourseManagementService courseSrv=AuthzRegistry.courseService();
    private final EnrollmentManagementService enrollmentManagementService= AuthzRegistry.enrollmentService();
    private final ClassRepository classRepository = PersistenceContext.repositories().classes();
    private final ExtraClassBuilder builder = new ExtraClassBuilder();
    private final TeacherRepository repoTeacher = PersistenceContext.repositories().teachers();
    private final StudentRepository studentRepository=  PersistenceContext.repositories().students();

    public Iterable<Course> listCourses(){
        return courseSrv.allCoursesBySystemUser(systemUser());
    }


    public boolean checkAvailability(Calendar date, LocalTime startTime, int duration){
        return classRepository.checkScheduleAvailability(currentTeacher().orElseThrow(IllegalStateException::new), date, startTime, startTime.plusMinutes(duration));
    }
    
    public Iterable <Student> listStudents (Course course){
        return enrollmentManagementService.findAllStudentsByCourse(course);
    }

    @Transactional
    public ExtraordinaryClass schedulesExtraClass(String title, int duration, Calendar date, LocalTime time, List <Student> participants, Course course){
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.TEACHER);
        Teacher teacher = currentTeacher().orElseThrow(IllegalStateException::new);
        ExtraordinaryClass extraClass = builder.withTitle(title).withDuration(duration).withDay(date).withTime(time).withTeacher(teacher).withParticipants(participants).withCourse(course).build();
        return classRepository.save(extraClass);
    }


    private Optional<Teacher> currentTeacher(){
        return repoTeacher.findByEmail(systemUser().identity());
    }

    public SystemUser systemUser() {
        return authz.session().get().authenticatedUser();
    }
}
