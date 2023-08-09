package eapli.base.classmanagement.application;

import eapli.base.classmanagement.domain.RecurringClass;
import eapli.base.classmanagement.domain.RecurringClassBuilder;
import eapli.base.classmanagement.repositories.ClassRepository;
import eapli.base.coursemanagement.application.CourseManagementService;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.base.teachermanagement.repositories.TeacherRepository;
import eapli.base.usermanagement.domain.EcourseRoles;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Optional;

public class ScheduleRecurringClassController {

    private final AuthorizationService authz= AuthzRegistry.authorizationService();
    private final CourseManagementService courseSrv= AuthzRegistry.courseService();
    private final ClassRepository classRepository = PersistenceContext.repositories().classes();
    private final TeacherRepository repoTeacher = PersistenceContext.repositories().teachers();
    private final RecurringClassBuilder builder = new RecurringClassBuilder();


    public Iterable<Course> listCourses(){
        return courseSrv.allCoursesBySystemUser(systemUser());
    }


    public boolean checkAvailability(Calendar date, LocalTime startTime, int duration){
        return classRepository.checkScheduleAvailability(currentTeacher().orElseThrow(IllegalStateException::new), date, startTime, startTime.plusMinutes(duration));
    }

    public RecurringClass scheduleRecurringClass(String title, int duration, Calendar date, LocalTime time, Course course) {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.TEACHER);
        Teacher teacher = currentTeacher().orElseThrow(IllegalStateException::new);
        RecurringClass recurringClass = builder.withTitle(title).withDuration(duration).withDay(date).withTime(time).withTeacher(teacher).withCourse(course).build();
        return classRepository.save(recurringClass);
    }

    private Optional<Teacher> currentTeacher(){
        return repoTeacher.findByEmail(systemUser().identity());
    }

    public SystemUser systemUser() {
        return authz.session().get().authenticatedUser();
    }
}
