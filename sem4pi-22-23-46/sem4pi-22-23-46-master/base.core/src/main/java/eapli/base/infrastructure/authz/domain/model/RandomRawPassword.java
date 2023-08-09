/*
 * Copyright (c) 2013-2022 the original author or authors.
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
package eapli.base.infrastructure.authz.domain.model;

import eapli.framework.domain.model.Immutable;
import eapli.framework.strings.RandomString;
import eapli.framework.strings.util.Strings;
import lombok.EqualsAndHashCode;

/**
 * A random password. Can be used to present as a suggestion to the user upon
 * registration.
 * <p>
 * Inspired by <a href=
 * "https://mkyong.com/java/java-password-generator-example/">MKyong</a>.
 *
 * @author Paulo Gandra Sousa 28/05/2020
 *
 */
@Immutable
@EqualsAndHashCode
public final class RandomRawPassword {

    private static final int DEFAULT_LENGTH = 12;

    private static final String LOWER_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CHARS = LOWER_CHARS.toUpperCase();
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL = "_-+*/@=><#?!.%():;[]|{}&$€";

    public static final String ALLOWED_CHARS = LOWER_CHARS + UPPER_CHARS + NUMBERS + SPECIAL;

    private final String rawPassword;

    /**
     * Constructs a random password with the default length.
     */
    public RandomRawPassword() {
        this(DEFAULT_LENGTH);
    }

    /**
     * Constructs a random password with the desired length.
     *
     * @param length
     */
    public RandomRawPassword(final int length) {
        if (length < 3) {
            throw new IllegalArgumentException("Lenght must be at least 4");
        }
        final String part1 = RandomString.of(length - 3, LOWER_CHARS).toString();
        final String part2 = RandomString.of(1, UPPER_CHARS).toString();
        final String part3 = RandomString.of(1, NUMBERS).toString();
        final String part4 = RandomString.of(1, SPECIAL).toString();

        rawPassword = Strings.shuffle(part1 + part2 + part3 + part4);
    }

    @Override
    public String toString() {
        return rawPassword;
    }
}
