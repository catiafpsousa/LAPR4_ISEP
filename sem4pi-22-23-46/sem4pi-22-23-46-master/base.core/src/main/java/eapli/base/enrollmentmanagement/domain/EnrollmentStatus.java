package eapli.base.enrollmentmanagement.domain;

import eapli.framework.domain.model.ValueObject;

import java.io.Serializable;

public enum EnrollmentStatus implements ValueObject, Serializable {
    REQUESTED, ACCEPTED, REJECTED
}
