package eapli.base.teachermanagement.application;

import eapli.base.infrastructure.authz.application.PasswordPolicy;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.teachermanagement.domain.Acronym;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.base.infrastructure.authz.domain.model.VatID;
import eapli.base.teachermanagement.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * Teacher Management Service. Provides the typical application use cases for
 * managing {@link SystemUser}, e.g., adding, deactivating, listing, searching.
 *
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */

@Component
public class TeacherManagementService {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder encoder;
    private final PasswordPolicy policy;

    /**
     * @param teacherRepo
     * @param encoder
     * @param policy
     */
    @Autowired
    public TeacherManagementService(final TeacherRepository teacherRepo, final PasswordPolicy policy,
                                    final PasswordEncoder encoder) {
        teacherRepository = teacherRepo;
        this.policy = policy;
        this.encoder = encoder;
    }

    /**
     * Registers a new teacher in the system after a user is created
     * was created.
     *
     * @param user
     * @param vatID
     * @param day
     * @param month
     * @param year
     * @param acronym
     * @return the new Teacher
     */
//    @Transactional
//    public Teacher registerNewTeacher(final SystemUser user, final String vatID, final int day, final int month, final int year, final String acronym) {
//        final var newTeacher = new Teacher(user, VatID.valueOf(vatID), new BirthDate(day, month, year), Acronym.valueOf(acronym));
//        return teacherRepository.save(newTeacher);
//    }


    /**
     * @return all teachers
     */
    public Iterable<Teacher> allTeachers() {
        return teacherRepository.findAll();
    }

    /**
     * Looks up a teacher by its vatID
     *
     * @param id
     * @return an Optional which value is the teacher with the desired identifyer. an
     * empty Optional if there is no user with that vatID
     */
    public Optional<Teacher> TeacherOfIdentity(final Acronym id) {
        return teacherRepository.ofIdentity(id);
    }

}
