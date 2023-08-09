package application;

import eapli.base.boardmanagement.application.BoardManagementService;
import eapli.base.boardmanagement.domain.Cell;
import eapli.base.boardmanagement.domain.PostIt;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.framework.application.UseCaseController;

import java.util.Optional;

/**
 * Use case controller for undoing the last change on a post-it note.
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */
@UseCaseController
public class ServerUndoPostItController {
    private final UserRepository userRepo = PersistenceContext.repositories().users();
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final BoardManagementService boardSvc = AuthzRegistry.boardService();


    /**
     * Undo the last change made on a post-it note.
     * @param board The board where the post-it note is located.
     * @param postIt The post-it note whose change needs to be undone.
     * @return true if the change was undone successfully, false otherwise.
     */
    public boolean undoPostItLastChange(SharedBoard board, PostIt postIt) {
        System.out.println( "PostIT que vai ser Undo" + postIt);
        boolean updateBoard = boardSvc.undoPostItLastChange(board, postIt);
        board.printSharedBoard();
        return updateBoard;
    }

    /**
     * Get all boards where the current user has write permissions.
     * @return An iterable containing all boards where the current user has write permissions.
     */
    public Iterable<SharedBoard> sharedBoardsWithWritePermission() {
        return boardSvc.allBoardsBySystemUserWithWritePermission(currentUser());
    }


    /**
     * Get all cells of a board that contain post-its created by the current user.
     * @param sharedBoard The board to get cells from.
     * @return An iterable containing all cells of the board that contain post-its created by the current user.
     */
    public Iterable<Cell> cellsOfBoardAndUser(SharedBoard sharedBoard) {
        final SystemUser user = currentUser().orElseThrow(IllegalStateException::new);
        return sharedBoard.cellOfBoardAndUser(user);
    }

    /**
     * Get the current user from the session.
     * @return An optional containing the current user if present, empty optional otherwise.
     */
    public Optional<SystemUser> currentUser() {
        return authz.session().flatMap(s -> userRepo.ofIdentity(s.authenticatedUser().identity()));
    }

    /**
     * Get all boards.
     * @return An iterable containing all boards.
     */
    public Iterable<SharedBoard> allBoards() {
        return boardSvc.allBoards();
    }

    /**
     * Prints the representation of the given shared board as a table to the console.
     * @param board The shared board that is to be printed as a table.
     */
    public void printBoardAsTable (SharedBoard board){
        boardSvc.printBoardAsTable(board);
    }
}
