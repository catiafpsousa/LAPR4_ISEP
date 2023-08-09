package eapli.base.boardmanagement.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;

import javax.persistence.Embeddable;
import java.io.Serializable;



/**
 * @author Pedro Garrido 1182090@isep.ipp.pt
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */

/**
 * Represents a description of a board modification, implements ValueObject and Serializable.
 * Each ModificationDescription instance includes a string description.
 */
@Embeddable
public class ModificationDescription implements ValueObject, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The description of the modification.
     */
    @JsonProperty
    private String description;


    /**
     * Protected constructor for the ModificationDescription class.
     * Used only by ORM (Object Relational Mapping)
     */
    protected ModificationDescription() {
    }

    //CREATION / DELETE / UNDO

    /**
     * Constructor for creation, delete, undo type modifications.
     *
     * @param postIt The PostIt associated with the modification.
     * @param type   The type of the modification.
     * @param cell   The cell being modified.
     */
    public ModificationDescription(PostIt postIt, ModificationType type, Cell cell) {
        if (type.equals(ModificationType.CREATION)) {
            this.description = "PostIt created by " + postIt.postItOwner().identity().toString() +  " on cell (" + cell.row().number() + ", " + cell.column().number() + ")";
        } else if (type.equals(ModificationType.DELETE)) {
            this.description = "PostIt deleted by " + postIt.postItOwner().identity().toString() + "from cell (" + cell.row().number() + ", " + cell.column().number() + ")";
        } else if (type.equals(ModificationType.UNDO)) {
            this.description = "Undo operation on PostIt by " + postIt.postItOwner().identity().toString();
        }
    }

    //UPDATE CONTENT

    /**
     * Constructor for content update type modifications.
     *
     * @param postIt     The PostIt associated with the modification.
     * @param cell       The cell being modified.
     * @param newContent The new content for the PostIt.
     */
    public ModificationDescription(PostIt postIt, Cell cell, Content newContent) {
        this.description = "PostIt of cell (" + cell.row().number() + ", " + cell.column().number() + ")" + "changed content: " + postIt.content() + " -->" + newContent  + "(done by " + postIt.postItOwner().identity().toString()+ ")";
    }

    //MOVE

    /**
     * Constructor for move type modifications.
     *
     * @param postIt  The PostIt associated with the modification.
     * @param oldCell The cell being moved from.
     * @param newCell The cell being moved to.
     */
    public ModificationDescription(PostIt postIt, Cell oldCell, Cell newCell) {
        this.description = "PostIt moved by " + postIt.postItOwner().identity().toString() + " from cell (" + oldCell.row().number() + ", " + oldCell.column().number() + ") to (" + newCell.row().number() + ", " + newCell.column().number() + ")";
    }

    public String toString() {
        return this.description;
    }


    /**
     * Checks if this object is equal to another.
     *
     * @param o The object to compare with this object.
     * @return true if the objects are equal, false otherwise.
     */
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ModificationDescription)) {
            return false;
        } else {
            ModificationDescription other = (ModificationDescription) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$description = this.description;
                Object other$description = other.description;
                if (this$description == null) {
                    if (other$description != null) {
                        return false;
                    }
                } else if (!this$description.equals(other$description)) {
                    return false;
                }
                return true;
            }
        }
    }

    /**
     * Helper method to support the equals method.
     *
     * @param other The other object in the equals comparison.
     * @return true if the other object can be cast to ModificationDescription, false otherwise.
     */
    protected boolean canEqual(final Object other) {
        return other instanceof ModificationDescription;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $description = this.description;
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        return result;
    }
}
