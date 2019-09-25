package com.jaeyeonling.skills.elo;

import com.jaeyeonling.skills.DefaultPlayer;
import com.jaeyeonling.skills.DefaultTeam;
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

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class DuelingEloCalculator extends SkillCalculator {

    private final TwoPlayerEloCalculator twoPlayerEloCalculator;

    public DuelingEloCalculator(final TwoPlayerEloCalculator twoPlayerEloCalculator) {
        super(EnumSet.noneOf(CalculateOptions.class), Range.atLeast(2), Range.atLeast(1));
        this.twoPlayerEloCalculator = twoPlayerEloCalculator;
    }

    @Override
    public Map<Player, Rating> calculateNewRatings(final GameInfo gameInfo,
                                                   final Collection<Team> teams,
                                                   final long... ranks) {
        validateTeamCountAndPlayersCountPerTeam(teams);

        final var sortedTeams = RankSorter.sort(teams, ranks);
        final var newTeams = sortedTeams.toArray(new Team[]{});

        final var deltas = new HashMap<Player, Map<Player, Double>>();
        for (var currentTeamIndex = 0; currentTeamIndex < newTeams.length; currentTeamIndex++) {
            for (var otherTeamIndex = 0; otherTeamIndex < newTeams.length; otherTeamIndex++) {
                if (currentTeamIndex == otherTeamIndex) {
                    continue;
                }

                final var currentTeam = newTeams[currentTeamIndex];
                final var otherTeam = newTeams[otherTeamIndex];

                final var otherTeamRank = ranks[otherTeamIndex];
                final var currentTeamRank = ranks[currentTeamIndex];
                final var signumValue = Math.signum(otherTeamRank - currentTeamIndex);

                final var comparison = PairwiseComparison.fromMultiplier((int) signumValue);
                for (final var currentTeamPlayerRatingPair : currentTeam.entrySet()) {
                    for (final var otherTeamPlayerRatingPair : otherTeam.entrySet()) {
                        updateDuels(gameInfo, deltas,
                                currentTeamPlayerRatingPair.getKey(), currentTeamPlayerRatingPair.getValue(),
                                otherTeamPlayerRatingPair.getKey(), otherTeamPlayerRatingPair.getValue(),
                                comparison);
                    }
                }
            }
        }

        final var result = new HashMap<Player, Rating>();
        for (final var currentTeam : newTeams) {
            for (final var currentTeamPlayerPair : currentTeam.entrySet()) {
                final var player = currentTeamPlayerPair.getKey();
                final var currentRating = currentTeamPlayerPair.getValue();
                final var currentPlayerAverageDuellingDelta = Math.mean(deltas.get(player).values());

                result.put(player, new EloRating(currentRating.getMean() + currentPlayerAverageDuellingDelta));
            }
        }

        return result;
    }

    private void updateDuels(final GameInfo gameInfo,
                             final HashMap<Player, Map<Player, Double>> duels,
                             final Player player1,
                             final Rating player1Rating,
                             final Player player2,
                             final Rating player2Rating,
                             final PairwiseComparison weakToStrongComparison) {
        final var team1 = new DefaultTeam(player1, player1Rating);
        final var team2 = new DefaultTeam(player2, player2Rating);
        final var teams = Team.concat(team1, team2);
        final var ranks = getDefaultRanks(weakToStrongComparison);

        final var duelOutcomes = twoPlayerEloCalculator.calculateNewRatings(gameInfo, teams, ranks);

        updateDuelInfo(duels, player1, player1Rating, duelOutcomes.get(player1), player2);
        updateDuelInfo(duels, player2, player2Rating, duelOutcomes.get(player2), player1);
    }

    @Override
    public double calculateMatchQuality(final GameInfo gameInfo,
                                        final Collection<Team> teams) {
        var minQuality = 1.;
        final var newTeams = teams.toArray(new Team[]{});

        for (var currentTeamIndex = 0; currentTeamIndex < newTeams.length; currentTeamIndex++) {
            final var currentMeanMean = Rating.calcMeanMean(newTeams[currentTeamIndex].values());
            final var currentTeamAverageRating = new EloRating(currentMeanMean);
            final var currentPlayer = new DefaultPlayer<Integer>(currentTeamIndex);
            final var currentTeam = new DefaultTeam(currentPlayer, currentTeamAverageRating);

            for (var otherTeamIndex = currentTeamIndex + 1; otherTeamIndex < newTeams.length; otherTeamIndex++) {
                final var otherMeanMean = Rating.calcMeanMean(newTeams[otherTeamIndex].values());
                final var otherTeamAverageRating = new EloRating(otherMeanMean);
                final var otherPlayer = new DefaultPlayer<Integer>(otherTeamIndex);
                final var otherTeam = new DefaultTeam(otherPlayer, otherTeamAverageRating);

                final var quality = twoPlayerEloCalculator.calculateMatchQuality(gameInfo,
                        Team.concat(currentTeam, otherTeam));
                minQuality = Math.min(minQuality, quality);
            }
        }

        return minQuality;
    }

    private void updateDuelInfo(final HashMap<Player, Map<Player, Double>> duels,
                                final Player self,
                                final Rating selfBeforeRating,
                                final Rating selfAfterRating,
                                final Player opponent) {
        if (!duels.containsKey(self)) {
            final var newSelfToOpponentDuelDeltas = new HashMap<Player, Double>();
            duels.put(self, newSelfToOpponentDuelDeltas);
        }
        final var selfToOpponentDuelDeltas = duels.get(self);

        selfToOpponentDuelDeltas.put(opponent, selfAfterRating.getMean() - selfBeforeRating.getMean());
    }

    private long[] getDefaultRanks(final PairwiseComparison pairwiseComparison) {
        if (pairwiseComparison == PairwiseComparison.WIN) {
            return new long[]{1, 2};
        }
        if (pairwiseComparison == PairwiseComparison.LOSE) {
            return new long[]{2, 1};
        }

        return new long[]{1, 1};
    }
}
