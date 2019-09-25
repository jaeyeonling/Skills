package com.jaeyeonling.skills;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface Team {

    public static Collection<Team> concat(final Team... teams) {
        return Arrays.asList(teams);
    }

    long size();
    Set<Map.Entry<Player, Rating>> entrySet();
    Collection<Rating> values();
    Set<Player> keySet();
    Rating get(final Player player);
}
