package eapli.base.boardmanagement.application;

import eapli.base.boardmanagement.domain.BoardModification;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.framework.application.UseCaseController;

import java.util.Optional;
import java.util.Set;

/**
 * Use case controller for viewing the history of updates in a board.
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */

@UseCaseController
public class ViewBoardHistoryController {
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
     * Get the history of updates in a board.
     * @param board The shared board to retrieve the history of updates from.
     * @return A set containing the board modifications representing the history of updates of the board.
     */
    public Set<BoardModification> boardHistory (SharedBoard board){
        return boardSvc.boardModifications(board);
    }
}
