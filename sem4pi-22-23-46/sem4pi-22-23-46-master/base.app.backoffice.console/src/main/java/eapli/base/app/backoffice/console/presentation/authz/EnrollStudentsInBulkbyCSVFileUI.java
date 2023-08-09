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

import eapli.base.coursemanagement.application.SetCourseTeachersController;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.enrollmentmanagement.application.EnrollStudentsInBulkbyCSVFileController;
import eapli.base.teachermanagement.domain.Teacher;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */
@SuppressWarnings("squid:S106")
public class EnrollStudentsInBulkbyCSVFileUI extends AbstractUI {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnrollStudentsInBulkbyCSVFileUI.class);

    /**
     * Controller responsible for loading the cCSV Enrollment File
     */
    private final EnrollStudentsInBulkbyCSVFileController theController = new EnrollStudentsInBulkbyCSVFileController();

    /**
     * Message to ask the user to introduce the file's pathname
     */
    private static final String INTRODUCE_PATH = "Please enter the pathname of the file:";

    /**
     * Message to inform the user that the file was successfully read
     */
    private static final String IO_SUCCESS_ANSWER = "The file was read with success";

    /**
     * Message to inform the user that the file could not be read
     */
    private static final String IOEXCEPTION_ANSWER = "\n\tWARNING: The file could not be read, please ensure that the file you want to read is valid";

    /**
     * Message to inform the user that the file could not be read
     */
    private static final String FILENOTFOUNDEXCEPTION_ANSWER = "\n\tWARNING: The file could not be found, please ensure that the path and/or the filename is valid\n";




    @Override
    protected boolean doShow() {

        List<String[]> enrollmentList = null;
        try {
            String filename = Console.readLine(INTRODUCE_PATH);
            enrollmentList = theController.readCsvFile(filename);
        } catch (FileNotFoundException i) {
            System.out.println(FILENOTFOUNDEXCEPTION_ANSWER);
        } catch (Exception e) {
            System.out.println(e.getMessage() + " Caused by " + e.getCause());
            System.out.println(IOEXCEPTION_ANSWER);
        }

        this.theController.createEnrollmentFromList(enrollmentList);

        return true;
    }

    @Override
    public String headline() {
        return "STUDENTS BULK ENROLL";
    }
}
