package com.jaeyeonling.skills;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RankSorterTest {

    @Test
    void sortAlreadySorted() {
        final var people = List.of("One", "Two", "Three");
        final var ranks = new long[]{1, 2, 3};

        final var sortedRanks = RankSorter.sort(people, ranks);

        final var expectedRanks = new long[]{1, 2, 3};
        final var expectedPeople = List.of("One", "Two", "Three");

        for (var i = 0; i < sortedRanks.size(); i++) {
            assertEquals(ranks[i], expectedRanks[i]);
        }
        assertEquals(sortedRanks, expectedPeople);
    }

    @Test
    void sortUnsortedTest() {
        final var people = List.of("Five", "Two1", "Two2", "One", "Four");
        final var ranks = new long[]{5, 2, 2, 1, 4};

        final var sortedRanks = RankSorter.sort(people, ranks);

        final var expectedRanks = new long[]{1, 2, 2, 4, 5};
        final var expectedPeople = List.of("One", "Two1", "Two2", "Four", "Five");

        for (var i = 0; i < sortedRanks.size(); i++) {
            assertEquals(ranks[i], expectedRanks[i]);
        }
        assertEquals(sortedRanks, expectedPeople);
    }
}
