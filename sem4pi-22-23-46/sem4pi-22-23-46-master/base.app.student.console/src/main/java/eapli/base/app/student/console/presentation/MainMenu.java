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
package eapli.base.app.student.console.presentation;

import eapli.base.app.common.console.presentation.authz.MyUserMenu;
import eapli.base.Application;
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
import eapli.framework.presentation.console.menu.HorizontalMenuRenderer;
import eapli.framework.presentation.console.menu.MenuItemRenderer;
import eapli.framework.presentation.console.menu.MenuRenderer;
import eapli.framework.presentation.console.menu.VerticalMenuRenderer;

/**
 * TODO split this class in more specialized classes for each menu
 *
 * @author Paulo Gandra Sousa
 */
public class MainMenu extends AbstractUI {

    private static final String SEPARATOR_LABEL = "--------------";

    private static final int EXIT_OPTION = 0;

    // MAIN MENU
    private static final int MY_USER_OPTION = 1;
    private static final int MEETINGS_OPTION = 2;
    private static final int COURSES_OPTION = 3;
    private static final int EXAMS_OPTION = 4;
    private static final int SHARED_BOARD = 5;

    // COURSES

    private static final int LIST_COURSES_OPTION = 1;
    private static final int REQUEST_ENROLLMENT_OPTION = 2;
    private static final int VIEW_MY_ENROLLMENT_REQUESTS = 3;

    // EXAMS
    private static final int LIST_MY_FUTURE_EXAMS_OPTION = 1;
    private static final int TAKE_AUTOMATIC_EXAMS_OPTION = 2;
    private static final int TAKE_EXAMS_OPTION = 3;
    private static final int LIST_MY_GRADES_OPTION = 4;

    // Meetings

    private static final int SCHEDULE_MEETING_OPTION = 1;
    private static final int CANCEL_MEETING_OPTION = 2;
    private static final int LIST_PARTICIPANTS_OPTION = 3;
    private static final int ACCEPT_OR_REJECT_OPTION = 4;

    // Board
    private static final int CREATE_BOARD = 1;
    private static final int CREATE_POSTIT = 2;
    private static final int UPDATE_POSTIT = 3;
    private static final int UNDO_POSTIT = 4;
    private static final int SHARE_BOARD = 5;
    private static final int VIEW_BOARD = 6;
    private static final int ARCHIVE_BOARD = 7;
    private static final int RESTORE_BOARD = 8;
    private static final int BOARD_HISTORY = 9;
    private static final int BOARD_VERSION = 10;


    private final AuthorizationService authz = AuthzRegistry.authorizationService();

    private final Menu menu;
    private final MenuRenderer renderer;

    public MainMenu() {
        menu = buildMainMenu();
        renderer = getRenderer(menu);
    }

    private MenuRenderer getRenderer(final Menu menu) {
        final MenuRenderer theRenderer;
        if (Application.settings().isMenuLayoutHorizontal()) {
            theRenderer = new HorizontalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
        } else {
            theRenderer = new VerticalMenuRenderer(menu, MenuItemRenderer.DEFAULT);
        }
        return theRenderer;
    }

    @Override
    public boolean doShow() {
        return renderer.render();
    }

    @Override
    public boolean show() {
        drawFormTitle();
        return doShow();
    }

    @Override
    public String headline() {

        return authz.session().map(s -> "eCourse [ @" + s.authenticatedUser().identity() + " ]")
                .orElse("eCourse [ ==Anonymous== ]");
    }

    private Menu buildMainMenu() {
        final Menu mainMenu = new Menu();

        final Menu myUserMenu = new MyUserMenu(EcourseRoles.STUDENT);
        mainMenu.addSubMenu(MY_USER_OPTION, myUserMenu);

        if (!Application.settings().isMenuLayoutHorizontal()) {
            mainMenu.addItem(MenuItem.separator(SEPARATOR_LABEL));
        }

        if (authz.isAuthenticatedUserAuthorizedTo(EcourseRoles.STUDENT)) {
            final Menu course_menu = courseMenu();
            final Menu meeting_menu = meetingMenu();
            final Menu exam_menu = examMenu();
            final Menu board_menu = boardMenu();
            mainMenu.addSubMenu(MEETINGS_OPTION, meeting_menu);
            mainMenu.addSubMenu(COURSES_OPTION, course_menu);
            mainMenu.addSubMenu(EXAMS_OPTION, exam_menu);
            mainMenu.addSubMenu(SHARED_BOARD, board_menu);
        }

        if (!Application.settings().isMenuLayoutHorizontal()) {
            mainMenu.addItem(MenuItem.separator(SEPARATOR_LABEL));
        }

        mainMenu.addItem(EXIT_OPTION, "Exit", new ExitWithMessageAction("Bye, Bye"));

        return mainMenu;
    }

