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
package eapli.base.app.backoffice.console.presentation;

import eapli.base.app.backoffice.console.presentation.authz.*;
import eapli.base.app.common.console.presentation.authz.MyUserMenu;
import eapli.base.Application;
//import eapli.base.app.backoffice.console.presentation.clientuser.AcceptRefuseSignupRequestAction;
import eapli.base.app.common.console.presentation.meeting.AcceptOrRejectMeetingRequestAction;
import eapli.base.app.common.console.presentation.meeting.ListParticipantsInMeetingAction;
import eapli.base.app.common.console.presentation.meeting.CancelMeetingAction;
import eapli.base.app.common.console.presentation.meeting.ScheduleMeetingAction;
import eapli.base.app.common.console.presentation.sharedboard.*;
import eapli.base.usermanagement.domain.EcourseRoles;
import eapli.framework.actions.Actions;
import eapli.framework.actions.menu.Menu;
import eapli.framework.actions.menu.MenuItem;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.ExitWithMessageAction;
import eapli.framework.presentation.console.ShowMessageAction;
import eapli.framework.presentation.console.menu.HorizontalMenuRenderer;
import eapli.framework.presentation.console.menu.MenuItemRenderer;
import eapli.framework.presentation.console.menu.MenuRenderer;
import eapli.framework.presentation.console.menu.VerticalMenuRenderer;

/**
 *
 * @author Paulo Gandra Sousa
 */
public class MainMenu extends AbstractUI {

    private static final String RETURN_LABEL = "Return ";

    private static final int EXIT_OPTION = 0;

    // USERS
    private static final int ADD_USER_OPTION = 1;
    private static final int LIST_USERS_OPTION = 2;
    private static final int DEACTIVATE_USER_OPTION = 3;
    private static final int ACTIVATE_USER_OPTION = 4;
    private static final int LIST_ALL_STUDENTS_OPTION = 5;
    private static final int LIST_ALL_TEACHERS_OPTION = 6;
    private static final int FIND_STUDENT_OPTION = 7;
    private static final int FIND_TEACHER_OPTION = 8;

    // COURSES
    private static final int CREATE_COURSE = 1;
    private static final int LIST_COURSES = 2;
    private static final int OPEN_COURSE = 3;
    private static final int CLOSE_COURSE = 4;
    private static final int OPEN_ENROLL = 5;
    private static final int CLOSE_ENROLL = 6;
    private static final int SET_TEACHERS = 7;
    private static final int SET_TEACHER_IN_CHARGE = 8;

    // ENROLLMENTS
    private static final int ENROLL_IN_BULK_BY_CSV = 1;
    private static final int LIST_ENROLLMENTS = 2;
    private static final int APPROVE_REJECT_ENROLLMENTS = 3;

//METINGS

    private static final int SCHEDULE_MEETING = 1;


    private static final int CANCEL_MEETING = 2;

    // MAIN MENU
    private static final int MY_USER_OPTION = 1;
    private static final int USERS_OPTION = 2;
    private static final int COURSES_OPTION = 3;
    private static final int ENROLLMENT_OPTION = 4;
    private static final int SETTINGS_OPTION = 5;
    private static final int MEETINGS_OPTION = 6;


    private static final String SEPARATOR_LABEL = "--------------";
    private static final int LIST_PARTICIPANTS_OPTION = 5;
    private static final int ARCHIVE_BOARD_OPTION = 6;
    private static final int BOARD_OPTION = 7;
    private static final int CREATE_BOARD_OPTION = 1;
    private static final int CREATE_POSTIT_OPTION = 2;
    private static final int UPDATE_OPTION = 3;
    private static final int UNDO_LAST_CHANGE_OPTION = 4;
    private static final int SHARE_BOARD_OPTION = 5;
    private static final int RESTORE_BOARD_OPTION = 7;
    private static final int ACCEPT_REJECT_MEETING_OPTION = 7;


    private final AuthorizationService authz = AuthzRegistry.authorizationService();

    @Override
    public boolean show() {
        drawFormTitle();
        return doShow();
    }

    /**
     * @return true if the user selected the exit option
     */
    @Override
    public boolean doShow() {
        final Menu menu = buildMainMenu();
        final MenuRenderer renderer;
        if (Application.settings().isMenuLayoutHorizontal()) {
            renderer = new HorizontalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
        } else {
            renderer = new VerticalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
        }
        return renderer.render();
    }

    @Override
    public String headline() {

        return authz.session().map(s -> "Base [ @" + s.authenticatedUser().identity() + " ]")
                .orElse("Base [ ==Anonymous== ]");
    }

