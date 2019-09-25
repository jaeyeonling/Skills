package com.jaeyeonling.skills;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class GameInfo {

    private static final double DEFAULT_INITIAL_MEAN = 25.;
    private static final double DEFAULT_META = DEFAULT_INITIAL_MEAN / 6.;
    private static final double DEFAULT_DRAW_PROBABILITY = 0.1;
    private static final double DEFAULT_DYNAMICS_FACTOR = DEFAULT_INITIAL_MEAN / 300.;
    private static final double DEFAULT_INITIAL_STANDARD_DEVIATION = DEFAULT_INITIAL_MEAN / 3.;

    private final double initialMean;
    private final double initialStandardDeviation;

    @Getter
    private final double beta;
    private final double dynamicsFactor;
    private final double drawProbability;

    @Builder
    private GameInfo(final double initialMean,
                     final double initialStandardDeviation,
                     final double beta,
                     final double dynamicsFactor,
                     final double drawProbability) {
        this.initialMean = initialMean;
        this.initialStandardDeviation = initialStandardDeviation;
        this.beta = beta;
        this.dynamicsFactor = dynamicsFactor;
        this.drawProbability = drawProbability;
    }

    public static GameInfo getDefault() {
        return builder().initialMean(DEFAULT_INITIAL_MEAN)
                .initialStandardDeviation(DEFAULT_INITIAL_STANDARD_DEVIATION)
                .beta(DEFAULT_META)
                .dynamicsFactor(DEFAULT_DYNAMICS_FACTOR)
                .drawProbability(DEFAULT_DRAW_PROBABILITY)
                .build();
    }

    public Rating getRating() {
        return new Rating(initialMean, initialStandardDeviation);
    }
}
