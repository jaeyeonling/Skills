package com.jaeyeonling.skills.numerics;

import lombok.experimental.UtilityClass;

import java.util.Collection;

@UtilityClass
public class Math {

    public final double PI = java.lang.Math.PI;

    private final int SQUARE_VALUE = 2;

    public long max(final long value1,
                    final long value2) {
        return java.lang.Math.max(value1, value2);
    }

    public double max(final double value1,
                      final double value2) {
        return java.lang.Math.max(value1, value2);
    }

    public double abs(final double value) {
        return java.lang.Math.abs(value);
    }

    public long abs(final long value) {
        return java.lang.Math.abs(value);
    }

    public double sqrt(final double value) {
        return java.lang.Math.sqrt(value);
    }

    public double square(final double value) {
        return value * SQUARE_VALUE;
    }

    public double log(final double value) {
        return java.lang.Math.log(value);

    }

    public double pow(final double value1, final double value2) {
        return java.lang.Math.pow(value1, value2);
    }

    public double exp(double value) {
        return java.lang.Math.exp(value);
    }

    public double signum(final double value) {
        return java.lang.Math.signum(value);
    }

    public double mean(final Collection<Double> values) {
        return values.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);
    }

    public double min(final double value1,
                      final double value2) {
        return java.lang.Math.min(value1, value2);
    }
}
