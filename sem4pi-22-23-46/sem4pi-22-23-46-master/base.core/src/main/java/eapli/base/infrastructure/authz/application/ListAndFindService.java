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

import eapli.base.enrollmentmanagement.domain.Enrollment;
import eapli.base.enrollmentmanagement.repositories.EnrollmentRepository;
import eapli.base.infrastructure.authz.domain.model.*;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.studentmanagement.domain.MechanographicNumber;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.studentmanagement.repositories.StudentRepository;
import eapli.base.teachermanagement.domain.Acronym;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.base.teachermanagement.repositories.TeacherRepository;
import eapli.base.usermanagement.domain.EcourseRoles;
import eapli.framework.time.util.CurrentTimeCalendars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Optional;
import java.util.Set;

/**
 * List and Find Service. Provides the typical application use cases for
 * managing {@link Teacher}, e.g.,  listing, searching. [Testing Purpose]
 * <p>
 * * List and Find Service. Provides the typical application use cases for
 * * managing {@link Student}, e.g.,  listing, searching. [Testing Purpose]
 /**
 *
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */
@Component
public class ListAndFindService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PasswordPolicy policy;
    private final PasswordEncoder encoder;

    /**
     *
     * @param policy
     * @param encoder
     * @param teacherRepository
     * @param studentRepository
     */
    @Autowired
    public ListAndFindService(final PasswordPolicy policy,
                              final PasswordEncoder encoder, final TeacherRepository teacherRepository, final StudentRepository studentRepository, final EnrollmentRepository enrollmentRepository) {
        this.policy = policy;
        this.encoder = encoder;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }


    /**
     * @return all teachers no matter their info
     */
    public Iterable<Teacher> allTeachers() {
        return teacherRepository.findAll();
    }

    /**
     * @return all students no matter their info
     */
    public Iterable<Student> allStudents() {
        return studentRepository.findAll();
    }

    /**
     * @return all enrollments no matter their status
     */
    public Iterable<Enrollment> allEnrollments() {
        return enrollmentRepository.findAll();
    }




    /**
     * Looks up a student by its acronym.
     *
     * @param id
     * @return an Optional which value is the user with the desired identify. an
     * empty Optional if there is no user with that email
     */
    public Optional<Student> userOfIdentity(final MechanographicNumber id) {
        return studentRepository.ofIdentity(id);
    }

    /**
     * Looks up a teacher by its acronym.
     *
     * @param id
     * @return an Optional which value is the user with the desired identify. an
     * empty Optional if there is no user with that email
     */
    public Optional<Teacher> userOfIdentity(final Acronym id) {
        return teacherRepository.ofIdentity(id);
    }


}
