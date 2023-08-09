/*
 * Copyright (c) 2013-2023 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package eapli.base.coursemanagement.application;

import eapli.base.coursemanagement.domain.Course;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.usermanagement.domain.EcourseRoles;
import eapli.framework.application.UseCaseController;

/**
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */
@UseCaseController
public class CloseEnrollmentsInCourseController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final CourseManagementService courseSvc = AuthzRegistry.courseService();



    public Iterable<Course> enrollingCourses() {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.POWER_USER, EcourseRoles.ADMIN);
        return courseSvc.enrollingCourses();
    }

    public Course closeEnrollments(Course course) {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.POWER_USER, EcourseRoles.ADMIN);
        return courseSvc.closeEnrollments(course);
    }


}
