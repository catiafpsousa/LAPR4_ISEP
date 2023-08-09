/*
 * Copyright (c) 2013-2023 the original author or authors.
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
package eapli.base.studentmanagement.repositories;

import eapli.base.clientusermanagement.domain.MecanographicNumber;
import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.base.studentmanagement.domain.Student;
import eapli.base.studentmanagement.domain.MechanographicNumber;
import eapli.framework.domain.repositories.DomainRepository;

import java.util.Optional;

/**
 * @author Pedro Sousa 1201326@isep.ipp.pt on 08/05/2023
 */
public interface StudentRepository
        extends DomainRepository<MechanographicNumber, Student> {

    /**
     * returns the student  with the given email
     *
     * @param email
     * @return the student  with the given email
     */
    Optional<Student> findByEmail(EmailAddress email);

    /**
     * returns the student with the given mecanographic number
     *
     * @param number
     * @return
     */
    Optional<Student> ofIdentity(MecanographicNumber number);

    /**
     * Finds all students (users) in active state
     *
     * @return an iterable with all active students
     */
    Iterable<Student> findAllActiveStudents();

    /**
     * Finds all students (users)
     *
     * @return an iterable with all  students
     */
    Iterable<Student> findAll();



}
