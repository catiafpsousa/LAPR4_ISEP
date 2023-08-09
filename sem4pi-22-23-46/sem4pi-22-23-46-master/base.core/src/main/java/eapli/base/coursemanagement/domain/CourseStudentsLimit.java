package eapli.base.coursemanagement.domain;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import eapli.framework.domain.model.ValueObject;
import eapli.base.Application;


import lombok.Value;
import lombok.experimental.Accessors;
import validations.util.Preconditions;

import java.io.Serializable;
import java.util.regex.Pattern;
import javax.persistence.Embeddable;


/**
 * Limitations of number of students in a Course
 */

@Embeddable
//@Value
//@Accessors(fluent = true)
public class CourseStudentsLimit implements ValueObject, Serializable {


    private static final long serialVersionUID = 1L;


    private final int min;
    private final int max;

    protected CourseStudentsLimit(final int min, final int max) {
        Preconditions.isPositive(min, "Number of MIN students must be greater than 0");
        Preconditions.isPositive(max, "Number of MAX students must be greater than 0");
        Preconditions.isWithinMargin(min, Application.settings().getMinStudentsLimit(),Application.settings().getMaxStudentsLimit(), "Invalid MIN value...");
        Preconditions.isWithinMargin(max, Application.settings().getMinStudentsLimit(), Application.settings().getMaxStudentsLimit(), "Invalid MAX value...");
        Preconditions.isGreater(min,max, "Invalid range: MAX is not greater than MIN");


        this.min = min;
        this.max = max;
    }

    protected CourseStudentsLimit() {
        // ORM only
        min = max = 0;
    }

    /**
     * builds a Course Students Limit object
     *
     * @param min
     * @param max
     * @return a Course Students Limit
     */
    public static CourseStudentsLimit valueOf(final int min, final int max) {
        return new CourseStudentsLimit(min, max);
    }


    @Override
    public String toString() {
        return "Minimum nr. of Students: " + min + "\nMaximum nr. of Students: " + max;
    }
}

