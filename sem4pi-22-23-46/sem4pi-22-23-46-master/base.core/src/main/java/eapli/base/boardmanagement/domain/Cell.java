package eapli.base.boardmanagement.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.base.util.SetUtils;
import validations.util.Preconditions;

/**
 * A class representing a Cell entity.
 * Each cell is part of a board and can contain multiple Post-It notes.
 * The cell class provides methods for creating, updating, and manipulating Post-It notes.
 * This class implements Serializable and Comparable interfaces.
 */
@Entity
@Table(name = "cell")
public class Cell implements Serializable, Comparable<Cell> {
    private static final long serialVersionUID = 1L;


    /**
     * The unique identifier for each cell instance
     */
    @Id
    @Column(name = "IDCELL")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idCell;

    /**
     * The column location of the cell on a board
     */
    @Embedded
    private BoardColumn boardColumn;

    /**
     * The row location of the cell on a board
     */
    @Embedded
    private BoardRow boardRow;

    /**
     * The set of PostIt notes that are currently on this cell
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PostIt> postIts;

    /**
     * Default constructor used by the ORM.
     */
    protected Cell() {
        //ORM
    }

    /**
     * Constructs a cell with specified row and column on the board.
     *
     * @param boardRow    the row of the cell on the board.
     * @param boardColumn the column of the cell on the board.
     */
    public Cell(BoardRow boardRow, BoardColumn boardColumn) {
        Preconditions.noneNull(boardRow);
        Preconditions.noneNull(boardColumn);
        this.boardRow = boardRow;
        this.boardColumn = boardColumn;
        this.postIts = new TreeSet<>();
    }

    /**
     * @return the row of the cell on the board.
     */
    public BoardRow row() {
        return this.boardRow;
    }

    /**
     * @return the column of the cell on the board.
     */
    public BoardColumn column() {
        return this.boardColumn;
    }

    /**
     * @return a set of Post-It notes in the cell.
     */
    public Set<PostIt> postItList() {
        return this.postIts;
    }

    /**
     * Checks if the cell can have a new Post-It.
     * @return true if the cell has no Post-It or the last Post-It is deleted, false otherwise.
     */
    public boolean canHavePostIt() {
        PostIt lastPostIt = SetUtils.getLastElement(postIts);
        if (this.postItList().size() == 0 || lastPostIt.isDeleted()) {
            return true;
        } else
            return false;
    }

    /**
     * Adds a new Post-It to the cell, if it can have one.
     * @param owner the system user who owns the Post-It.
     * @param content the content of the Post-It.
     * @return true if the Post-It was added, false otherwise.
     */
    public boolean addPostIt(SystemUser owner, Content content) {
        if (canHavePostIt()) {
            this.postItList().add(new PostIt(owner, content));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds a moved Post-It to the cell, if it can have one.
     * @param parent the original Post-It being moved.
     * @return true if the Post-It was moved successfully, false otherwise.
     */
    public boolean addMovedPostIt(PostIt parent) {
        if (canHavePostIt()) {
            this.postItList().add(new PostIt(parent, parent.content()));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates a Post-It note's content.
     * @param postIt the Post-It to update.
     * @param content the new content for the Post-It.
     * @return true always, indicating that the Post-It was updated successfully.
     */
    public boolean updatePostIt(PostIt postIt, Content content) {
        PostIt next = new PostIt(postIt, content);
        if (this.postItList().add(next))
        return true;
        else return false;
    }


    /**
     * Compares this cell with the specified cell for order.
     * Cells are ordered first by row number, then by column number.
     * @param other the cell to be compared.
     * @return a negative integer, zero, or a positive integer as this cell is less than, equal to, or greater than the specified cell.
     */
    @Override
    public int compareTo(Cell other) {
        if (this.boardRow.number() == other.boardRow.number()) {
            return this.boardColumn.number() - other.boardColumn.number();
        } else {
            return this.boardRow.number() - other.boardRow.number();
        }
    }

    /**
     * Retrieves the latest Post-It note that is not deleted.
     * @return the latest not-deleted Post-It or null if all Post-Its are deleted or there are no Post-Its.
     */
    public PostIt latestActivePostIt() {
        if (this.postItList().isEmpty()) return null;
        for (PostIt p : this.postItList()) {
            if (!p.isDeleted()) return p;
        }
        return null;
    }


    /**
     * Retrieves the latest Post-It note that is not deleted.
     * @return the latest not-deleted Post-It or null if all Post-Its are deleted or there are no Post-Its.
     */
    public PostIt latestPostIt() {
        if (this.postItList().isEmpty()) return null;
        for (PostIt p : this.postItList()) {
            if (!p.isDeleted() || (p.isDeleted()&& !p.hasNext())) return p;
        }
        return null;
    }

    /**
     * Undoes the change on a Post-It, if previous cell cell can have one.
     * @param parent the deleted Post-It to be undone.
     * @return true if the Post-It was successfully undone, false otherwise.
     */
    public boolean undoPostIt(PostIt parent) {
        if (canHavePostIt()) {
            PostIt postItFromUndo = new PostIt(parent);
            this.postItList().add(postItFromUndo);
            parent.changeNextPostIt(postItFromUndo);
            return true;
        } else {
            return false;
        }
    }
}
