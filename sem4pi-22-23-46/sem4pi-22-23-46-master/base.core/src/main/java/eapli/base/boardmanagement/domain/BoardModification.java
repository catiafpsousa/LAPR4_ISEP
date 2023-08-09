package eapli.base.boardmanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.time.util.CurrentTimeCalendars;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;


/**
 * @author Pedro Garrido 1182090@isep.ipp.pt
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */

/**
 * Represents a modification made to the board, implements Serializable, ValueObject, Comparable<BoardModification>.
 * Each modification is linked to a specific post-it, has a type, timestamp and a description.
 *
 */
@Entity
@Table(name = "modification")
public class BoardModification implements Serializable, ValueObject, Comparable<BoardModification> {
    private static final long serialVersionUID = 1L;

    /**
     * Identifier for the modification.
     */
    @Id
    @Column(name = "IDMOD")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idMod;

    /**
     * The PostIt object associated with this modification.
     */
    @OneToOne
    private PostIt postIt;

    /**
     * Type of the modification.
     */
    @Enumerated(EnumType.STRING)
    private ModificationType type;

    /**
     * Timestamp when the modification occurred.
     */
    private Calendar modifiedOn;

    /**
     * Description of the modification.
     */
    @Embedded
    private ModificationDescription description;

    /**
     * Default constructor for the BoardModification class.
     */
    public BoardModification (){

    }

    /**
     * Constructor for creation, delete, undo type modifications.
     * @param postIt The PostIt associated with the modification.
     * @param type The type of the modification.
     * @param cell The cell being modified.
     */
    //CREATION / DELETE / UNDO
    public BoardModification(PostIt postIt, ModificationType type, Cell cell) {
        this.modifiedOn = CurrentTimeCalendars.now();
        this.postIt = postIt;
        this.type = type;
        this.description = new ModificationDescription(postIt, type, cell);
    }

    /**
     * Constructor for content update type modifications.
     * @param postIt The PostIt associated with the modification.
     * @param type The type of the modification.
     * @param cell The cell being modified.
     * @param newContent The new content for the PostIt.
     */
    //UPDATE CONTENT
    public BoardModification(PostIt postIt, ModificationType type, Cell cell, Content newContent) {
        this.modifiedOn = CurrentTimeCalendars.now();
        this.postIt = postIt;
        this.type = type;
        this.description = new ModificationDescription(postIt, cell, newContent);
    }

    /**
     * Constructor for move type modifications.
     * @param postIt The PostIt associated with the modification.
     * @param type The type of the modification.
     * @param oldCell The cell being moved from.
     * @param newCell The cell being moved to.
     */
    //MOVE
    public BoardModification(PostIt postIt, ModificationType type, Cell oldCell, Cell newCell) {
        this.modifiedOn = CurrentTimeCalendars.now();
        this.postIt = postIt;
        this.type = type;
        this.description = new ModificationDescription(postIt, oldCell, newCell);
    }

    /**
     * Returns the ID of the modification.
     * @return The ID of the modification.
     */
    public long id() {
        return idMod;
    }

    /**
     * Returns the PostIt associated with the modification.
     * @return The PostIt associated with the modification.
     */
    public PostIt postIt() {
        return postIt;
    }

    /**
     * Returns the type of the modification.
     * @return The type of the modification.
     */
    public ModificationType type() {
        return type;
    }


    /**
     * Returns the description of the modification.
     * @return The description of the modification.
     */
    public ModificationDescription description() {
        return description;
    }

    /**
     * Returns the timestamp of the modification.
     * @return The timestamp of the modification.
     */
    public Calendar timestamp(){
        return this.modifiedOn;
    }

    /**
     * Implements the Comparable interface for comparing two BoardModification objects based on their ID.
     * @param o The BoardModification object to compare this object to.
     * @return A negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(BoardModification o) {
        return (int) (this.idMod - o.idMod);
    }
}
