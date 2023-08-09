package eapli.base.boardmanagement.domain;

import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import validations.util.Preconditions;

import javax.persistence.*;
import java.util.*;

/**
 * @author Joana Nogueira 1201924@isep.ipp.pt
 */
@Entity
@Table(name = "sharedboard")
public class SharedBoard implements AggregateRoot<Long> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "IDBOARD")
    private long id = 1;

    @org.springframework.data.annotation.Version
    private Long version;

    private boolean isLocked;

    @Embedded
    private SharedBoardTitle title;

    @ManyToOne
    private SystemUser owner;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<BoardParticipant> sharedList;

    @Embedded
    private NumberOfRows rowNumber;

    @Embedded
    private NumberOfColumns columnNumber;

    @Enumerated
    private BoardStatus boardStatus;

    @Temporal (TemporalType.DATE)
    private Calendar modifiedOn;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<Cell> cells;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<BoardModification> modifications;

    protected SharedBoard() {
        //ORM
    }

    boolean running = false;

    public SharedBoard(SystemUser owner, SharedBoardTitle title, NumberOfRows rowNumber, NumberOfColumns columnNumber) {
        Preconditions.noneNull(owner);
        this.owner = owner;
        this.rowNumber = rowNumber;
        this.title = title;
        this.columnNumber = columnNumber;
        this.cells = new TreeSet<>();
        this.modifications = new TreeSet<>();
        this.boardStatus = BoardStatus.ACTIVE;
        this.sharedList= new HashSet<>();
        this.isLocked = false;
        createAllBoardCells(rowNumber.numberOfRows(), columnNumber.numberOfColumns());
    }

    public SharedBoard(SystemUser owner, SharedBoardTitle title) {
        Preconditions.noneNull(owner);
        this.owner = owner;
        this.title = title;
        this.cells = new TreeSet<>();
        this.modifications = new TreeSet<>();
        this.boardStatus = BoardStatus.ACTIVE;
        this.sharedList= new HashSet<>();
        this.isLocked = false;
    }

    private void createAllBoardCells(int numRows, int numColumns) {
        Map<Integer, BoardColumn> columns = new TreeMap<>();
        for (int rowNumber = 1; rowNumber <= numRows; rowNumber++) {
            BoardRow row = new BoardRow(rowNumber, null);
            for (int columnNumber = 1; columnNumber <= numColumns; columnNumber++) {
                BoardColumn column = columns.get(columnNumber);
                if (column == null) {
                    column = new BoardColumn(columnNumber, null);
                    columns.put(columnNumber, column);
                }
                Cell cell = new Cell(row, column);
                cells.add(cell);
            }
        }
    }

    public synchronized void startContinuousLoop() {
        if (isLocked) {
            new ContinuousLoop().run();
        }
    }

