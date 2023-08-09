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
package eapli.base.app.teacher.console.presentation;

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
    private static final int COURSES = 2;
    private static final int EXAMS = 3;
    private static final int QUESTIONS = 4;
    private static final int MEETINGS = 5;
    private static final int CLASSES = 6;
    private static final int BOARD = 7;


    // Exams
    private static final int CREATE_EXAM = 1;
    private static final int CREATE_AUTOMATIC_FORMATIVE_EXAM = 2;
    private static final int LIST_ALL_EXAMS_OF_COURSE = 3;
    private static final int UPDATE_AUTOMATIC_FORMATIVE_EXAM = 4;
    private static final int UPDATE_EXAM = 5;
    private static final int LIST_ALL_GRADES_OF_COURSE = 6;


    // Meetings
    private static final int SCHEDULE_MEETING_OPTION = 1;
    private static final int CANCEL_MEETING_OPTION = 4;
    private static final int LIST_PARTICIPANTS_OPTION = 2;
    private static  final int ACCEPT_OR_REJECT_OPTION=3;

    //Class
    private static final int SCHEDULE_EXTRA_OPTION = 1;
    private static final int SCHEDULE_RECURRING_OPTION = 2;
    private static final int UPDATE_CLASS_SCHEDULE_OPTION = 3;
    //Courses
    private static final int LIST_COURSES_OPTION = 1;

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


    //Question

    private static final int ADD_QUESTION=1;




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

        final Menu myUserMenu = new MyUserMenu(EcourseRoles.TEACHER);
        mainMenu.addSubMenu(MY_USER_OPTION, myUserMenu);


        if (authz.isAuthenticatedUserAuthorizedTo(EcourseRoles.TEACHER)) {
            final Menu course_menu = buildCourseMenu();
            mainMenu.addSubMenu(COURSES, course_menu);
            final Menu exam_menu = buildExamMenu();
            mainMenu.addSubMenu(EXAMS, exam_menu);
            final Menu question_menu = buildQuestionMenu();
            mainMenu.addSubMenu(QUESTIONS, question_menu);
            final Menu meeting_menu = buildMeetingMenu();
            mainMenu.addSubMenu(MEETINGS, meeting_menu);
            final Menu class_menu = buildClassMenu();
            mainMenu.addSubMenu(CLASSES, class_menu);
            final Menu board_menu = buildBoardMenu();
            mainMenu.addSubMenu(BOARD, board_menu);
        }

        if (!Application.settings().isMenuLayoutHorizontal()) {
            mainMenu.addItem(MenuItem.separator(SEPARATOR_LABEL));
        }

        mainMenu.addItem(EXIT_OPTION, "Exit", new ExitWithMessageAction("Bye, Bye"));

        return mainMenu;
    }

    private Menu buildExamMenu() {
        final Menu teacherMenu = new Menu("Exams");
        teacherMenu.addItem(EXIT_OPTION, "Return", Actions.SUCCESS);
        teacherMenu.addItem(CREATE_EXAM, "Create an exam", new CreateExamAction());
        teacherMenu.addItem(CREATE_AUTOMATIC_FORMATIVE_EXAM, "Create automatic formative exam", new CreateAutomaticFormativeExamAction());
        teacherMenu.addItem(LIST_ALL_EXAMS_OF_COURSE, "List all exams of a course", new ListAllExamsOfCourseAction());
        teacherMenu.addItem(UPDATE_AUTOMATIC_FORMATIVE_EXAM, "Update Automatic Formative Exam", new UpdateAutomaticFormativeExamAction());
        teacherMenu.addItem(UPDATE_EXAM, "Update Exam", new UpdateExamAction());
        teacherMenu.addItem(LIST_ALL_GRADES_OF_COURSE, "List all grades of a course", new ListGradesOfCourseAction());

        return teacherMenu;
    }

    private Menu buildCourseMenu() {
        final Menu teacherMenu = new Menu("Courses");
        teacherMenu.addItem(EXIT_OPTION, "Return", Actions.SUCCESS);
        teacherMenu.addItem(LIST_COURSES_OPTION, "List My Courses ", new ListCoursesByUserAction());
        return teacherMenu;
    }

    private Menu buildMeetingMenu() {
        final Menu teacherMenu = new Menu("Meeting");
        teacherMenu.addItem(EXIT_OPTION, "Return", Actions.SUCCESS);
        teacherMenu.addItem(SCHEDULE_MEETING_OPTION, "Schedule Meeting", new ScheduleMeetingAction());
        teacherMenu.addItem(LIST_PARTICIPANTS_OPTION, "List Participants in Meeting", new ListParticipantsInMeetingAction());
        teacherMenu.addItem(ACCEPT_OR_REJECT_OPTION, "Accept or Reject Meeting Request", new AcceptOrRejectMeetingRequestAction());
        teacherMenu.addItem(CANCEL_MEETING_OPTION, "Cancel Meeting", new CancelMeetingAction());

        return teacherMenu;
    }

    private Menu buildBoardMenu() {
        final Menu teacherMenu = new Menu("Board");
        teacherMenu.addItem(EXIT_OPTION, "Return", Actions.SUCCESS);
        teacherMenu.addItem(CREATE_BOARD, "Create Board", new CreateBoardAction());
        teacherMenu.addItem(CREATE_POSTIT, "Create PostIt", new CreatePostItAction());
        teacherMenu.addItem(UPDATE_POSTIT, "Update PostIt", new UpdatePostItAction());
        teacherMenu.addItem(UNDO_POSTIT, "Undo Last Change in PostIt", new UndoPostItLastChangeAction());
        teacherMenu.addItem(SHARE_BOARD,"Share a Board", new ShareABoardAction());
        teacherMenu.addItem(VIEW_BOARD, "View Board", new ViewBoardAction());
        teacherMenu.addItem(ARCHIVE_BOARD, "Archive Board", new ArchiveBoardAction());
        teacherMenu.addItem(RESTORE_BOARD, "Restore Board", new RestoreBoardAction());
        teacherMenu.addItem(BOARD_HISTORY, "View Board History", new ViewBoardHistoryAction());
        teacherMenu.addItem(BOARD_VERSION, "View Board Version", new ViewBoardVersionAction());

        return teacherMenu;
    }

    private Menu buildClassMenu() {
        final Menu teacherMenu = new Menu("Class");
        teacherMenu.addItem(EXIT_OPTION, "Return", Actions.SUCCESS);
        teacherMenu.addItem(SCHEDULE_EXTRA_OPTION, "Schedule Extraordinary Class", new ScheduleExtraClassAction());
        teacherMenu.addItem(SCHEDULE_RECURRING_OPTION, "Schedule Recurring Class", new ScheduleRecurringClassAction());
        teacherMenu.addItem(UPDATE_CLASS_SCHEDULE_OPTION, "Update Recurring Class Schedule", new UpdateClassScheduleAction());

        return teacherMenu;
    }

    private Menu buildQuestionMenu(){
        final Menu teacherMenu = new Menu("Questions");
        teacherMenu.addItem(EXIT_OPTION, "Return", Actions.SUCCESS);
        teacherMenu.addItem(ADD_QUESTION, "Add question", new AddExamQuestionAction());
        return teacherMenu;
    }
}
