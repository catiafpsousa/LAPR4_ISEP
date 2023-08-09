package application;

import eapli.base.boardmanagement.application.BoardManagementService;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.infrastructure.persistence.PersistenceContext;

import java.util.Optional;

public class ViewBoardController {
    private final BoardManagementService boardSvc = AuthzRegistry.boardService();
    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final UserRepository userRepo = PersistenceContext.repositories().users();

    public SharedBoard getSharedBoardById(Long id){
        return boardSvc.boardById(id).orElseThrow(IllegalStateException::new);
    }

    public Iterable <SharedBoard> listBoardByUser(){
        return boardSvc.allBoardsBySystemUserWithReadPermission(currentUser());
    }

    public Optional<SystemUser> currentUser() {
        return authz.session().flatMap(s -> userRepo.ofIdentity(s.authenticatedUser().identity()));
    }


}
