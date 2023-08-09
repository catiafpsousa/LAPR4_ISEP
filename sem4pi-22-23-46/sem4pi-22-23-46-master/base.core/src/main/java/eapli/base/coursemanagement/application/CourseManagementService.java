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
package eapli.base.coursemanagement.application;

import eapli.base.coursemanagement.domain.*;
import eapli.base.coursemanagement.repositories.CourseRepository;
import eapli.base.enrollmentmanagement.domain.Enrollment;
import eapli.base.enrollmentmanagement.repositories.EnrollmentRepository;
import eapli.base.infrastructure.authz.application.PasswordPolicy;
import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.studentmanagement.domain.MechanographicNumber;
import eapli.base.studentmanagement.repositories.StudentRepository;
import eapli.base.teachermanagement.domain.Acronym;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.base.teachermanagement.repositories.TeacherRepository;
import eapli.framework.time.util.CurrentTimeCalendars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * Course Management Service. Provides the typical application use cases for
 * managing {@link Course}, e.g., adding, deactivating, listing, searching.
 * /**
 *
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */
@Component
public class CourseManagementService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final PasswordPolicy policy;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    /**
     *
     * @param userRepo
     * @param encoder
     * @param policy
     */
    @Autowired
    public CourseManagementService(final UserRepository userRepo, final PasswordPolicy policy,
                                   final PasswordEncoder encoder, final TeacherRepository teacherRepository, final StudentRepository studentRepository, final CourseRepository courseRepository , final EnrollmentRepository enrollmentRepository) {
        userRepository = userRepo;
        this.policy = policy;
        this.encoder = encoder;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Transactional
    public Course registerNewCourse(final String code, final String name, final String description, final int minLimit, final int maxLimit, final Calendar date) {
        Course newCourse = new Course(code, name, description, minLimit, maxLimit, date);
        return courseRepository.save(newCourse);
    }

    /**
     *
     * @return all not enroll state Courses
     */
    public Iterable<Course> notEnrollStateCourses() {
        return courseRepository.findByEnrollState(false);
    }

    /**
     *
     * @return all enroll state Courses
     */
    public Iterable<Course> enrollStateCourses() {
        return courseRepository.findByEnrollState(true);
    }

    /**
     *
     * @return all courses no matter their status
     */
    public Iterable<Course> allCourses() {
        return courseRepository.findAll();
    }

    /**
     * Looks up a course by its code.
     *
     * @param id
     * @return an Optional which value is the course with the desired code. an
     * empty Optional if there is no course with that code
     */
    public Optional<Course> courseOfIdentity(final CourseCode id) {
        Optional<Course> course = courseRepository.ofIdentity(id);
        if (!course.isPresent())
            return Optional.empty();
        else {
            Optional<Course> existingCourse = Optional.ofNullable(course.get());
            return existingCourse;}
    }

    /**
     * Closes a course. Course code must not reference the input parameter after
     * calling this method and must use the return object instead.
     *
     * @param course
     * @return the updated course.
     */
    @Transactional
    public Course closeCourse(final Course course) {
        course.close(CurrentTimeCalendars.now());
        return courseRepository.save(course);
    }

    /**
     * Opens a course. Course code must not reference the input parameter after
     * calling this method and must use the return object instead.
     *
     * @param course
     * @return the updated course.
     */
    @Transactional
    public Course openCourse(final Course course) {
        course.open(CurrentTimeCalendars.now());
        return courseRepository.save(course);
    }

    /**
     * Starts the progress of a course. Course code must not reference the input parameter after
     * calling this method and must use the return object instead.
     *
     * @param course
     * @return the updated course.
     */
    @Transactional
    public Course closeEnrollments(final Course course) {
        course.inProgress(CurrentTimeCalendars.now());
        return courseRepository.save(course);
    }

    /**
     * Starts the progress of a course. Course code must not reference the input parameter after
     * calling this method and must use the return object instead.
     *
     * @param course
     * @return the updated user.
     */
    @Transactional
    public Course openEnrollments(final Course course) {
        course.enroll(CurrentTimeCalendars.now());
        return courseRepository.save(course);
    }

    public Iterable<Course> nonClosedCourses() {
        return courseRepository.findByClosedState(false);

    }

    public Iterable<Course> closedCourses() {
        return courseRepository.findByClosedState(true);
    }

    public Iterable<Course> openedCourses() {
        return courseRepository.findByOpenedState();
    }

    public Iterable<Course> enrollingCourses() {
        return courseRepository.findByEnrollState();
    }

    @Transactional
    public Course setCourseTeachers(String selection, List<Teacher> allTeachers, Course course) {
        String[] selectionFromPrompt = selection.split(" ");

        int[] numbers = new int[selectionFromPrompt.length];
        for (int i = 0; i < selectionFromPrompt.length; i++) {
            numbers[i] = Integer.parseInt(selectionFromPrompt[i]);
        }
        List<Teacher> selectedTeachers = new ArrayList<>();
        for (int index : numbers
        ) {
            selectedTeachers.add(allTeachers.get(index - 1));
        }
        course.setCourseTeachers(selectedTeachers);
        return this.courseRepository.save(course);
    }

    public CourseRepository getCourseRepository() {
        return this.courseRepository;
    }

    public Iterable<Course>  allCoursesByAcceptedStatusAndSystemUser(final SystemUser systemUser ) {

        return enrollmentRepository.findByAcceptedStatusAndSystemUser( systemUser);
    }





    public Iterable<Course> allCoursesBySystemUser(final SystemUser systemUser ) {

        return courseRepository.findBySystemUser(systemUser);
    }



}


