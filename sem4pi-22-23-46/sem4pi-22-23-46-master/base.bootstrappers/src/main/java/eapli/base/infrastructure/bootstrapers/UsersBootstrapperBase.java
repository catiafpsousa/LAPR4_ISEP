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
package eapli.base.infrastructure.bootstrapers;

import java.util.Set;

import eapli.base.coursemanagement.application.*;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.coursemanagement.domain.CourseCode;
import eapli.base.infrastructure.authz.domain.model.EmailAddress;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.framework.time.util.CurrentTimeCalendars;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eapli.base.usermanagement.application.RegisterUserController;
import eapli.base.usermanagement.application.ListUsersController;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.base.infrastructure.authz.domain.model.Role;
import eapli.base.infrastructure.authz.domain.model.SystemUser;

public class UsersBootstrapperBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersBootstrapperBase.class);

    final RegisterUserController userController = new RegisterUserController();
    final ListUsersController listUserController = new ListUsersController();

    final CreateCourseController courseController = new CreateCourseController();
    final ListCoursesController listCoursesController = new ListCoursesController();
    final OpenCourseController openCourseController = new OpenCourseController();
    final OpenEnrollmentsInCourseController openEnrollmentsInCourseController = new OpenEnrollmentsInCourseController();

    final SetCourseTeachersController setCourseTeachersController = new SetCourseTeachersController();


    public UsersBootstrapperBase() {
        super();
    }

    /**
     * @param email
     * @param password
     * @param fullName
     * @param shortName
     * @param roles
     */
    protected SystemUser registerUser(final String email, final String password, final String fullName,
                                      final String shortName, final String vatID, final int day, final int month, final int year, final Set<Role> roles) {
        SystemUser u = null;
        try {
            u = userController.addUser(email, password, fullName, shortName, vatID, day, month, year, roles);
            LOGGER.debug("»»» %s", email);
        } catch (final IntegrityViolationException | ConcurrencyException e) {
            // assuming it is just a primary key violation due to the tentative
            // of inserting a duplicated user. let's just lookup that user
            u = listUserController.find(EmailAddress.valueOf(email)).orElseThrow(() -> e);
        }
        return u;
    }

    protected Course createCourse(final String code, final String name, final String description,
                                  final int min, final int max) {
        Course c = null;
        try {
            c = courseController.addCourse(code, name, description, min, max, CurrentTimeCalendars.now());
            LOGGER.debug("»»» %s", code);
        } catch (final IntegrityViolationException | ConcurrencyException e) {
            // assuming it is just a primary key violation due to the tentative
            // of inserting a duplicated user. let's just lookup that user{
            c = listCoursesController.findCourse(CourseCode.valueOf(code)).orElseThrow(() ->e);
        }
        return c;
    }

    public SetCourseTeachersController setCourseTeachersController (){
        return this.setCourseTeachersController;
    }

    public OpenCourseController openCourseController(){
        return this.openCourseController;
    }

    public OpenEnrollmentsInCourseController openEnrollmentsInCourseController(){
        return this.openEnrollmentsInCourseController;
    }

    public Course setTeacherInCharge (Teacher t, Course c){
        c = setCourseTeachersController().setCourseTeacherInCharge(t, c);
        return c;
    }

}
