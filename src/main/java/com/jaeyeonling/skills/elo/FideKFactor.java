package com.jaeyeonling.skills.elo;

public class FideKFactor extends KFactor {

    public FideKFactor() {
        super(-1.);
    }

    public double getValueForRating(final double rating) {
        return rating < 2400 ? 15 : 10;
    }
}
