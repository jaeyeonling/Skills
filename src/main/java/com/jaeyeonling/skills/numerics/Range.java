package com.jaeyeonling.skills.numerics;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public final class Range<T> {

    private final long min;
    private final long max;

    private Range(final long min,
                  final long max) {
        if (min > max) {
            throw new IllegalArgumentException();
        }

        this.min = min;
        this.max = max;
    }

    public static <T> Range<T> inclusive(final long min,
                                         final long max) {
        return new Range<>(min, max);
    }

    public static <T> Range<T> exactly(final long value) {
        return new Range<>(value, value);
    }

    public static <T> Range<T> atLeast(final long min) {
        return new Range<>(min, Long.MAX_VALUE);
    }

    public boolean isInRange(final long value) {
        return min <= value && value <= max;
    }
}
