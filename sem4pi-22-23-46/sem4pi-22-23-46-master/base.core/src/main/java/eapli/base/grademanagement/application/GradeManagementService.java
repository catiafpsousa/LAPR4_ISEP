package eapli.base.grademanagement.application;

import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.coursemanagement.repositories.CourseRepository;
import eapli.base.exammanagement.domain.Exam;
import eapli.base.grademanagement.domain.ExamGrade;
import eapli.base.grademanagement.repositories.GradeRepository;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

public class GradeManagementService {

    private final GradeRepository gradeRepository;

    private final CourseRepository courseRepository;

    public GradeManagementService(final GradeRepository gradeRepo, final CourseRepository courseRepo) {
        this.gradeRepository = gradeRepo;
        this.courseRepository = courseRepo;
    }

    @Transactional
    public ExamGrade saveGrade(SystemUser systemUser, Exam exam, Double grade) {
        return gradeRepository.save(new ExamGrade(systemUser, exam, grade, Calendar.getInstance()));
    }

    public Iterable<ExamGrade> allGradesByStudent(SystemUser student) {
        return gradeRepository.findByStudent(student);
    }


    public Iterable<ExamGrade> allGradesByCourse(CourseCode course) {

        return gradeRepository.findByCourse(course);
    }
}
