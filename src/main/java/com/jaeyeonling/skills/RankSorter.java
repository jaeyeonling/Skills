package com.jaeyeonling.skills;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class RankSorter {

    public <T> List<T> sort(@NonNull final Collection<T> items,
                            @NonNull final long... ranks) {
        var lastObservedRank = 0L;
        var needToSort = false;

        for (final var currentRank : ranks) {
            if (currentRank < lastObservedRank) {
                needToSort = true;
                break;
            }

            lastObservedRank = currentRank;
        }

        final List<T> newItems = new ArrayList<>(items);
        if (!needToSort) {
            return newItems;
        }

        final Map<T, Long> itemToRank = new HashMap<>(items.size());
        for (var i = 0; i < newItems.size(); i++) {
            itemToRank.put(newItems.get(i), ranks[i]);
        }

        newItems.sort(Comparator.comparing(itemToRank::get));
        Arrays.sort(ranks);

        return newItems;
    }
}
