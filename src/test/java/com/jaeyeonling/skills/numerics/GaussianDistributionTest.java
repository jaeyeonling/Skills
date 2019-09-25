package com.jaeyeonling.skills.numerics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GaussianDistributionTest {

    private static final double ErrorTolerance = .000001;

    @Test
    void cumulativeTo() {
        assertEquals(0.691462, GaussianDistribution.cumulativeTo(0.5), ErrorTolerance);
    }

    @Test
    void at() {
        assertEquals(0.352065, GaussianDistribution.at(0.5), ErrorTolerance);
    }

    @Test
    void multiplication() {
        final var standardNormal = new GaussianDistribution(0, 1);
        final var shiftedGaussian = new GaussianDistribution(2, 3);

        final var product = standardNormal.mult(shiftedGaussian);

        assertEquals(0.2, product.getMean(), ErrorTolerance);
        assertEquals(3.0 / Math.sqrt(10), product.getStandardDeviation(), ErrorTolerance);

        final var m4s5 = new GaussianDistribution(4, 5);
        final var m6s7 = new GaussianDistribution(6, 7);

        final var product2 = m4s5.mult(m6s7);

        final var expectedMean = (4 * Math.square(7) + 6 * Math.square(5)) /
                (Math.square(5) + Math.square(7));
        assertEquals(expectedMean, product2.getMean(), ErrorTolerance);

        final var expectedSigma = Math.sqrt(((Math.square(5) * Math.square(7)) /
                (Math.square(5) + Math.square(7))));
        assertEquals(expectedSigma, product2.getStandardDeviation(), ErrorTolerance);
    }

    @Test
    void division() {
        final var product = new GaussianDistribution(0.2, 3.0 / Math.sqrt(10));
        final var standardNormal = new GaussianDistribution(0, 1);

        final var productDividedByStandardNormal = GaussianDistribution.divide(product, standardNormal);
        assertEquals(2.0, productDividedByStandardNormal.getMean(), ErrorTolerance);
        assertEquals(3.0, productDividedByStandardNormal.getStandardDeviation(), ErrorTolerance);

        final var product2Mean = (4 * Math.square(7) + 6 * Math.square(5)) /
                (Math.square(5) + Math.square(7));
        final var standardDeviation = Math.sqrt(((Math.square(5) * Math.square(7)) /
                (Math.square(5) + Math.square(7))));
        final var product2 = new GaussianDistribution(product2Mean, standardDeviation);

        final var m4s5 = new GaussianDistribution(4, 5);
        final var product2DividedByM4S5 = GaussianDistribution.divide(product2, m4s5);
        assertEquals(6.0, product2DividedByM4S5.getMean(), ErrorTolerance);
        assertEquals(7.0, product2DividedByM4S5.getStandardDeviation(), ErrorTolerance);
    }

    @Test
    void logProductNormalization() {
        final var standardNormal = new GaussianDistribution(0, 1);
        final var lpn = GaussianDistribution.logProductNormalization(standardNormal, standardNormal);
        assertEquals(-1.2655121234846454, lpn, ErrorTolerance);

        final var m1s2 = new GaussianDistribution(1, 2);
        final var m3s4 = new GaussianDistribution(3, 4);
        final var lpn2 = GaussianDistribution.logProductNormalization(m1s2, m3s4);
        assertEquals(-2.5168046699816684, lpn2, ErrorTolerance);
    }

    @Test
    void logRatioNormalization() {
        final var m1s2 = new GaussianDistribution(1, 2);
        final var m3s4 = new GaussianDistribution(3, 4);
        final var lrn = GaussianDistribution.logRatioNormalization(m1s2, m3s4);
        assertEquals(2.6157405972171204, lrn, ErrorTolerance);
    }

    @Test
    void absoluteDifference() {
        final var standardNormal = new GaussianDistribution(0, 1);
        final var absDiff = GaussianDistribution.absoluteDifference(standardNormal, standardNormal);
        assertEquals(0.0, absDiff, ErrorTolerance);

        final var m1s2 = new GaussianDistribution(1, 2);
        final var m3s4 = new GaussianDistribution(3, 4);
        final var absDiff2 = GaussianDistribution.absoluteDifference(m1s2, m3s4);
        assertEquals(0.4330127018922193, absDiff2, ErrorTolerance);
    }
}
