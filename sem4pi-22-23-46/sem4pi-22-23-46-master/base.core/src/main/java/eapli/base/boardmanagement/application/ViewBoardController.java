package eapli.base.boardmanagement.application;

import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.framework.application.UseCaseController;

import java.util.Optional;

/**
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */

/**
 * Use case controller for viewing a board.
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */
@UseCaseController
public class ViewBoardController {
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
     * Prints the representation of the given shared board as a table to the console.
     * @param board The shared board that is to be printed as a table.
     */
    public void printBoardAsTable (SharedBoard board){
        boardSvc.printBoardAsTable(board);
    }
}
