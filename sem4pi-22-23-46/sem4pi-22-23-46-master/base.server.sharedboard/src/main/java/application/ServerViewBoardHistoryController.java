package application;

import eapli.base.boardmanagement.application.BoardManagementService;
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
public class ServerViewBoardHistoryController {
    private final BoardManagementService boardSvc = AuthzRegistry.boardService();


    /**
     * Get all boards where the current user has read permissions.
     * @return An iterable containing all boards where the current user has read permissions.
     */
    public Iterable<SharedBoard> sharedBoardsWithReadPermission(SystemUser user) {
        return boardSvc.allBoardsBySystemUserWithReadPermission(Optional.ofNullable(user));
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
