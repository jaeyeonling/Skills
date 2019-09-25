package com.jaeyeonling.skills;

import com.jaeyeonling.skills.numerics.GaussianDistribution;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collection;

@ToString
@EqualsAndHashCode
public class Rating {

    private static final int DEFAULT_CONSERVATIVE_STANDARD_DEVIATION_MULTIPLIER = 3;

    private final double conservativeStandardDeviationMultiplier;

    @Getter
    private final double mean;

    @Getter
    private final double standardDeviation;
    private final double conservativeRating;

    public Rating(final double mean,
                  final double standardDeviation) {
        this(mean, standardDeviation, DEFAULT_CONSERVATIVE_STANDARD_DEVIATION_MULTIPLIER);
    }

    public Rating(final double mean,
                  final double standardDeviation,
                  final double conservativeStandardDeviationMultiplier) {

        this.mean = mean;
        this.standardDeviation = standardDeviation;
        this.conservativeStandardDeviationMultiplier = conservativeStandardDeviationMultiplier;
        this.conservativeRating = mean - conservativeStandardDeviationMultiplier * standardDeviation;
    }

    public static Rating partialUpdate(final Rating prior,
                                       final Rating fullPosterior,
                                       final double updatePercentage) {
        final var priorGaussian = new GaussianDistribution(prior);
        final var posteriorGaussian = new GaussianDistribution(fullPosterior);

        final var precisionDifference = posteriorGaussian.getPrecision() - priorGaussian.getPrecision();
        final var partialPrecisionDifference = updatePercentage * precisionDifference;
        final var precisionMean = priorGaussian.getPrecision() + partialPrecisionDifference;

        final var precisionMeanDifference = posteriorGaussian.getPrecisionMean() - priorGaussian.getPrecisionMean();
        final var partialPrecisionMeanDifference = updatePercentage * precisionMeanDifference;
        final var precision = priorGaussian.getPrecisionMean() + partialPrecisionMeanDifference;

        final var partialPosteriorGaussian = GaussianDistribution.fromPrecisionMean(precision, precisionMean);

        return new Rating(partialPosteriorGaussian.getMean(), partialPosteriorGaussian.getStandardDeviation(),
                prior.conservativeStandardDeviationMultiplier);
    }

    public static double calcMeanMean(final Collection <Rating> ratings) {
        return ratings.stream()
                .mapToDouble(rating -> rating.mean)
                .average()
                .orElse(0);
    }
}
