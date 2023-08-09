package eapli.base.app.common.console.presentation.sharedboard;
import eapli.base.boardmanagement.application.ShareABoardController;
import eapli.base.boardmanagement.domain.BoardPermission;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.util.UiUtils;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import java.util.ArrayList;
import java.util.List;

public class ShareABoardUI extends AbstractUI {

    private static final int READ =1;
    private static final int WRITE =2;
    private final ShareABoardController controller = new ShareABoardController();

    @Override
    protected boolean doShow() {
        try {
            Iterable<SharedBoard> boards =controller.listBoards();
            if(!boards.iterator().hasNext()){
                System.out.println("To share a board you need to own one. Therefore, this functionality is not available.");
            } else {
                final SharedBoard board = (SharedBoard) UiUtils.showAndSelectOne((List) boards, "Select board that you to share with other users");
                final Iterable<SystemUser> users = this.controller.listUsers();
                System.out.println(users.iterator().hasNext());
                UiUtils.showList((List) users, "Select users to share this board (SEPARATED BY SPACES):");
                final String selection = Console.readLine("Enter selection");
                List<SystemUser> boardUsers = selectSharedList(selection, (List) users);

                for(SystemUser user: boardUsers){
                    System.out.println("Does user "+ user.shortName() +" with e-mail "+user.identity()+ " have READ or WRITE permissions?");
                    boolean validOption = false;
                    while (!validOption) {
                        final int choice = UiUtils.readIntegerFromConsole("Enter 1 to READ or 2 to WRITE board access");
                        if (choice == READ) {
                            controller.shareABoard(board, user, BoardPermission.READ);
                            validOption = true;
                        } else if (choice == WRITE) {
                            controller.shareABoard(board, user, BoardPermission.WRITE);
                            validOption = true;
                        }
                    }
                }

                boolean save = UiUtils.confirm("Do you want to save this shared list? (S/N)");
                if(save){
                    controller.save(board);
                    System.out.println("Shared list added successfully");
                }
            }
        }catch (Exception ex){
            System.out.println(ex.getMessage());
            System.out.println("Unfortunatelly there was an unexpected error in the application. Please try again and if the problem persists, contact your system admnistrator.");
        }
        return false;
    }

    public List<SystemUser> selectSharedList(String selection, List<SystemUser> allUsers) {
        String[] selectionFromPrompt = selection.split(" ");

        int[] numbers = new int[selectionFromPrompt.length];
        for (int i = 0; i < selectionFromPrompt.length; i++) {
            numbers[i] = Integer.parseInt(selectionFromPrompt[i]);
        }
        List<SystemUser> selectedUsers= new ArrayList<>();
        for (int index : numbers
        ) {
            selectedUsers.add(allUsers.get(index - 1));
        }
        return selectedUsers;
    }


    @Override
    public String headline() {
        return "Share a Board";
    }
}
