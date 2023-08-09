package eapli.base.coursemanagement.domain;

import eapli.framework.domain.model.ValueObject;


import java.io.Serializable;

public enum CourseState implements ValueObject, Serializable {
    CLOSED, OPEN, ENROLL, IN_PROGRESS
}
