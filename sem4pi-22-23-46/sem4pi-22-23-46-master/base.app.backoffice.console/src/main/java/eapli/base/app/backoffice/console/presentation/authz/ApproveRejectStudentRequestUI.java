package eapli.base.app.backoffice.console.presentation.authz;
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

import eapli.base.enrollmentmanagement.application.ApproveRejectStudentRequestController;
import eapli.base.enrollmentmanagement.domain.Enrollment;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.usermanagement.application.ActivateUserController;
import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */
@SuppressWarnings("squid:S106")
public class ApproveRejectStudentRequestUI extends AbstractUI {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApproveRejectStudentRequestUI.class);

    private final ApproveRejectStudentRequestController theController = new ApproveRejectStudentRequestController();

    @Override
    protected boolean doShow() {
        final List<Enrollment> pendingEnrollments = new ArrayList<>();
        System.out.println("Listing Pending Requests:");
        final Iterable<Enrollment> iterable = this.theController.pendingRequests();
        if (!iterable.iterator().hasNext()) {
            System.out.println("There are no pending requests...");
        } else {
            int cont = 1;
            System.out.println("SELECT Request to Approve/Reject\n");
            System.out.printf("%-6s%-20s%-50s%-10s%-20s%n","No.", "MECHANOGRAPHIC No.", "NAME", "COURSE", "STATUS");
            for (final Enrollment enrollment : iterable) {
                pendingEnrollments.add(enrollment);
                System.out.printf("%-6d%-20s%-50s%-10s%-20s%n", cont, enrollment.student().identity(), enrollment.student().user().fullName(),
                        enrollment.course().courseCode(), enrollment.enrollmentStatus());
                cont++;
            }
            final int option = Console.readInteger("\nEnter Enrollment No. to approve/reject or 0 to finish\n");
            if (option == 0) {
                System.out.println("No Enrollment selected");
            } else {
                final int action = Console.readInteger("[1]-Approve    [2]-Reject    [0]-Cancel ");
                if (action == 0) {
                    System.out.println("Action canceled");
                } else if (action == 1) {
                    try {
                        this.theController.approveStudentRequest(pendingEnrollments.get(option - 1));
                    } catch (IntegrityViolationException | ConcurrencyException ex) {
                        LOGGER.error("Error performing the operation", ex);
                        System.out.println(
                                "Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");
                    }
                } else {
                    try {
                        this.theController.rejectStudentRequest(pendingEnrollments.get(option - 1));
                    } catch (IntegrityViolationException | ConcurrencyException ex) {
                        LOGGER.error("Error performing the operation", ex);
                        System.out.println(
                                "Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String headline() {
        return "Approve/Reject Student Requests";
    }
}

