package eapli.base.meetingmanagement.domain;

import eapli.framework.domain.model.ValueObject;

import java.io.Serializable;

public enum MeetingStatus implements ValueObject, Serializable {
    ACTIVE, CANCELED
}
