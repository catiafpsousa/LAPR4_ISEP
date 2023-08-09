package eapli.base.boardmanagement.repositories;

import eapli.base.boardmanagement.domain.Cell;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.repositories.DomainRepository;

import java.util.Calendar;

/**
 *
 * @author Joana Nogueira 1202924@isep.ipp.pt
 */
public interface BoardRepository extends DomainRepository<Long, SharedBoard> {

    Iterable<Cell> findCellsWithUserPostIts(SystemUser user, SharedBoard sharedBoard);

    void flush();


    Iterable<SharedBoard> findBoardsOwnedByUser(SystemUser systemUser);


    Iterable<SharedBoard> findBySystemUserWithWritePermission(SystemUser systemUser);

    Iterable<SharedBoard> findBySystemUserWithReadPermission(SystemUser user);

    Iterable<SharedBoard> findBoardsArchivedOwnedByUser(SystemUser authenticatedUser);


}