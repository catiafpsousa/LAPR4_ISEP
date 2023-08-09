package eapli.base.app.sharedboard.console.presentation;

import eapli.base.Application;
import eapli.base.app.sharedboard.console.sharedboard.client.TcpClient;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.actions.menu.Menu;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.menu.HorizontalMenuRenderer;
import eapli.framework.presentation.console.menu.MenuItemRenderer;
import eapli.framework.presentation.console.menu.MenuRenderer;
import eapli.framework.presentation.console.menu.VerticalMenuRenderer;

import java.io.IOException;

public class MainMenu extends AbstractUI {
    // Board
    private static final int EXIT_OPTION = 0;
    private static final int CREATE_BOARD = 1;
    private static final int CREATE_POSTIT = 2;
    private static final int UPDATE_POSTIT = 3;
    private static final int UNDO_POSTIT = 4;
    private static final int SHARE_BOARD = 5;
    private static final int VIEW_BOARD_HISTORY = 6;
    private static final int VIEW_BOARD_VERSION = 7;
    private static final int ARCHIVE_BOARD = 8;
    private static final int RESTORE_BOARD = 9;

    private static final int VIEW_BOARD = 10;

    private final Menu menu;
    private final MenuRenderer renderer;
    private TcpClient client;
    private SystemUser user;

    public MainMenu(TcpClient client) throws IOException, ClassNotFoundException {
        this.client = client;
        menu = buildMainMenu();
        renderer = getRenderer(menu);
        this.user = this.client.sessionUser();
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

    private Menu buildMainMenu() {
        final Menu mainMenu = new Menu();
        mainMenu.addItem(EXIT_OPTION, "Exit", new ReturnAction(client));
        mainMenu.addItem(CREATE_BOARD, "Create Board", new CreateBoardAction(client));
        mainMenu.addItem(CREATE_POSTIT, "Create PostIt", new CreatePostItAction(client));
        mainMenu.addItem(UPDATE_POSTIT, "Update PostIt", new UpdatePostItAction(client));
        mainMenu.addItem(UNDO_POSTIT, "Undo Last Change in PostIt", new UndoPostItLastChangeAction(client));
        mainMenu.addItem(SHARE_BOARD, "Share a board", new ShareABoardAction(client));
        mainMenu.addItem(VIEW_BOARD_HISTORY, "View Board History of Modifications", new ViewBoardHistoryAction(client));
        mainMenu.addItem(VIEW_BOARD_VERSION, "View Board Past Version", new ViewBoardVersionAction(client));
        mainMenu.addItem(ARCHIVE_BOARD, "Archive a board", new ArchiveBoardAction(client));
        mainMenu.addItem(RESTORE_BOARD, "Restore a board", new RestoreBoardAction(client));
        mainMenu.addItem(VIEW_BOARD, "View a board", new ViewBoardAction(client));
        return mainMenu;
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
        return "SHARED BOARD APPLICATION";
    }
}