//    public synchronized void stopContinuousLoop() {
//        running = false;
//    }

    private class ContinuousLoop  {
        public void run() {
            while (isLocked) {
            }
        }
    }


    public void updateRowTitle(String newTitle, int rowNumber) {
        for (Cell cell : cells) {
            if (cell.row().number() == rowNumber) {
                cell.row().updateRowTitle(newTitle);
                break;
            }
        }
    }

    public String getRowTitle(int rowNumber) {
        for (Cell cell : cells) {
            if (cell.row().number() == rowNumber) {
                return cell.row().title();
            }
        }
        return "";
    }

    public String getColumnTitle(int columnNumber) {
        for (Cell cell : cells) {
            if (cell.column().number() == columnNumber) {
                return cell.column().title();
            }
        }
        return "";
    }

    public void updateColumnTitle(String newTitle, int columnNumber) {
        for (Cell cell : cells) {
            if (cell.column().number() == columnNumber) {
                cell.column().updateColumnTitle(newTitle);
                break;
            }
        }
    }

    public void addCreationModification (PostIt createdPostIt, Cell cell){
        BoardModification mod = new BoardModification(createdPostIt, ModificationType.CREATION, cell);
        this.modifications.add(mod);
    }

    public void addDeleteModification (PostIt createdPostIt, Cell cell){
        BoardModification mod = new BoardModification(createdPostIt, ModificationType.DELETE, cell);
        this.modifications.add(mod);
    }

    public void addUndoModification (PostIt createdPostIt, Cell cell){
        BoardModification mod = new BoardModification(createdPostIt, ModificationType.UNDO, cell);
        this.modifications.add(mod);
    }

    public void addContentModification (PostIt postIt, Cell cell, Content content){
        BoardModification mod = new BoardModification(postIt, ModificationType.CONTENT_UPDATE, cell, content);
        this.modifications.add(mod);
    }

    public void addPlacementModification (PostIt createdPostIt, Cell oldCell, Cell newCell){
        BoardModification mod = new BoardModification(createdPostIt, ModificationType.MOVED, oldCell, newCell);
        this.modifications.add(mod);
    }

    public SharedBoardTitle title() {
        return title;
    }

    public SystemUser owner() {
        return owner;
    }

    public NumberOfRows rowNumber() {
        return rowNumber;
    }

    public NumberOfColumns columnNumber() {
        return columnNumber;
    }

    public Set<Cell> cells() {
        return cells;
    }

    public Set<BoardModification> modifications() {
        return modifications;
    }

    public BoardStatus boardStatus() {
        return boardStatus;
    }

    public Set<BoardParticipant> sharedlist(){
        return this.sharedList;
    }

    public boolean isLocked (){
        return this.isLocked;
    }

    public void lock (){
        this.isLocked = true;
    }

    public void unLock (){
        this.isLocked = false;
    }

    @Override
    public int compareTo(Long other) {
        return AggregateRoot.super.compareTo(other);
    }

    @Override
    public Long identity() {
        return this.id;
    }

    @Override
    public boolean hasIdentity(Long id) {
        return AggregateRoot.super.hasIdentity(id);
    }

    @Override
    public boolean equals(final Object o) {
        return DomainEntities.areEqual(this, o);
    }

    @Override
    public int hashCode() {
        return DomainEntities.hashCode(this);
    }

    @Override
    public boolean sameAs(final Object other) {
        return DomainEntities.areEqual(this, other);
    }

    public void archive(Calendar now) {
        boardStatus = BoardStatus.ARCHIVED;
        this.modifiedOn=now;
    }

    public Set<Cell> availableCells() {
        Set<Cell> availableCells = new TreeSet<>();
        for (Cell cell : cells) {
            if (cell.latestActivePostIt() == null) {
                availableCells.add(cell);
            }
        }
        return availableCells;
    }

    public Set<Cell> cellOfBoardAndUser(SystemUser user){
        Set<Cell> cellOfBoardAndUser = new TreeSet<>();
        for (Cell cell : cells){
            if (cell.latestActivePostIt() != null) {
                if (cell.latestActivePostIt().postItOwner().equals(user)) {
                    cellOfBoardAndUser.add(cell);
            }

            }
        }
        return cellOfBoardAndUser;
    }

    public Set<Cell> cellsAlsoWithDeletedOfBoardAndUser(SystemUser user){
        Set<Cell> cellOfBoardAndUser = new TreeSet<>();
        for (Cell cell : cells){
            if (cell.latestPostIt() != null) {
                if (cell.latestPostIt().postItOwner().equals(user)) {
                    cellOfBoardAndUser.add(cell);
                }

            }
        }
        return cellOfBoardAndUser;
    }

    public void printSharedBoard() {
        int rows = this.rowNumber.numberOfRows();
        int columns = this.columnNumber.numberOfColumns();

        // Translate the Set into a 2D array for easier access
        PostIt[][] board = new PostIt[rows][columns];
        for (Cell cell : this.cells) {
            int rowIndex = cell.row().number() - 1;
            int columnIndex = cell.column().number() - 1;
            board[rowIndex][columnIndex] = cell.latestActivePostIt();
        }

        // Print the top border
        printHorizontalBorder(columns);

        // Print the board
        for (int i = 0; i < rows; i++) {
            // Split the content into lines
            String[][] rowLines = new String[columns][];
            int maxLines = 1; // The minimum number of lines per row
            for (int j = 0; j < columns; j++) {
                PostIt postIt = board[i][j];
                if (postIt == null || postIt.isDeleted()) {
                    rowLines[j] = new String[] { " ".repeat(15) }; // Empty cell or deleted post-it
                } else {
                    String content = postIt.content().toString();
                    content = postIt.postItOwnerIdentifier() + ":" + content;
                    // Split the content into lines of 15 characters each
                    rowLines[j] = content.split("(?<=\\G.{15})");
                    maxLines = Math.max(maxLines, rowLines[j].length);
                }
            }
            // Print the lines
            for (int line = 0; line < maxLines; line++) {
                for (int j = 0; j < columns; j++) {
                    if (line < rowLines[j].length) {
                        // This cell has a line for this row
                        System.out.printf("| %-15s ", rowLines[j][line]);
                    } else {
                        // This cell doesn't have a line for this row
                        System.out.printf("| %-15s ", "");
                    }
                }
                System.out.println("|");
            }
            printHorizontalBorder(columns); // Border between rows
        }
        System.out.println();

    }

    // Helper method to print a horizontal border
    private void printHorizontalBorder(int columns) {
        for (int i = 0; i < columns; i++) {
            System.out.print("+" + "-".repeat(17)); // Each cell is 15 characters wide
        }
        System.out.println("+"); // End of border
    }

    @Override
    public String toString() {
        return String.format("| BOARD %d | Title: %s |", id, title);
    }

    /**
     * Share a board with other users
     * @param user
     * @param permission - Users may have read or write access to the board
     */
    public void shareABoard(SystemUser user, BoardPermission permission){
        BoardParticipant newParticipant = new BoardParticipant(user, permission);
        boolean participantExists = false;
        Iterator<BoardParticipant> iterator = this.sharedList.iterator();
        while (iterator.hasNext()) {
            BoardParticipant existingParticipant = iterator.next();
            if (existingParticipant.user().identity().equals(user.identity())) {
                existingParticipant.updatePermission(permission);
                participantExists = true;
                break;
            }
        }
        if (!participantExists) {
            this.sharedList.add(newParticipant);
        }
    }

    public boolean isActive() {
        return this.boardStatus == BoardStatus.ACTIVE;
    }

    public void restore(Calendar now) {
        boardStatus = BoardStatus.ACTIVE;
        this.modifiedOn=now;
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

                if (((originalPostIt.timestamp().before(timestamp) || originalPostIt.timestamp().equals(timestamp)) && originalPostIt.getNext() == null) || ((originalPostIt.timestamp().before(timestamp) || originalPostIt.timestamp().equals(timestamp)) && (originalPostIt.getNext() != null && originalPostIt.getNext().timestamp().after(timestamp)))) {
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

    public boolean hasRowTitles() {
        if (cells.isEmpty()) {
            return false; // No cells available
        }

        Cell firstCell = cells.iterator().next(); // Get the first cell
        BoardRow firstRow = firstCell.row();

        return firstRow.title() != null; // Assuming `hasTitle()` method exists in `BoardRow` class
    }

}
