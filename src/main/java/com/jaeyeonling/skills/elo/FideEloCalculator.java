package com.jaeyeonling.skills.elo;

import com.jaeyeonling.skills.GameInfo;
import com.jaeyeonling.skills.numerics.Math;

public class FideEloCalculator extends TwoPlayerEloCalculator {

    public FideEloCalculator() {
        this(new FideKFactor());
    }

    public FideEloCalculator(final KFactor kFactor) {
        super(kFactor);
    }

    @Override
    protected double getPlayerWinProbability(final GameInfo gameInfo,
                                             final double playerRating,
                                             final double opponentRating) {
        final var ratingDifference = opponentRating - playerRating;

        return 1. / (1. + Math.pow(10., ratingDifference / Math.square(gameInfo.getBeta())));
    }
}