    private Menu buildMainMenu() {
        final Menu mainMenu = new Menu();

        final Menu myUserMenu = new MyUserMenu();
        mainMenu.addSubMenu(MY_USER_OPTION, myUserMenu);



        if (authz.isAuthenticatedUserAuthorizedTo(EcourseRoles.POWER_USER, EcourseRoles.ADMIN)) {
            final Menu usersMenu = buildUsersMenu();
            mainMenu.addSubMenu(USERS_OPTION, usersMenu);
            final Menu coursesMenu = buildCoursesMenu();
            mainMenu.addSubMenu(COURSES_OPTION, coursesMenu);
            final Menu enrollMenu = buildEnrollmentMenu();
            mainMenu.addSubMenu(ENROLLMENT_OPTION, enrollMenu);
            final Menu settingsMenu = buildAdminSettingsMenu();
            mainMenu.addSubMenu(SETTINGS_OPTION, settingsMenu);
            final Menu meetingMenu = buildMeetingMenu();
            mainMenu.addSubMenu(MEETINGS_OPTION,meetingMenu);
            final Menu boardMenu = buildBoardMenu();
            mainMenu.addSubMenu(BOARD_OPTION, boardMenu);

        }

        if (!Application.settings().isMenuLayoutHorizontal()) {
            mainMenu.addItem(MenuItem.separator(SEPARATOR_LABEL));
        }

        mainMenu.addItem(EXIT_OPTION, "Exit", new ExitWithMessageAction("Bye, Bye"));

        return mainMenu;
    }

    private Menu buildAdminSettingsMenu() {
        final Menu menu = new Menu("Settings >");

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    private Menu buildUsersMenu() {
        final Menu menu = new Menu("Users >");

        menu.addItem(ADD_USER_OPTION, "Add User", new RegisterUserAction());
        menu.addItem(LIST_USERS_OPTION, "List all Users", new ListUsersAction());
        menu.addItem(DEACTIVATE_USER_OPTION, "Deactivate User", new DeactivateUserAction());
        menu.addItem(ACTIVATE_USER_OPTION, "Activate User", new ActivateUserAction());
        menu.addItem(LIST_ALL_STUDENTS_OPTION, "List All Students", new ListStudentsAction());
        menu.addItem(LIST_ALL_TEACHERS_OPTION, "List All Teachers", new ListTeachersAction());
        menu.addItem(FIND_STUDENT_OPTION, "Find Student by Number", new FindStudentAction());
        menu.addItem(FIND_TEACHER_OPTION, "Find Teacher by Acronym", new FindTeacherAction());

        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }


    private Menu buildCoursesMenu() {
        final Menu menu = new Menu("Courses >");

        menu.addItem(CREATE_COURSE, "Create a New Course", new CreateCourseAction());
        menu.addItem(LIST_COURSES, "List Courses", new ListCoursesAction());
        menu.addItem(OPEN_COURSE, "Open Course", new OpenCourseAction());
        menu.addItem(CLOSE_COURSE, "Close Course", new CloseCourseAction());
        menu.addItem(OPEN_ENROLL, "Open Enrolls in Course", new OpenEnrollmentsInCourseAction());
        menu.addItem(CLOSE_ENROLL, "Close Enrolls in Course - Start Progress", new CloseEnrollmentsInCourseAction());
        menu.addItem(SET_TEACHERS, "Set Course Teachers", new SetCourseTeachersAction());
        menu.addItem(SET_TEACHER_IN_CHARGE, "Set Teacher In Charge", new SetCourseTeachersInChargeAction());


        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);

        return menu;
    }

    private Menu buildEnrollmentMenu() {
        final Menu menu = new Menu("Enrollments >");

        menu.addItem(ENROLL_IN_BULK_BY_CSV, "Enroll Students in Bulk by CSV file", new EnrollStudentsInBulkbyCSVAction());
        menu.addItem(LIST_ENROLLMENTS,"List all Enrollments", new ListEnrollmentsAction());
        menu.addItem(APPROVE_REJECT_ENROLLMENTS,"Approve/Reject Student Requests", new ApproveRejectStudentRequestAction());


        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);


        return menu;
    }
    private Menu buildMeetingMenu() {
        final Menu menu = new Menu("Meetings >");

        menu.addItem(SCHEDULE_MEETING, "Schedule Meeting", new ScheduleMeetingAction());
        menu.addItem(CANCEL_MEETING, "Cancel Meeting", new CancelMeetingAction());

        menu.addItem(LIST_PARTICIPANTS_OPTION, "List Participants in Meeting", new ListParticipantsInMeetingAction());
        menu.addItem(ACCEPT_REJECT_MEETING_OPTION, "Accept or Reject Meeting Request", new AcceptOrRejectMeetingRequestAction());
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);


        return menu;
    }

    private Menu buildBoardMenu(){
        final Menu menu = new Menu("Board >");

        menu.addItem(CREATE_BOARD_OPTION, "Create Board", new CreateBoardAction());
        menu.addItem(CREATE_POSTIT_OPTION, "Create PostIt", new CreatePostItAction());
        menu.addItem(UPDATE_OPTION, "Update PostIt", new UpdatePostItAction());
        menu.addItem(UNDO_LAST_CHANGE_OPTION, "Undo Last Change", new UndoPostItLastChangeAction());
        menu.addItem(SHARE_BOARD_OPTION, "Share Board", new ShareABoardAction());
        menu.addItem(ARCHIVE_BOARD_OPTION, "Archive Board", new ArchiveBoardAction());
        menu.addItem(RESTORE_BOARD_OPTION, "Restore Board", new RestoreBoardAction());
        menu.addItem(EXIT_OPTION, RETURN_LABEL, Actions.SUCCESS);
        return menu;

    }

}
