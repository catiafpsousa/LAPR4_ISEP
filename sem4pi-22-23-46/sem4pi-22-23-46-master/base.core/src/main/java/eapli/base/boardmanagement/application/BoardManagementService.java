package eapli.base.boardmanagement.application;

import eapli.base.boardmanagement.domain.*;
import eapli.base.boardmanagement.repositories.BoardRepository;
import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.infrastructure.authz.domain.repositories.UserRepository;
import eapli.framework.time.util.CurrentTimeCalendars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Optional;
import java.util.Set;

/**
 * This service handles board management related operations.
 */
@Component
public class BoardManagementService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    /**
     * Constructor for dependency injection.
     * @param userRepository User repository to be used by this service.
     * @param boardRepository Board repository to be used by this service.
     */
    @Autowired
    public BoardManagementService(UserRepository userRepository, BoardRepository boardRepository) {
        this.userRepository = userRepository;
        this.boardRepository = boardRepository;
    }

    /**
     * Persists the given shared board into the database.
     *
     * @param board The shared board to be persisted.
     * @return The persisted shared board.
     */
    @Transactional
    public SharedBoard saveSharedBoard(SharedBoard board) {
        return this.boardRepository.save(board);
    }

    /**
     * Retrieves all boards from the database, regardless of their status.
     *
     * @return An Iterable of all the shared boards.
     */
    public Iterable<SharedBoard> allBoards() {
        return boardRepository.findAll();
    }

    /**
     * Retrieves all boards from the database where the given user has write permission.
     *
     * @param systemUser The system user for whom to retrieve the boards.
     * @return An Iterable of the shared boards where the given user has write permission.
     */
    public Iterable<SharedBoard> allBoardsBySystemUserWithWritePermission(final Optional<SystemUser> systemUser) {
        return boardRepository.findBySystemUserWithWritePermission(systemUser.get());
    }

    /**
     * Retrieves all boards from the database where the given user has read permission.
     *
     * @param systemUser The system user for whom to retrieve the boards.
     * @return An Iterable of the shared boards where the given user has read permission.
     */
    public Iterable<SharedBoard> allBoardsBySystemUserWithReadPermission(Optional<SystemUser> systemUser) {
        return boardRepository.findBySystemUserWithReadPermission(systemUser.get());
    }

    /**
     * Retrieves all cells containing Post-Its from the given user on the specified shared board.
     *
     * @param user The system user whose Post-Its to find.
     * @param sharedBoard The shared board to look for Post-Its.
     * @return An Iterable of cells with the user's Post-Its on the given shared board.
     */
    public Iterable<Cell> findCellsWithUserPostIts(final SystemUser user, final SharedBoard sharedBoard) {
        return boardRepository.findCellsWithUserPostIts(user, sharedBoard);
    }

    /**
     * Updates the content of a PostIt on a shared board. The old PostIt is deleted and a new one is created.
     *
     * @param board The shared board containing the PostIt.
     * @param cell The cell containing the PostIt.
     * @param postIt The PostIt to be updated.
     * @param newContent The new content for the PostIt.
     * @return A boolean indicating the success of the operation.
     */
    @Transactional
    public boolean updatePostItContent(SharedBoard board, Cell cell, PostIt postIt, Content newContent) {
        deleteStatePostIt(board, postIt);
        if (cell.updatePostIt(postIt, newContent)) {
            registerContentBoardModification(board, postIt, cell, newContent);
            boardRepository.save(board);
            return true;
        } else return false;
    }

    /**
     * Moves a PostIt from one cell to another on a shared board. The old PostIt is deleted and a new one is created in the new cell.
     *
     * @param board The shared board containing the PostIt.
     * @param oldCell The cell currently containing the PostIt.
     * @param newCell The cell where the PostIt is to be moved.
     * @param postIt The PostIt to be moved.
     * @return A boolean indicating the success of the operation.
     */
    @Transactional
    public boolean movePostIt(SharedBoard board, Cell oldCell, PostIt postIt, Cell newCell) {
        //set to deletedState the last postIt of a Cell
        deleteStatePostIt(board, postIt);
        boolean success = newCell.addMovedPostIt(postIt);

        if (success) {
            registerPlacementBoardModification(board, postIt, oldCell, newCell);

            boardRepository.save(board);
            return true;
        } else return false;
    }

    /**
     * Deletes a PostIt from a cell on a shared board.
     *
     * @param board The shared board containing the PostIt.
     * @param cell The cell containing the PostIt.
     * @param postIt The PostIt to be deleted.
     * @return A boolean indicating the success of the operation.
     */
    @Transactional
    public boolean deletePostIt(SharedBoard board, Cell cell, PostIt postIt) {
        deleteStatePostIt(board, postIt); // changes postIt Delete State to True
        if (cell.updatePostIt(postIt, postIt.content())) {  // creates a new one with same attributes but new timestamp
            cell.latestActivePostIt().deleteStatePostIt();         // changes it state again to deleted to the new state modification instant
            registerPrimaryBoardModification(board, postIt, ModificationType.DELETE, cell);

            boardRepository.save(board);
            return true;
        } else return false;
    }

    /**
     * Changes the state of a PostIt on a shared board to deleted.
     *
     * @param board The shared board containing the PostIt.
     * @param postIt The PostIt to be marked as deleted.
     * @return A boolean indicating the success of the operation.
     */
    @Transactional
    public boolean deleteStatePostIt(SharedBoard board, PostIt postIt) {
        postIt.deleteStatePostIt();
        boardRepository.save(board);
        return true;
    }

    /**
     * Retrieves all the shared boards owned by a specific user.
     *
     * @param systemUser The user whose owned shared boards are to be retrieved.
     * @return An iterable collection of all shared boards owned by the provided user.
     */
    public Iterable<SharedBoard> allBoardsOwnedByUser(final SystemUser systemUser) {
        return boardRepository.findBoardsOwnedByUser(systemUser);
    }

    /**
     * Archives the specified shared board.
     *
     * @param boardSelected The shared board to be archived.
     * @return The archived shared board.
     */
    @Transactional
    public SharedBoard archiveBoard(SharedBoard boardSelected) {
        boardSelected.archive(CurrentTimeCalendars.now());
        return boardRepository.save(boardSelected);
    }

    /**
     * Undoes the last change made to a Post-It on a shared board.
     *
     * @param board The shared board containing the Post-It.
     * @param postIt The Post-It to undo the last change.
     * @return A boolean indicating the success of the operation.
     */
    @Transactional
    public boolean undoPostItLastChange(SharedBoard board, PostIt postIt) {
        boolean findCell = true;
        Cell parentCell = null;
        Cell actualCell = null;

        for (Cell c : board.cells()) {
            if (c.postItList().contains(postIt.parent())) {
                parentCell = c;
                break;
            }
        }

        for (Cell c : board.cells()) {
            if (c.postItList().contains(postIt)) {
                actualCell = c;
                break;
            }
        }

        if (parentCell == null) {
            return false;
        } else if (!actualCell.equals(parentCell)) {
            if (!board.availableCells().contains(parentCell)) {
                return false;
            }
        }

        deleteStatePostIt(board, postIt);

        boolean success = parentCell.undoPostIt(postIt);

        System.out.println(success+ "UNDO SUCCESS");

        if (success) {
            registerPrimaryBoardModification(board, postIt, ModificationType.UNDO, null);
            boardRepository.save(board);
            return true;
        } else return false;
    }

    /**
     * Prints the representation of the given shared board as a table to the console.
     *
     * @param board The shared board that is to be printed as a table.
     */
    public void printBoardAsTable(SharedBoard board){
        board.printSharedBoard();
    }

    /**
     * Returns the board modifications of the given shared board.
     *
     * @param board The shared board whose modifications are to be returned.
     * @return A set of the board modifications.
     */
    public Set<BoardModification> boardModifications(SharedBoard board) {
        return board.modifications();
    }


    /**
     * Registers a board modification of type CREATION, DELETE OR UNDO for a specific post-it and cell.
     *
     * @param board The shared board where the modification has occurred.
     * @param createdPostIt The post-it that was modified.
     * @param type The type of the modification.
     * @param cell The cell where the post-it is located.
     */
    public void registerPrimaryBoardModification(SharedBoard board, PostIt createdPostIt, ModificationType type, Cell cell) {
        switch (type) {
            case CREATION:
                board.addCreationModification(createdPostIt, cell);
                break;
            case DELETE:
                board.addDeleteModification(createdPostIt, cell);
                break;
            case UNDO:
                board.addUndoModification(createdPostIt, cell);
                break;
        }
    }


    /**
     * Registers a content modification of a PostIt on a shared board.
     *
     * @param board The shared board containing the PostIt.
     * @param createdPostIt The PostIt that was modified.
     * @param cell The cell containing the PostIt.
     * @param newContent The new content for the PostIt.
     */
    public void registerContentBoardModification(SharedBoard board, PostIt createdPostIt, Cell cell, Content newContent) {
        board.addContentModification(createdPostIt, cell, newContent);

    }

    /**
     * Registers a placement modification (Move operation) of a PostIt on a shared board.
     *
     * @param board The shared board containing the PostIt.
     * @param createdPostIt The PostIt that was moved.
     * @param oldCell The cell that previously contained the PostIt.
     * @param newCell The cell that now contains the PostIt.
     */
    public void registerPlacementBoardModification(SharedBoard board, PostIt createdPostIt, Cell oldCell, Cell newCell) {
        board.addPlacementModification(createdPostIt, oldCell, newCell);
    }


    /**
     * Retrieves all archived boards from the database owned by the given user.
     *
     * @param authenticatedUser The system user who owns the boards.
     * @return An Iterable of the archived boards owned by the given user.
     */
    public Iterable<SharedBoard> allBoardsArchivedOwnedByUser(SystemUser authenticatedUser) {
        return boardRepository.findBoardsArchivedOwnedByUser(authenticatedUser);
    }

    /**
     * Restores an archived shared board back to active status.
     *
     * @param boardSelected The shared board to be restored.
     * @return The restored shared board.
     */
    @Transactional
    public SharedBoard restoreBoard(SharedBoard boardSelected) {
        boardSelected.restore(CurrentTimeCalendars.now());
        return boardRepository.save(boardSelected);
    }



    /**
     * Generates a version of a shared board as of a specific timestamp. This version will include all PostIts created on or before the timestamp.
     *
     * @param originalSharedBoard The original shared board.
     * @param timestamp The timestamp to use when generating the board version.
     * @return A new shared board that is a version of the original board as of the specified timestamp.
     */
    public SharedBoard boardVersion(SharedBoard originalSharedBoard, Calendar timestamp) {
        // Create a new shared board with the same properties as the original board
        SharedBoard newSharedBoard = new SharedBoard(
                originalSharedBoard.owner(),
                originalSharedBoard.title(),
                originalSharedBoard.rowNumber(),
                originalSharedBoard.columnNumber()
        );

        newSharedBoard.cells().clear();

        // Iterate over the cells of the original shared board
        for (Cell originalCell : originalSharedBoard.cells()) {
            Cell newCell = new Cell(originalCell.row(), originalCell.column());

            // Iterate over the post-its in the cell
            for (PostIt originalPostIt : originalCell.postItList()) {
                // Check if the post-it meets the criteria

                if (((originalPostIt.timestamp().before(timestamp) || originalPostIt.timestamp().equals(timestamp) ) && originalPostIt.getNext() == null) || ((originalPostIt.timestamp().before(timestamp) || originalPostIt.timestamp().equals(timestamp) ) && (originalPostIt.getNext() != null && originalPostIt.getNext().timestamp().after(timestamp)))) {
                    // Create a new post-it with the same properties as the original post-it
                    PostIt newPostIt = new PostIt(
                            originalPostIt.postItOwner(),
                            originalPostIt.content());
                    // Add the new post-it to the new cell
                    newCell.postItList().add(newPostIt);
                }
            }
            // Add the new cell to the new shared board
            newSharedBoard.cells().add(newCell);
        }

        return newSharedBoard;
    }

    public Optional<SharedBoard> boardById(Long id){
        return boardRepository.ofIdentity(id);
    }

    public SharedBoard lockBoard(SharedBoard board){
        board.lock();
        return boardRepository.save(board);
    }

    public SharedBoard unLockBoard(SharedBoard board){
        board.unLock();
        return boardRepository.save(board);
    }


}
