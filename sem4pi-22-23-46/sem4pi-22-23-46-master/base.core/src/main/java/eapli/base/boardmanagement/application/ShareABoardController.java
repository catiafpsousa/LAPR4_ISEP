package eapli.base.boardmanagement.application;

import eapli.base.boardmanagement.domain.BoardPermission;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.boardmanagement.repositories.BoardRepository;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.application.UserManagementService;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.framework.application.UseCaseController;

import java.util.Optional;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */

@UseCaseController
public class ShareABoardController {
    private final BoardRepository boardRepo= PersistenceContext.repositories().boards();
    private final UserManagementService userSvc =AuthzRegistry.userService();
    private final UserRepository userRepo = PersistenceContext.repositories().users();
    private final AuthorizationService authz= AuthzRegistry.authorizationService();

    public Iterable<SharedBoard> listBoards(){
        final SystemUser owner = currentUser().orElseThrow(IllegalStateException::new);
        return boardRepo.findBoardsOwnedByUser(owner);
    }

    public Iterable<SystemUser> listUsers(){
        return userSvc.activeUsers();
    }

    private Optional<SystemUser> currentUser(){
        return authz.session().flatMap(s->userRepo.ofIdentity(s.authenticatedUser().identity()));
    }

    public void shareABoard(final SharedBoard board, SystemUser user, BoardPermission permission){
        board.shareABoard(user, permission);
    }

    public SharedBoard save(final SharedBoard board){
        return boardRepo.save(board);
    }


}
