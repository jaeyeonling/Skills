package com.jaeyeonling.skills;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum PairwiseComparison {

    WIN(1),
    DRAW(0),
    LOSE(-1),
    ;

    private final int multiplier;

    public static PairwiseComparison fromMultiplier(final int multiplier) {
        return Arrays.stream(values())
                .filter(pairwiseComparison -> pairwiseComparison.multiplier == multiplier)
                .findFirst()
                .orElseThrow();
    }
}
