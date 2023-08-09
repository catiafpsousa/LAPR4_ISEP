package eapli.base.classmanagement.application;

import eapli.base.classmanagement.domain.RecurringClass;
import eapli.base.classmanagement.repositories.ClassRepository;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.base.teachermanagement.repositories.TeacherRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Optional;

public class UpdateClassScheduleController {

    private final AuthorizationService authz= AuthzRegistry.authorizationService();
    private final ClassRepository classRepository = PersistenceContext.repositories().classes();
    private final TeacherRepository repoTeacher = PersistenceContext.repositories().teachers();



//    public Iterable<Course> listCourses(){
//        return courseSrv.allCoursesBySystemUser(systemUser());
//    }

    public Iterable<RecurringClass> listRecurringClasses(){
        return classRepository.findRecurringClassesOfTeacher(currentTeacher().orElseThrow(IllegalStateException::new));
    }

    private Optional<Teacher> currentTeacher(){
        return repoTeacher.findByEmail(systemUser().identity());
    }

    public SystemUser systemUser() {
        return authz.session().get().authenticatedUser();
    }

    public boolean checkAvailability(Calendar newClassDate, LocalTime startTime, int duration) {
        return classRepository.checkScheduleAvailability(currentTeacher().orElseThrow(IllegalStateException::new), newClassDate, startTime, startTime.plusMinutes(duration));
    }

    @Transactional
    public RecurringClass updateClassSchedule(RecurringClass chosenClass, Calendar classDateToChange, Calendar newClassDate, LocalTime time, int duration) {
        chosenClass.addReschedule(classDateToChange, newClassDate, time, duration);
        return classRepository.save(chosenClass);


    }
}
