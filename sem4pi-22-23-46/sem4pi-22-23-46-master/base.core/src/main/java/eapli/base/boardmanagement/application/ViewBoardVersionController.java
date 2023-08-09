package eapli.base.boardmanagement.application;

import eapli.base.boardmanagement.domain.BoardModification;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.framework.application.UseCaseController;

import java.util.Calendar;
import java.util.Optional;
import java.util.Set;

/**
 * Use case controller for viewing a specific version of a board.
 * Provides methods to retrieve board versions, board history, and print a board as a table.
 * Also includes methods to retrieve shared boards with read permission and all boards.
 * Used in the user interface for displaying board versions and history.
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */

@UseCaseController
public class ViewBoardVersionController {
    private final UserRepository userRepo = PersistenceContext.repositories().users();
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final BoardManagementService boardSvc = AuthzRegistry.boardService();

    /**
     * Get the current user from the session.
     * @return An optional containing the current user if present, empty optional otherwise.
     */
    private Optional<SystemUser> currentUser() {
        return authz.session().flatMap(s -> userRepo.ofIdentity(s.authenticatedUser().identity()));
    }

    /**
     * Get all boards where the current user has read permissions.
     * @return An iterable containing all boards where the current user has read permissions.
     */
    public Iterable<SharedBoard> sharedBoardsWithReadPermission() {
        return boardSvc.allBoardsBySystemUserWithReadPermission(currentUser());
    }

    /**
     * Get all boards.
     * @return An iterable containing all boards.
     */
    public Iterable<SharedBoard> allBoards() {
        return boardSvc.allBoards();
    }

    /**
     * Get a specific version of a shared board based on the given timestamp.
     * @param sharedBoard The shared board to retrieve the version from.
     * @param timestamp The timestamp indicating the version of the board to retrieve.
     * @return The shared board object representing the specified version.
     */
    public SharedBoard boardVersion (SharedBoard sharedBoard, Calendar timestamp){
        return boardSvc.boardVersion (sharedBoard, timestamp);
    }

    /**
     * Get the history of updates of a board.
     * @param board The shared board to retrieve the history of updates from.
     * @return A set containing the board modifications representing the history of updates of the board.
     */
    public Set<BoardModification> boardHistory (SharedBoard board){
        return boardSvc.boardModifications(board);
    }

    /**
     * Prints the representation of the given shared board as a table to the console.
     * @param board The shared board that is to be printed as a table.
     */
    public void printBoardAsTable (SharedBoard board){
        boardSvc.printBoardAsTable(board);
    }

}
