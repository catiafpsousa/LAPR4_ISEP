package eapli.base.boardmanagement.domain;

import eapli.base.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.time.util.CurrentTimeCalendars;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Calendar;


/**
 * @author Pedro Garrido 1182090@isep.ipp.pt
 * @author Pedro Sousa 1201326@isep.ipp.pt
 */

/**
 * Represents a PostIt, an object that contains content and is owned by a system user. This class is part of the data layer
 * and is used to map the PostIt object to a relational model. PostIts can be created, updated, and deleted.
 * Each PostIt can be linked to its parent and next PostIt, forming a chain of modifications.
 */
@Entity
@Table(name = "postit")
public class PostIt implements Serializable, Comparable<PostIt> {
    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier of the PostIt.
     */
    @Id
    @Column(name = "IDPOSTIT")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    /**
     * The previous version of this PostIt.
     */
    @OneToOne
    private PostIt parent;

    /**
     * The next version of this PostIt.
     */
    @OneToOne
    private PostIt next;

    /**
     * The owner of this PostIt.
     */
    @OneToOne
    private SystemUser owner;

    /**
     * The content of this PostIt.
     */
    @Embedded
    private Content content;

    /**
     * The timestamp when this PostIt was last modified.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar timestamp;

    /**
     * The flag indicating whether this PostIt is deleted.
     */
    private boolean isDeleted;


    public Calendar timestamp() {
        return timestamp;
    }

    /**
     * Default constructor used by the ORM.
     */
    protected PostIt() {
        //ORM
    }

    // CREATE
    /**
     * Constructs a new PostIt with the given owner and content.
     *
     * @param owner The user who owns the PostIt.
     * @param content The content of the PostIt.
     */
    public PostIt(SystemUser owner, Content content) {
        this.parent = null;
        this.next = null;
        this.owner = owner;
        this.content = content;
        this.timestamp = CurrentTimeCalendars.now();
        this.isDeleted = false;
    }

    // MOVE / UPDATE CONTENT / DELETE
    /**
     * Constructs a new PostIt, creating a new version of an existing PostIt.
     *
     * @param parent The previous version of the PostIt.
     * @param newContent The updated content for the PostIt.
     */
    public PostIt(PostIt parent, Content newContent) {
        this.parent = parent;
        parent.next = this;
        this.next = null;
        this.owner = parent.owner;
        this.content = newContent;
        this.isDeleted = false;
        this.timestamp = CurrentTimeCalendars.now();
    }

    // RECREATION FROM UNDO
    /**
     * Constructs a new PostIt, undoing a previous action.
     *
     * @param parent The previous version of the PostIt.
     */
    public PostIt(PostIt parent) {
        this.parent = parent.parent().parent();
        this.next = null;
        this.owner = parent.owner;
        this.content = parent.parent().content;
        this.isDeleted = false;
        this.timestamp = CurrentTimeCalendars.now();
    }

    /**
     * Marks this PostIt as deleted and updates the timestamp.
     */
    public void deletePostIt() {
        this.isDeleted = true;
        this.timestamp = CurrentTimeCalendars.now();
    }

    /**
     * Marks this PostIt as deleted.
     */
    public void deleteStatePostIt() {
        this.isDeleted = true;
    }

    /**
     * Retrieves the owner of the PostIt.
     *
     * @return the SystemUser that owns the PostIt.
     */
    public SystemUser postItOwner() {
        return this.owner;
    }

    /**
     * Retrieves the next PostIt in the sequence.
     *
     * @return the next PostIt object.
     */
    public PostIt getNext(){
        return this.next;
    }

    /**
     * Returns the identifier of the PostIt owner.
     *
     * @return the identifier string.
     */
    public String postItOwnerIdentifier() {
        String[] identifier = this.owner.identity().toString().split("@");
        return identifier[0];
    }

    /**
     * Retrieves the parent of the PostIt.
     *
     * @return the parent PostIt object.
     */
    public PostIt parent(){
        return this.parent;
    }

    /**
     * Retrieves the content of the PostIt.
     *
     * @return the Content object associated with the PostIt.
     */
    public Content content() {
        return this.content;
    }

    /**
     * Retrieves the unique identifier of the PostIt.
     *
     * @return the id of the PostIt.
     */
    public Long id(){
        return this.id;
    }

    /**
     * Changes the next PostIt in the sequence.
     *
     * @param nextPostIt The new PostIt to be set as the next one.
     * @return the updated next PostIt.
     */
    public PostIt changeNextPostIt(PostIt nextPostIt) {
        return this.next = nextPostIt;
    }

    /**
     * Checks if the PostIt has been marked as deleted.
     *
     * @return true if the PostIt has been marked as deleted, false otherwise.
     */
    public boolean isDeleted() {
        return this.isDeleted;
    }

    /**
     * Checks if this PostIt has a next PostIt in the sequence.
     *
     * @return true if the PostIt has a next one, false otherwise.
     */
    public boolean hasNext(){
        return this.next != null;
    }

    @Override
    public String toString() {
        return "PostIt{" +
                "id=" + id +
                ", content=" + content +
                ", timestamp=" + timestamp +
                ", isDeleted=" + isDeleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostIt postIt = (PostIt) o;

        if (isDeleted != postIt.isDeleted) return false;
        if (id != postIt.id()) return false;
        if (postIt.parent() != null){
        if (!parent.equals(postIt.parent)) return false;}
        if (!owner.equals(postIt.owner)) return false;
        if (!content.equals(postIt.content)) return false;
        return timestamp.equals(postIt.timestamp);
    }


    @Override
    public int compareTo(PostIt other) {
        if (this.id == other.id) {
            return this.timestamp.compareTo(other.timestamp);
        } else {
            return (int) (this.id - other.id);
        }
    }



    @Override
    public int hashCode() {
        int result = owner.hashCode();
        result = 31 * result + content.hashCode();
        result = 31 * result + timestamp.hashCode();
        result = 31 * result + (isDeleted ? 1 : 0);
        return result;
    }

}
