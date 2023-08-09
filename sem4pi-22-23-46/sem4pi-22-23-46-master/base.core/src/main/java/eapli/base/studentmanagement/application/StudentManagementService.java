package eapli.base.studentmanagement.application;

import eapli.base.infrastructure.authz.application.PasswordPolicy;
import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.studentmanagement.domain.MechanographicNumber;
import eapli.base.studentmanagement.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * Student Management Service. Provides the typical application use cases for
 * managing {@link Student}, e.g., adding, deactivating, listing, searching.
 *
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */

@Component
public class StudentManagementService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder encoder;
    private final PasswordPolicy policy;

    /**
     * @param userRepo
     * @param encoder
     * @param policy
     */
    @Autowired
    public StudentManagementService(final StudentRepository userRepo, final PasswordPolicy policy,
                                    final PasswordEncoder encoder) {
        studentRepository = userRepo;
        this.policy = policy;
        this.encoder = encoder;
    }


    /**
     * @return all students
     */
    public Iterable<Student> allStudents() {
        return studentRepository.findAll();
    }

    /**
     * Looks up a student by its mechanographic number
     *
     * @param mechanographicNumber
     * @return an Optional which value is the student with the desired identifyer. an
     * empty Optional if there is no user with that mechanographic number
     */
    public Optional<Student> StudentOfIdentity(final MechanographicNumber mechanographicNumber) {
        return studentRepository.ofIdentity(mechanographicNumber);
    }

    /**
     * Looks up a student User by its mechanographic number
     *
     * @param mechanographicNumber
     * @return an Optional which value is the User related to the student with the desired identifyer. an
     * empty Optional if there is no user with that mechanographic number
     */
    public Optional<SystemUser> userStudentOfIdentity(final MechanographicNumber mechanographicNumber) {
        Optional<Student> student = studentRepository.ofIdentity(mechanographicNumber);
        if (!student.isPresent())
            return Optional.empty();
        else {
            Optional<SystemUser> userStudent = Optional.ofNullable(student.get().user());
        return userStudent;}
    }

    /**
     * Looks up a student User by its email address
     *
     * @param emailAddress
     * @return an Optional student value is the User related to the student with the desired identifyer. an
     * empty Optional if there is no user with that emaiol
     */
    public Optional<Student> studentOfUser(final EmailAddress emailAddress) {
        Optional<Student> student = studentRepository.findByEmail(emailAddress);
        return student;
    }

    public Optional<Student> findByEmail(final EmailAddress email) {
        return studentRepository.findByEmail(email);
    }
}


