package eapli.base.boardmanagement.application;

import eapli.base.boardmanagement.domain.*;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.framework.application.UseCaseController;


import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Use case controller for creating a PostIt on a board.
 * @author Pedro Garrido 1182090@isep.ipp.pt
 */
@UseCaseController
public class CreatePostItController {
    private final UserRepository userRepo = PersistenceContext.repositories().users();
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final BoardManagementService boardSvc = AuthzRegistry.boardService();

    /**
     * Method to create a PostIt on a specified cell in a specified board.
     * @param board The board where the PostIt is to be created.
     * @param cell The cell on the board where the PostIt is to be created.
     * @param content The content of the new PostIt.
     * @return True if the PostIt is successfully created, false otherwise.
     */
    @Transactional
    public boolean createPostIt(SharedBoard board, Cell cell, Content content) {
        final SystemUser owner = currentUser().orElseThrow(IllegalStateException::new);
        if (cell.addPostIt(owner, content)) {
            boardSvc.saveSharedBoard(board);
            PostIt createdPostIt = cell.latestActivePostIt();
            boardSvc.registerPrimaryBoardModification(board, createdPostIt, ModificationType.CREATION, cell);
            boardSvc.saveSharedBoard(board);
            return true;
        } else return false;
    }

    /**
     * Helper method to get the current user.
     * @return The current system user.
     */
    private Optional<SystemUser> currentUser() {
        return authz.session().flatMap(s -> userRepo.ofIdentity(s.authenticatedUser().identity()));
    }

    /**
     * Returns all boards where the current user has write permission.
     * @return An iterable of SharedBoard.
     */
    public Iterable<SharedBoard> sharedBoardsWithWritePermission() {
        return boardSvc.allBoardsBySystemUserWithWritePermission(currentUser());
    }

    /**
     * Returns all the boards in the system.
     * @return An iterable of SharedBoard.
     */
    public Iterable<SharedBoard> allBoards() {
        return boardSvc.allBoards();
    }

    /**
     * Prints a representation of the board as a table.
     * @param board The board to be printed.
     */
    public void printBoardAsTable(SharedBoard board) {
        boardSvc.printBoardAsTable(board);
    }

    public SharedBoard lockBoard(SharedBoard board){
        return boardSvc.lockBoard(board);
    }

    public SharedBoard unLockBoard(SharedBoard board){
        return boardSvc.unLockBoard(board);
    }
}
