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

import eapli.base.teachermanagement.domain.Teacher;
import eapli.base.usermanagement.application.ListAndFindController;
import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;

/**
 *
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */
@SuppressWarnings({ "squid:S106" })
public class ListTeachersUI extends AbstractListUI<Teacher> {
    private ListAndFindController theController = new ListAndFindController();

    @Override
    public String headline() {
        return "List TEACHERS";
    }

    @Override
    protected String emptyMessage() {
        return "No data.";
    }

    @Override
    protected Iterable<Teacher> elements() {
        return theController.allTeachers();
    }

    @Override
    protected Visitor<Teacher> elementPrinter() {
        return new TeacherPrinter();
    }

    @Override
    protected String elementName() {
        return "Teacher";
    }

    @Override
    protected String listHeader() {
        return String.format("#  %-10s%-50s%-25s%-30s%-10s", "ACRONYM", "FULL NAME", "BIRTHDATE", "VAT NUMBER", "ACTIVE STATUS");
    }
}
