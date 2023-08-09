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
package eapli.base.app.backoffice.console.presentation.authz;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import eapli.base.coursemanagement.application.CreateCourseController;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;

import eapli.framework.time.util.CurrentTimeCalendars;

/**
 *
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */
public class CreateCourseUI extends AbstractUI {

    private final CreateCourseController theController = new CreateCourseController();

    @Override
    protected boolean doShow() {

        final String code = Console.readLine("Code");
        final String name = Console.readLine("Name");
        final String description = Console.readLine("Description");
        final int min = Console.readInteger("Minimum number of Students");
        final int max = Console.readInteger("Maximum number of Students");


        try {
            this.theController.addCourse(code, name, description, min, max, CurrentTimeCalendars.now());
        } catch (final IntegrityViolationException | ConcurrencyException e) {
            System.out.println("That Course already exists.");
        }

        return false;
    }

    @Override
    public String headline() {
        return "Add Course";
    }
}
