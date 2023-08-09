package eapli.base.boardmanagement.application;

import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.boardmanagement.repositories.BoardRepository;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.base.usermanagement.domain.EcourseRoles;
import eapli.framework.application.UseCaseController;

/**
 * @author Cátia Sousa 1210849@isep.pt
 */

@UseCaseController
public class ArchiveBoardController {

    private final BoardManagementService boardSvc = AuthzRegistry.boardService();

    private final BoardRepository boardRepo = PersistenceContext.repositories().boards();

    private final AuthorizationService authz = AuthzRegistry.authorizationService();



    public Iterable<SharedBoard> allBoardsOwnedByUser() {
        authz.ensureAuthenticatedUserHasAnyOf(EcourseRoles.POWER_USER, EcourseRoles.ADMIN, EcourseRoles.STUDENT, EcourseRoles.TEACHER);
        return boardSvc.allBoardsOwnedByUser(authz.session().get().authenticatedUser());
    }


    public void archiveBoard(SharedBoard boardSelected) {
        boardSvc.archiveBoard(boardSelected);
    }
}