    private Menu courseMenu() {
        final Menu studentMenu = new Menu("Courses >");

        studentMenu.addItem(LIST_COURSES_OPTION, "List my Courses and Courses available to me ", new ListCoursesByUserAction());
        studentMenu.addItem(REQUEST_ENROLLMENT_OPTION, "Request Enrollment in a Course", new RequestEnrollmentAction());
        studentMenu.addItem(VIEW_MY_ENROLLMENT_REQUESTS, "View my Enrollment Requests", new ViewMyEnrollmentRequestsAction());
        studentMenu.addItem(EXIT_OPTION, "Return", Actions.SUCCESS);

        return studentMenu;
    }

    private Menu meetingMenu() {
        final Menu studentMenu2 = new Menu("Meetings >");
        studentMenu2.addItem(EXIT_OPTION, "Return", Actions.SUCCESS);
        studentMenu2.addItem(SCHEDULE_MEETING_OPTION, "Schedule Meeting", new ScheduleMeetingAction());
        studentMenu2.addItem(CANCEL_MEETING_OPTION, "Cancel Meeting", new CancelMeetingAction());
        studentMenu2.addItem(LIST_PARTICIPANTS_OPTION, "List Participants in Meeting", new ListParticipantsInMeetingAction());
        studentMenu2.addItem(ACCEPT_OR_REJECT_OPTION, "Accept or Reject Meeting Request", new AcceptOrRejectMeetingRequestAction());
        return studentMenu2;
    }

    private Menu examMenu() {
        final Menu studentMenu3 = new Menu("Exams >");

        studentMenu3.addItem(LIST_MY_FUTURE_EXAMS_OPTION, "List My Future Exams", new ListFutureExamsAction());
        studentMenu3.addItem(TAKE_AUTOMATIC_EXAMS_OPTION, "Take an Automatic Formative Exam", new TakeAnAutomaticFormativeExamAction());
        studentMenu3.addItem(TAKE_EXAMS_OPTION, "Take an Exam", new TakeAnExamAction());
        studentMenu3.addItem(LIST_MY_GRADES_OPTION, "List My Grades", new ListMyGradesAction());
        studentMenu3.addItem(EXIT_OPTION, "Return", Actions.SUCCESS);

        return studentMenu3;
    }

    private Menu boardMenu() {
        final Menu studentMenu3 = new Menu("Board >");
        studentMenu3.addItem(EXIT_OPTION, "Return", Actions.SUCCESS);
        studentMenu3.addItem(CREATE_BOARD, "Create Board", new CreateBoardAction());
        studentMenu3.addItem(CREATE_POSTIT, "Create PostIt", new CreatePostItAction());
        studentMenu3.addItem(UPDATE_POSTIT, "Update PostIt", new UpdatePostItAction());
        studentMenu3.addItem(UNDO_POSTIT, "Undo Last Change in PostIt", new UndoPostItLastChangeAction());
        studentMenu3.addItem(SHARE_BOARD,"Share a Board", new ShareABoardAction());
        studentMenu3.addItem(VIEW_BOARD,"View a Board", new ViewBoardAction());
        studentMenu3.addItem(ARCHIVE_BOARD, "Archive Board", new ArchiveBoardAction());
        studentMenu3.addItem(RESTORE_BOARD, "Restore Board", new RestoreBoardAction());
        studentMenu3.addItem(BOARD_HISTORY, "View Board History", new ViewBoardHistoryAction());
        studentMenu3.addItem(BOARD_VERSION, "View Board Version", new ViewBoardVersionAction());
        return studentMenu3;
    }
}
