package com.jaeyeonling.skills;

import com.jaeyeonling.skills.numerics.Range;
import lombok.NonNull;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

public abstract class SkillCalculator {

    private final EnumSet<CalculateOptions> supportCalculateOptions;
    private final Range<Player> playersPerTeamAllow;
    private final Range<Team> totalTeamsAllow;

    protected SkillCalculator(final EnumSet<CalculateOptions> supportCalculateOptions,
                              final Range<Player> playersPerTeamAllow,
                              final Range<Team> totalTeamsAllow) {
        this.supportCalculateOptions = supportCalculateOptions;
        this.playersPerTeamAllow = playersPerTeamAllow;
        this.totalTeamsAllow = totalTeamsAllow;
    }

    public abstract Map<Player, Rating> calculateNewRatings(final GameInfo gameInfo, final Collection<Team> teams,
                                                            final long... ranks);
    public abstract double calculateMatchQuality(final GameInfo gameInfo, final Collection<Team> teams);

    public boolean isSupport(final CalculateOptions calculateOptions) {
        return supportCalculateOptions.contains(calculateOptions);
    }

    protected void validateTeamCountAndPlayersCountPerTeam(@NonNull final Collection<Team> teams) {
        final var isNoneMatch = teams.stream()
                .map(Team::size)
                .noneMatch(playersPerTeamAllow::isInRange);

        if (isNoneMatch) {
            throw new IllegalArgumentException();
        }
        if (!totalTeamsAllow.isInRange(teams.size())) {
            throw new IllegalArgumentException();
        }
    }
}
