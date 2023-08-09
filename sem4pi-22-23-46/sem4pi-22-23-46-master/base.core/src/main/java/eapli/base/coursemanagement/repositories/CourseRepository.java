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
package eapli.base.coursemanagement.repositories;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.coursemanagement.domain.CourseState;
import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.repositories.DomainRepository;


import java.util.Optional;

/**
 *
 * @author Pedro Sousa 1201326@isep.ipp.pt on 08/05/2023
 */
public interface CourseRepository
        extends DomainRepository<CourseCode, Course> {

    /**
     * Finds all *******TO COMPLETE***********
     *
     * @return an iterable with all active students
     */
    Iterable<Course> findAllActiveCourses();

    /**
     * returns the course with the given code
     *
     * @param code
     * @return
     */

    Optional<Course> ofIdentity (CourseCode code);


    /**
     * Finds all Courses
     *
     * @return an iterable with all courses
     */
    Iterable<Course> findAll();
    Iterable<Course> findAll(boolean b);

    Iterable<Course> findByEnrollState(boolean b);

    Iterable<Course> findByClosedState(boolean b);

    Iterable<Course> findByOpenedState();

    Iterable<Course> findByEnrollState();

    Iterable<Course> findBySystemUser(SystemUser systemUser);


    Boolean checkCourseCode(CourseCode courseCode);
}
