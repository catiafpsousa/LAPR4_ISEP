package eapli.base.boardmanagement.application;

import eapli.base.boardmanagement.domain.*;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.framework.application.UseCaseController;

import java.util.Optional;
import java.util.Set;


/**
 * Use case controller for updating a post-it note.
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */

@UseCaseController
public class UpdatePostItController {
    private final UserRepository userRepo = PersistenceContext.repositories().users();
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final BoardManagementService boardSvc = AuthzRegistry.boardService();
    private SharedBoard board;

    /**
     * Update the content of a post-it note.
     * @param board The board where the post-it note is located.
     * @param cell The cell containing the post-it note.
     * @param postIt The post-it note whose content needs to be updated.
     * @param newContent The new content of the post-it note.
     * @return true if the content was updated successfully, false otherwise.
     */
    public boolean updatePostItContent(SharedBoard board, Cell cell, PostIt postIt, Content newContent) {
        boolean updateBoard = boardSvc.updatePostItContent(board, cell, postIt, newContent);
        return updateBoard;
    }

    /**
     * Move a post-it note from one cell to another.
     * @param board The board where the post-it note is located.
     * @param oldCell The cell currently containing the post-it note.
     * @param newCell The cell to which the post-it note should be moved.
     * @param postIt The post-it note that needs to be moved.
     * @return true if the post-it was moved successfully, false otherwise.
     */
    public boolean movePostIt(SharedBoard board, Cell oldCell, PostIt postIt, Cell newCell) {
        boolean updateBoard = boardSvc.movePostIt(board, oldCell, postIt, newCell);
        return updateBoard;
    }

    /**
     * Delete a post-it note.
     * @param board The board where the post-it note is located.
     * @param cell The cell containing the post-it note.
     * @param postIt The post-it note that needs to be deleted.
     * @return true if the post-it was deleted successfully, false otherwise.
     */
    public boolean deletePostIt(SharedBoard board, Cell cell, PostIt postIt) {
        boolean updateBoard = boardSvc.deletePostIt(board, cell, postIt);
        return updateBoard;
    }

    /**
     * Get the current user from the session.
     * @return An optional containing the current user if present, empty optional otherwise.
     */
    private Optional<SystemUser> currentUser() {
        return authz.session().flatMap(s -> userRepo.ofIdentity(s.authenticatedUser().identity()));
    }

    /**
     * Get all boards where the current user has write permissions.
     * @return An iterable containing all boards where the current user has write permissions.
     */
    public Iterable<SharedBoard> sharedBoardsWithWritePermission() {
        final SystemUser user = currentUser().orElseThrow(IllegalStateException::new);
        return boardSvc.allBoardsBySystemUserWithWritePermission(Optional.ofNullable(user));
    }

    /**
     * Get all cells of a board that contain post-its created by the current user.
     * @param sharedBoard The board to get cells from.
     * @return An iterable containing all cells of the board that contain post-its created by the current user.
     */
    public Iterable<Cell> cellsOfBoardAndUser(SharedBoard sharedBoard) {
        final SystemUser user = currentUser().orElseThrow(IllegalStateException::new);
        return boardSvc.findCellsWithUserPostIts(user, sharedBoard);
    }

    /**
     * Get all cells of a board that are currently not occupied by any post-it notes on delete state false.
     * @param sharedBoard The board to get cells from.
     * @return A set containing all cells of the board that are currently not occupied by any post-it noteson delete state false.
     */
    public Set<Cell> availableCells(SharedBoard sharedBoard) {
        return sharedBoard.availableCells();
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
    public void printBoardAsTable(SharedBoard board) {
        boardSvc.printBoardAsTable(board);
    }

}
