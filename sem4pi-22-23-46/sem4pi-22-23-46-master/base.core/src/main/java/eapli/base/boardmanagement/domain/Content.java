package eapli.base.boardmanagement.domain;
import eapli.framework.domain.model.ValueObject;
import validations.util.Preconditions;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

/**
 * Represents the content of a PostIt, implements ValueObject and Serializable.
 * Each instance of Content contains a String representing its text.
 */
@Embeddable
public class Content implements ValueObject, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The text content of the Content object.
     */
    @Column(name = "TEXTCONTENT")
    private String textContent;

    /**
     * The image content of the Content object.
     */
    @Column(name = "IMAGECONTENT", length = 10485760)
    private byte[] imageContent;

    /**
     * Constructor for creating a Content object with only text content.
     *
     * @param textContent The text content of the Content object.
     */
    public Content(String textContent) {
        Preconditions.nonEmpty(textContent);
        this.textContent = textContent;
        this.imageContent = null;
    }

    /**
     * Constructor for creating a Content object with only image content.
     *
     * @param imageContent The image content of the Content object.
     */
    public Content(byte[] imageContent) {
        Preconditions.nonNull(imageContent);
        Preconditions.isTrue(imageContent.length <= 10485760, "Image content exceeds the maximum length");
        this.textContent = null;
        this.imageContent = imageContent;
    }

    /**
     * Constructor for creating a Content object with both text and image content.
     *
     * @param textContent  The text content of the Content object.
     * @param imageContent The image content of the Content object.
     */
    public Content(String textContent, byte[] imageContent) {
        Preconditions.nonEmpty(textContent);
        Preconditions.nonNull(imageContent);
        this.textContent = textContent;
        this.imageContent = imageContent;
    }

    /**
     * Protected constructor for the Content class.
     * Used only by ORM (Object Relational Mapping).
     */
    protected Content() {
        // ORM only
        this.textContent = "";
        this.imageContent = null;
    }

    /**
     * Returns a new Content object with the provided text content.
     *
     * @param textContent The text content of the new Content object.
     * @return A new Content object.
     */
    public static Content withTextContent(final String textContent) {
        return new Content(textContent);
    }

    /**
     * Returns a new Content object with the provided image content.
     *
     * @param imageContent The image content of the new Content object.
     * @return A new Content object.
     */
    public static Content withImageContent(final byte[] imageContent) {
        return new Content(imageContent);
    }

    /**
     * Returns a new Content object with the provided text content and image content.
     *
     * @param textContent  The text content of the new Content object.
     * @param imageContent The image content of the new Content object.
     * @return A new Content object.
     */
    public static Content withTextAndImageContent(final String textContent, final byte[] imageContent) {
        return new Content(textContent, imageContent);
    }

    /**
     * Returns the text content of this object.
     *
     * @return The text content of this object.
     */
    public String textContent() {
        return textContent;
    }

    /**
     * Returns the image content of this object.
     *
     * @return The image content of this object.
     */
    public byte[] imageContent() {
        return imageContent;
    }

    /**
     * Checks if the Content object has text content.
     *
     * @return true if the Content object has text content, false otherwise.
     */
    public boolean hasTextContent() {
        return textContent != null;
    }

    /**
     * Checks if the Content object has image content.
     *
     * @return true if the Content object has image content, false otherwise.
     */
    public boolean hasImageContent() {
        return imageContent != null;
    }

    @Override
    public String toString() {
        if (hasTextContent() && hasImageContent()) {
            return textContent + " [Image Content] "  /* + imageContentAsString()*/;
        } else if (hasTextContent()) {
            return textContent;
        } else if (hasImageContent()) {
            return "[Image Content]" /* + imageContentAsString()*/;
        } else {
            return "";
        }
    }

    private String imageContentAsString() {
        return Base64.getEncoder().encodeToString(imageContent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return Objects.equals(textContent, content.textContent) && Arrays.equals(imageContent, content.imageContent);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(textContent);
        result = 31 * result + Arrays.hashCode(imageContent);
        return result;
    }
}