package application;

import eapli.base.boardmanagement.domain.BoardPermission;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.boardmanagement.repositories.BoardRepository;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.application.UserManagementService;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.infrastructure.persistence.PersistenceContext;

import java.util.Optional;

public class ShareABoardController {
    private final BoardRepository boardRepo= PersistenceContext.repositories().boards();
    private final UserManagementService userSvc = AuthzRegistry.userService();
    private SystemUser owner;

    public Iterable<SharedBoard> listBoards(SystemUser user){
        this.owner = user;
        return boardRepo.findBoardsOwnedByUser(owner);
    }

    public Iterable<SystemUser> listUsers(){
        return userSvc.activeUsers();
    }

    public void shareABoard(final SharedBoard board, SystemUser user, BoardPermission permission){
        board.shareABoard(user, permission);
    }

    public SystemUser getUser(){
        SystemUser sessionUser = this.owner;
        return sessionUser;
    }

    public SharedBoard save(final SharedBoard board){
        return boardRepo.save(board);
    }
}
