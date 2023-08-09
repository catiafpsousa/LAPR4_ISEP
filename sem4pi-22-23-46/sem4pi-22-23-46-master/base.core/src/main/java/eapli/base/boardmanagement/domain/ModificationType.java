package eapli.base.boardmanagement.domain;

import eapli.framework.domain.model.ValueObject;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import java.io.Serializable;



/**
 * @author Pedro Garrido 1182090@isep.ipp.pt
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */

/**
 * Enumerated to represent the different types of modifications.
 */
public enum ModificationType implements ValueObject, Serializable {

    /**
     * Represents the creation of a new PostIt.
     */
    CREATION,

    /**
     * Represents the update of the content of a PostIt.
     */
    CONTENT_UPDATE,

    /**
     * Represents the moving of a PostIt from one cell to another.
     */
    MOVED,

    /**
     * Represents the deletion of a PostIt.
     */
    DELETE,

    /**
     * Represents the undo of a previous modification.
     */
    UNDO
}
