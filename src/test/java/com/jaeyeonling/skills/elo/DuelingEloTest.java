package com.jaeyeonling.skills.elo;

import com.jaeyeonling.skills.DefaultPlayer;
import com.jaeyeonling.skills.DefaultTeam;
import com.jaeyeonling.skills.GameInfo;
import com.jaeyeonling.skills.Rating;
import com.jaeyeonling.skills.Team;
import com.jaeyeonling.skills.SkillCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DuelingEloTest {

    private final static double ErrorTolerance = 0.1;

    private SkillCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new DuelingEloCalculator(new GaussianEloCalculator());
    }

    @Test
    void twoOnTwoDuellingTest() {
        final var gameInfo = GameInfo.getDefault();

        final var player1 = new DefaultPlayer<Integer>(1);
        final var player2 = new DefaultPlayer<Integer>(2);
        final var player3 = new DefaultPlayer<Integer>(3);
        final var player4 = new DefaultPlayer<Integer>(4);

        final var team1 = new DefaultTeam()
                .addPlayer(player1, gameInfo.getRating())
                .addPlayer(player2, gameInfo.getRating());
        final var team2 = new DefaultTeam()
                .addPlayer(player3, gameInfo.getRating())
                .addPlayer(player4, gameInfo.getRating());

        final var teams = Team.concat(team2, team1);
        final var newRatingsWinLose = calculator.calculateNewRatings(gameInfo, teams, 2, 1);

        assertRating(37, newRatingsWinLose.get(player1));
        assertRating(37, newRatingsWinLose.get(player2));
        assertRating(13, newRatingsWinLose.get(player3));
        assertRating(13, newRatingsWinLose.get(player4));

        double quality = calculator.calculateMatchQuality(gameInfo, teams);
        assertEquals(1.0, quality, 0.001);
    }

    private void assertRating(final double expected,
                              final Rating actual) {
        assertEquals(expected, actual.getMean(), ErrorTolerance);
    }
}
