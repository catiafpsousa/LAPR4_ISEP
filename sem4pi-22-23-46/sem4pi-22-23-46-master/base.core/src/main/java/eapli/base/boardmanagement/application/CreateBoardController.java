package eapli.base.boardmanagement.application;

import eapli.base.boardmanagement.domain.BoardBuilder;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.boardmanagement.repositories.BoardRepository;
import eapli.base.infrastructure.authz.application.AuthorizationService;
import eapli.base.infrastructure.authz.application.AuthzRegistry;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.base.infrastructure.persistence.PersistenceContext;
import eapli.framework.application.UseCaseController;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */

@UseCaseController
public class CreateBoardController {
    private final BoardRepository boardRepo=PersistenceContext.repositories().boards();
    private final UserRepository userRepo=PersistenceContext.repositories().users();
    private final AuthorizationService authz=AuthzRegistry.authorizationService();
    private SharedBoard board;

    public SharedBoard createSharedBoard (final String title, final int numberOfrows, final int numberOfColumns){
        final SystemUser owner = currentUser().orElseThrow(IllegalStateException::new);
        final var boardBuilder = new BoardBuilder();
        boardBuilder.withOwner(owner).withTitle(title).withRowNumber(numberOfrows).withColumnNumber(numberOfColumns);
        board= boardBuilder.build();
        return board;
    }

    @Transactional
    public SharedBoard saveSharedBoard(){
        return boardRepo.save(board);
    }

    /**
     * Return session current user
     * @return Shared Board owner
     */
    private Optional<SystemUser> currentUser(){
        return authz.session().flatMap(s->userRepo.ofIdentity(s.authenticatedUser().identity()));
    }

    public void updateColumnTitles(int column, String title){
        board.updateColumnTitle(title, column);
    }

    public void updateRowTitles(int row, String title){
        board.updateRowTitle(title, row);
    }

}
