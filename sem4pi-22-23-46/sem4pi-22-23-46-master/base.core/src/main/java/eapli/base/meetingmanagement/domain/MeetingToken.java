/*
 * Copyright (c) 2013-2023 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package eapli.base.meetingmanagement.domain;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;
import java.util.UUID;

/**
 * A booking token used to identify a booking. It will be useful for the
 * delivery operation. A booking token is a unique string of characters.
 *
 * <p>
 * technically we are using a UUID as implementation but that detail is hidden
 * from the user of this class
 *
 * @author Paulo Gandra Sousa
 *
 */

public class MeetingToken implements ValueObject, Comparable<MeetingToken> {

    private static final long serialVersionUID = -1820803667373631580L;

    private final String token;

    private MeetingToken() {
        token = UUID.randomUUID().toString();
    }

    private MeetingToken(final String value) {
        Preconditions.nonEmpty(value);
        token = UUID.fromString(value).toString();
    }



    /**
     * Factory method.
     *
     * @param value
     * @return a new token
     */
    public static MeetingToken valueOf(final String value) {
        return new MeetingToken(value);
    }

    /**
     * Constructs a new unique token
     *
     * @return a new unique token
     */
    public static MeetingToken newToken() {
        return new MeetingToken();
    }

    @Override
    public String toString() {
        return token;
    }

    @Override
    public int compareTo(final MeetingToken o) {
        return token.compareTo(o.token);
    }
}
