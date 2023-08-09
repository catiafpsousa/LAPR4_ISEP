package eapli.base.persistence.impl.jpa;

import eapli.base.Application;
import eapli.base.boardmanagement.domain.BoardStatus;
import eapli.base.boardmanagement.domain.Cell;
import eapli.base.boardmanagement.domain.SharedBoard;
import eapli.base.boardmanagement.repositories.BoardRepository;
import eapli.base.coursemanagement.domain.Course;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class JpaBoardRepository extends JpaAutoTxRepository <SharedBoard, Long, Long> implements BoardRepository {
    public JpaBoardRepository(TransactionalContext tx) {
        super(tx, "id");
    }

    public JpaBoardRepository(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(),
                "id");
    }

    public Iterable<Cell> findCellsWithUserPostIts (final SystemUser user, final SharedBoard sharedBoard){
        String jpql = "SELECT DISTINCT cell " +
                "FROM SharedBoard sharedBoard " +
                "JOIN sharedBoard.cells cell " +
                "JOIN cell.postIts postIt " +
                "WHERE sharedBoard = :sharedBoard " +
                "AND postIt.owner = :user";

        Set<Cell> cellsWithUserPostIts = entityManager().createQuery(jpql, Cell.class)
                .setParameter("sharedBoard", sharedBoard)
                .setParameter("user", user)
                .getResultList()
                .stream()
                .collect(Collectors.toSet());

        return cellsWithUserPostIts;
    }

    public void flush (){
        entityManager().flush();
    }

    @Override
    public Iterable<SharedBoard> findBoardsOwnedByUser(SystemUser systemUser) {
        TypedQuery<SharedBoard> query = entityManager().createQuery("SELECT b FROM SharedBoard b WHERE b.owner = :systemUser AND b.boardStatus = :status", SharedBoard.class);
        query.setParameter("systemUser", systemUser);
        query.setParameter("status", BoardStatus.ACTIVE);
        return query.getResultList();
    }

    @Override
    public Iterable<SharedBoard> findBySystemUserWithWritePermission(SystemUser systemUser) {
        TypedQuery<SharedBoard> query = entityManager().createQuery("SELECT DISTINCT sb FROM SharedBoard sb " +
                "LEFT JOIN sb.sharedList sp " +
                "WHERE (sb.owner = :systemUser OR (sp.user = :systemUser AND sp.permission = 'WRITE')) AND sb.boardStatus = :active", SharedBoard.class);
        query.setParameter("systemUser", systemUser);
        query.setParameter("active", BoardStatus.ACTIVE);
        return query.getResultList();
    }

    @Override
    public Iterable<SharedBoard> findBySystemUserWithReadPermission(SystemUser systemUser) {
        TypedQuery<SharedBoard> query = entityManager().createQuery("SELECT DISTINCT sb FROM SharedBoard sb " +
                "LEFT JOIN sb.sharedList sp " +
                "WHERE (sb.owner = :systemUser OR sp.user = :systemUser) AND sb.boardStatus = :active", SharedBoard.class);
        query.setParameter("systemUser", systemUser);
        query.setParameter("active", BoardStatus.ACTIVE);
        return query.getResultList();
    }

    @Override
    public Iterable<SharedBoard> findBoardsArchivedOwnedByUser(SystemUser authenticatedUser) {
        TypedQuery<SharedBoard> query = entityManager().createQuery("SELECT b FROM SharedBoard b WHERE b.owner = :systemUser AND b.boardStatus = :status", SharedBoard.class);
        query.setParameter("systemUser", authenticatedUser);
        query.setParameter("status", BoardStatus.ARCHIVED);
        return query.getResultList();
    }

}
