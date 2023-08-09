package eapli.base.exammanagement.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.base.coursemanagement.domain.CourseCode;
import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ExamTitle implements ValueObject, Comparable<ExamTitle>, Serializable {
    private static final long serialVersionUID = 1L;

    private final String title;

    public ExamTitle() {
        this.title = "";
    }

    public ExamTitle(final String title) {
        Preconditions.nonEmpty(title, "Exam title should neither be null nor empty");
        this.title = title;
    }


    public static ExamTitle valueOf(final String title) {
        return new ExamTitle(title);
    }

    public String toString() {
        return this.title;
    }


    @Override
    public int compareTo(ExamTitle o) {
        return this.title.compareTo(o.title);
    }


    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ExamTitle)) {
            return false;
        } else {
            ExamTitle other = (ExamTitle)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$title = this.title;
                Object other$title = other.title;
                if (this$title == null) {
                    if (other$title != null) {
                        return false;
                    }
                } else if (!this$title.equals(other$title)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ExamTitle;
    }


    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $title = this.title;
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        return result;
    }

}
