package com.jaeyeonling.skills.elo;

import com.jaeyeonling.skills.GameInfo;
import com.jaeyeonling.skills.numerics.GaussianDistribution;
import com.jaeyeonling.skills.numerics.Math;

public class GaussianEloCalculator extends TwoPlayerEloCalculator {

    private static final KFactor STABLE_K_FACTOR = new KFactor(24);

    public GaussianEloCalculator() {
        super(STABLE_K_FACTOR);
    }

    @Override
    protected double getPlayerWinProbability(final GameInfo gameInfo,
                                             final double playerRating,
                                             final double opponentRating) {
        final var ratingDifference = playerRating - opponentRating;

        return GaussianDistribution.cumulativeTo(ratingDifference / Math.sqrt(2) * gameInfo.getBeta());
    }
}
