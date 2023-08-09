package application;

import eapli.base.boardmanagement.application.BoardManagementService;
import eapli.base.boardmanagement.domain.*;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.application.UseCaseController;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Use case controller for creating a PostIt on a board.
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */
@UseCaseController
public class ServerCreatePostItController {

    private final BoardManagementService boardSvc = AuthzRegistry.boardService();
    private SystemUser owner;
    private static boolean running;


    /**
     * Method to create a PostIt on a specified cell in a specified board.
     * @param board The board where the PostIt is to be created.
     * @param cell The cell on the board where the PostIt is to be created.
     * @param content The content of the new PostIt.
     * @return True if the PostIt is successfully created, false otherwise.
     */
    @Transactional
    public boolean createPostIt(SharedBoard board, Cell cell, Content content, SystemUser user) {
        this.owner = user;
        if (cell.addPostIt(owner, content)) {
            PostIt createdPostIt = cell.latestActivePostIt();
            boardSvc.registerPrimaryBoardModification(board, createdPostIt, ModificationType.CREATION, cell);
            boardSvc.saveSharedBoard(board);
            board.printSharedBoard();
            return true;
        } else return false;
    }

    /**
     * Helper method to get the current user.
     * @return The current system user.
     */
    private Optional<SystemUser> currentUser() {
        return Optional.ofNullable(this.owner);
    }

    /**
     * Returns all boards where the current user has write permission.
     * @return An iterable of SharedBoard.
     */
    public Iterable<SharedBoard> sharedBoardsWithWritePermission(SystemUser user) {
        return boardSvc.allBoardsBySystemUserWithWritePermission(Optional.ofNullable(user));
    }

    public void startAcess(SharedBoard board){
        board.startContinuousLoop();
    }

//    public void stopAcess(SharedBoard board){
//        board.stopContinuousLoop();
//    }

    /**
     * Returns all the boards in the system.
     * @return An iterable of SharedBoard.
     */
    public Iterable<SharedBoard> allBoards() {
        return boardSvc.allBoards();
    }

    public SharedBoard lockBoard(SharedBoard board){
        return boardSvc.lockBoard(board);
    }

    public SharedBoard unLockBoard(SharedBoard board){
        return boardSvc.unLockBoard(board);
    }

}
