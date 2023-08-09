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

import eapli.base.studentmanagement.domain.MechanographicNumber;
import eapli.base.teachermanagement.domain.Acronym;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.base.usermanagement.application.ListAndFindController;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.ListWidget;
import eapli.base.studentmanagement.domain.Student;


import java.util.Collections;
import java.util.Optional;

/**
 *
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */
@SuppressWarnings({ "squid:S106" })
public class FindTeacherUI {

    private ListAndFindController theController = new ListAndFindController();

    protected boolean doShow() {
        final String input = Console.readLine("Insert Teacher Acronym:");
        Acronym acronym = Acronym.valueOf(input);

        Optional<Teacher> optional = this.theController.findTeacher(acronym);
        Teacher teacher = optional.get();

        new ListWidget(this.listHeader(), Collections.singleton(teacher), this.elementPrinter()).show();

        return false;
    }

    protected String listHeader() {
        return String.format("#  %-10s%-50s%-25s%-30s%-10s", "ACRONYM", "FULL NAME", "BIRTHDATE", "VAT", "ACTIVE STATUS");
    }


    protected TeacherPrinter elementPrinter() {
        return new TeacherPrinter();
    }

    public String headline() {
        return "Find TEACHER";
    }
}
