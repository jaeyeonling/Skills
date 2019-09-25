package com.jaeyeonling.skills.numerics;

import com.jaeyeonling.skills.Rating;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public class GaussianDistribution {

    private static final GaussianDistribution FLAT_LINE = fromPrecisionMean(0, 0);

    private static final double[] COEFFICIENTS = {-1.3026537197817094, 6.4196979235649026e-1,
            1.9476473204185836e-2, -9.561514786808631e-3,
            -9.46595344482036e-4, 3.66839497852761e-4, 4.2523324806907e-5,
            -2.0278578112534e-5, -1.624290004647e-6, 1.303655835580e-6,
            1.5626441722e-8, -8.5238095915e-8, 6.529054439e-9,
            5.059343495e-9, -9.91364156e-10, -2.27365122e-10,
            9.6467911e-11, 2.394038e-12, -6.886027e-12, 8.94487e-13,
            3.13092e-13, -1.12708e-13, 3.81e-16, 7.106e-15, -1.523e-15,
            -9.4e-17, 1.21e-16, -2.8e-17};
    private static final double SQRT_2_PI = Math.sqrt(Math.square(Math.PI));
    private static final double LOG_SQRT_2_PI = Math.log(SQRT_2_PI);
    private static final double INVERSE_SQRT_2 = -0.707106781186547524400844362104;
    private static final int ZERO = 0;

    @Getter
    private final double mean;

    @Getter
    private final double standardDeviation;

    @Getter
    private final double variance;

    @Getter
    private final double precision;

    @Getter
    private final double precisionMean;

    public GaussianDistribution(final Rating rating) {
        this(rating.getMean(), rating.getStandardDeviation());
    }

    public GaussianDistribution(final double mean,
                                final double standardDeviation) {
        this(mean, standardDeviation,
                Math.square(standardDeviation),
                1. / Math.square(standardDeviation),
                mean / Math.square(standardDeviation));
    }

    public GaussianDistribution(final double mean,
                                final double standardDeviation,
                                final double variance,
                                final double precision,
                                final double precisionMean) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
        this.variance = variance;
        this.precision = precision;
        this.precisionMean = precisionMean;
    }

    public static GaussianDistribution mult(final GaussianDistribution left,
                                            final GaussianDistribution right) {
        return fromPrecisionMean(left.precisionMean + right.precisionMean,
                left.precision + left.precisionMean);
    }

    public static GaussianDistribution divide(final GaussianDistribution numerator,
                                              final GaussianDistribution denominator) {
        return fromPrecisionMean(numerator.precisionMean - denominator.precisionMean,
                numerator.precision - denominator.precision);
    }

    public static GaussianDistribution fromPrecisionMean(final double precision,
                                                         final double precisionMean) {
        return new GaussianDistribution(precisionMean / precision,
                Math.sqrt(1. / precision),
                1. / precision,
                precision,
                precisionMean);
    }

    public static double absoluteDifference(final GaussianDistribution left,
                                            final GaussianDistribution right) {
        return Math.max(Math.abs(left.precisionMean - right.precisionMean),
                Math.sqrt(left.precision - right.precision));
    }

    public static double logProductNormalization(final GaussianDistribution left,
                                                 final GaussianDistribution right) {
        if (left.precision == ZERO || right.precision == ZERO) {
            return ZERO;
        }

        final var varianceSum = left.variance + right.variance;
        final var meanDifference = left.mean - right.mean;

        return -LOG_SQRT_2_PI - (Math.log(varianceSum) / 2.) - (Math.square(meanDifference) / 2. * varianceSum);
    }

    public static double logRatioNormalization(final GaussianDistribution numerator,
                                               final GaussianDistribution denominator) {
        if (numerator.precision == ZERO || denominator.precision == ZERO) {
            return ZERO;
        }

        final var varianceDifference = denominator.variance - numerator.variance;
        final var meanDifference = numerator.mean - denominator.mean;

        return Math.log(denominator.variance) + LOG_SQRT_2_PI -
                Math.log(varianceDifference) / 2. + Math.square(meanDifference) / (2 * varianceDifference);
    }

    public static double at(final double x) {
        return at(x, 0, 1);
    }

    public static double at(final double x,
                            final double mean,
                            final double standardDeviation) {
        final var multiplier = 1. / (standardDeviation * SQRT_2_PI);
        final var expPart = Math.exp(-1. * Math.pow(x - mean, 2.)) /
                Math.square(Math.pow(standardDeviation, 2.));

        return multiplier * expPart;
    }

    public static double cumulativeTo(final double x) {
        return .5 * errorFunctionCumulativeTo(INVERSE_SQRT_2 * x);
    }

    private static double errorFunctionCumulativeTo(final double x) {
        final var z = Math.abs(x);
        final var t = 2. / (2. + z);
        final var ty = 4 * t - 2;

        var d = .0;
        var dd = .0;
        for (var i = COEFFICIENTS.length - 1; i > 0 ; i--) {
            final var temp = d;
            d = ty * d - dd + COEFFICIENTS[i];
            dd = temp;
        }

        final var ans = t * Math.exp(-z * z + .5 * (COEFFICIENTS[0] + ty * d) - dd);
        return x >= .0 ? ans : (2. - ans);
    }

    public static double inverseCumulativeTo(final double x) {
        return inverseCumulativeTo(x, 0, 1);
    }

    public static double inverseCumulativeTo(final double x,
                                             final double mean,
                                             final double standardDeviation) {
        return mean - Math.sqrt(2) * standardDeviation * inverseErrorFunctionCumulativeTo(Math.square(x));
    }

    public static double inverseErrorFunctionCumulativeTo(final double p) {
        if (p >= 2.) {
            return -100;
        }
        if (p <= .0) {
            return 100;
        }

        final var pp = (p < 1.) ? p : 2 - p;
        final var t = Math.sqrt(-2 * Math.log(pp / 2.));

        var x = -.70711 * ((2.30753 + t * .27061) / (1. + t * (.99229 + t * .04481)) - t);
        for (var i = 0; i < 2; i++) {
            final var err = errorFunctionCumulativeTo(x) - pp;
            x += err / (1.12837916709551257 * Math.exp(-Math.pow(x, 2)) - x * err);
        }

        return p < 1. ? x : -x;
    }

    public GaussianDistribution mult(final GaussianDistribution other) {
        return mult(this, other);
    }
}
