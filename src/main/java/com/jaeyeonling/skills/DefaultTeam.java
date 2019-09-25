package com.jaeyeonling.skills;

import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
public class DefaultTeam implements Team {

    private final Map<Player, Rating> teams = new HashMap<>();

    public DefaultTeam(final Player player,
                       final Rating rating) {
        addPlayer(player, rating);
    }

    public DefaultTeam addPlayer(final Player player,
                                 final Rating rating) {
        teams.put(player, rating);
        return this;
    }

    @Override
    public long size() {
        return teams.size();
    }

    @Override
    public Set<Map.Entry<Player, Rating>> entrySet() {
        return teams.entrySet();
    }

    @Override
    public Collection<Rating> values() {
        return teams.values();
    }

    @Override
    public Set<Player> keySet() {
        return teams.keySet();
    }

    @Override
    public Rating get(final Player player) {
        return teams.get(player);
    }
}
