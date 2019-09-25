package com.jaeyeonling.skills.elo;

import com.jaeyeonling.skills.GameInfo;
import com.jaeyeonling.skills.PairwiseComparison;
import com.jaeyeonling.skills.Player;
import com.jaeyeonling.skills.RankSorter;
import com.jaeyeonling.skills.Rating;
import com.jaeyeonling.skills.Team;
import com.jaeyeonling.skills.CalculateOptions;
import com.jaeyeonling.skills.SkillCalculator;
import com.jaeyeonling.skills.numerics.Math;
import com.jaeyeonling.skills.numerics.Range;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public abstract class TwoPlayerEloCalculator extends SkillCalculator {

    private final KFactor kFactor;

    protected TwoPlayerEloCalculator(final KFactor kFactor) {
        super(EnumSet.noneOf(CalculateOptions.class), Range.exactly(2), Range.exactly(1));

        this.kFactor = kFactor;
    }

    protected abstract double getPlayerWinProbability(GameInfo gameInfo, double playerRating, double opponentRating);

    @Override
    public Map<Player, Rating> calculateNewRatings(final GameInfo gameInfo,
                                                   final Collection<Team> teams,
                                                   final long... ranks) {
        validateTeamCountAndPlayersCountPerTeam(teams);

        final var sortedTeam = RankSorter.sort(teams, ranks);

        final var players = new ArrayList<Player>(2);
        for (final var team : sortedTeam) {
            final var teamPlayers = team.keySet().toArray(new Player[1]);
            players.add(teamPlayers[0]);
        }

        final var isDraw = ranks[0] == ranks[1];

        final var player1 = players.get(0);
        final var player1Rating = sortedTeam.get(0).get(player1).getMean();
        final var player1Comparison = isDraw ? PairwiseComparison.DRAW : PairwiseComparison.WIN;

        final var player2 = players.get(1);
        final var player2Rating = sortedTeam.get(1).get(player2).getMean();
        final var player2Comparison = isDraw ? PairwiseComparison.DRAW : PairwiseComparison.LOSE;

        final var newPlayer1Rating = calculateNewRating(gameInfo, player1Rating, player2Rating, player1Comparison);
        final var newPlayer2Rating = calculateNewRating(gameInfo, player2Rating, player1Rating, player2Comparison);

        final var result = new HashMap<Player, Rating>();
        result.put(player1, newPlayer1Rating);
        result.put(player2, newPlayer2Rating);

        return result;
    }

    @Override
    public double calculateMatchQuality(final GameInfo gameInfo,
                                        final Collection<Team> teams) {
        validateTeamCountAndPlayersCountPerTeam(teams);

        final var players = new ArrayList<Player>(2);
        for (final var team : teams) {
            final var teamPlayers = team.keySet().toArray(new Player[0]);
            players.add(teamPlayers[0]);
        }

        final var iterator = teams.iterator();
        final var player1Rating = iterator.next().get(players.get(0)).getMean();
        final var player2Rating = iterator.next().get(players.get(1)).getMean();

        final var playerWinProbability = getPlayerWinProbability(gameInfo, player1Rating, player2Rating);
        final var deltaFrom50Percent = Math.abs( playerWinProbability - .5);

        return (.5 - deltaFrom50Percent) / .5;
    }

    private EloRating calculateNewRating(final GameInfo gameInfo,
                                         final double selfRating,
                                         final double opponentRating,
                                         final PairwiseComparison selfToOpponentComparison) {
        final var expectedProbability = getPlayerWinProbability(gameInfo, selfRating, opponentRating);
        final var actualProbability = getScoreFromComparison(selfToOpponentComparison);
        final var ratingChange = kFactor.getValue() * (actualProbability - expectedProbability);
        final var newRating = selfRating + ratingChange;

        return new EloRating(newRating);
    }

    private double getScoreFromComparison(final PairwiseComparison comparison) {
        switch (comparison) {
            case WIN:
                return 1;
            case DRAW:
                return .5;
            case LOSE:
                return 0;
            default:
                throw new IllegalArgumentException();
        }
    }
}
