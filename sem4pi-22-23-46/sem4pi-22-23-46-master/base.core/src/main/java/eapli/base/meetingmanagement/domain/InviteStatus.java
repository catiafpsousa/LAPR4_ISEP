package eapli.base.meetingmanagement.domain;

import eapli.framework.domain.model.ValueObject;

import java.io.Serializable;

public enum InviteStatus implements ValueObject, Serializable {
    ACCEPT, REJECT, UNKNOWN
}
