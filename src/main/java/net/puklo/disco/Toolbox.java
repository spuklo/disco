package net.puklo.disco;

import static java.util.Objects.requireNonNull;

public class Toolbox {
    public static String notEmpty(final String maybeNullOrEmpty, final String errorMessage) {
        if (requireNonNull(maybeNullOrEmpty, errorMessage).isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
        return maybeNullOrEmpty;
    }

}
