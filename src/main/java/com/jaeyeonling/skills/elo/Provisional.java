package com.jaeyeonling.skills.elo;

public class Provisional extends FideKFactor {

    @Override
    public double getValueForRating(final double ignore) {
        return 25;
    }
}
