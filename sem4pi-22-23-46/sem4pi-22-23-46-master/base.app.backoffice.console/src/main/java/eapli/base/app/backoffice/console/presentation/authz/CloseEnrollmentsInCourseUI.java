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
package eapli.base.app.backoffice.console.presentation.authz;

import eapli.base.coursemanagement.application.CloseEnrollmentsInCourseController;
import eapli.base.coursemanagement.domain.Course;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */
@SuppressWarnings("squid:S106")
public class CloseEnrollmentsInCourseUI extends AbstractUI {
    private static final Logger LOGGER = LoggerFactory.getLogger(CloseEnrollmentsInCourseUI.class);

    private final CloseEnrollmentsInCourseController theController = new CloseEnrollmentsInCourseController();

    @Override
    protected boolean doShow() {
        final List<Course> list = new ArrayList<>();
        final Iterable<Course> iterable = this.theController.enrollingCourses();
        if (!iterable.iterator().hasNext()) {
            System.out.println("There are no Courses");
        } else {
            int cont = 1;
            System.out.println("SELECT Course to Close Enrollments\n");
            System.out.printf("%-6s%-35s%-15s%-10s%n", "No.:", "Code", "Name", "State");
            for (final Course course : iterable) {
                list.add(course);
                System.out.printf("%-6d%-35s%-15s%-10s%n", cont, course.identity(), course.courseName(),
                        course.courseState());
                cont++;
            }
            final int option = Console.readInteger("Enter Course to CLOSE ENROLLMENTS and START PROGRESS or 0 to finish ");
            if (option == 0) {
                System.out.println("No Course selected");
            } else {
                try {
                    this.theController.closeEnrollments(list.get(option - 1));
                } catch (IntegrityViolationException | ConcurrencyException ex) {
                    LOGGER.error("Error performing the operation", ex);
                    System.out.println(
                            "Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");
                }
            }
        }
        return true;
    }

    @Override
    public String headline() {
        return "CLOSE COURSE ENROLL";
    }
}
