package com.jaeyeonling.skills;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class DefaultPlayer<T> implements Player, SupportPartialPlay, SupportPartialUpdate {

    private static final double MIN = .0;
    private static final double MAX = 1.;

    private static final double DEFAULT_PARTIAL_PLAY_PERCENTAGE = MAX;
    private static final double DEFAULT_PARTIAL_UPDATE_PERCENTAGE = MAX;

    private final T id;
    private final double partialPlayPercentage;
    private final double partialUpdatePercentage;

    public DefaultPlayer(final T id) {
        this(id, DEFAULT_PARTIAL_PLAY_PERCENTAGE);
    }

    public DefaultPlayer(final T id,
                         final double partialPlayPercentage) {
        this(id, partialPlayPercentage, DEFAULT_PARTIAL_PLAY_PERCENTAGE);
    }

    public DefaultPlayer(final T id,
                         final double partialPlayPercentage,
                         final double partialUpdatePercentage) {
        if (partialPlayPercentage < MIN || partialPlayPercentage > MAX) {
            throw new IndexOutOfBoundsException();
        }
        if (partialUpdatePercentage < MIN || partialUpdatePercentage > MAX) {
            throw new IndexOutOfBoundsException();
        }

        this.id = id;
        this.partialPlayPercentage = partialPlayPercentage;
        this.partialUpdatePercentage = partialUpdatePercentage;
    }

    @Override
    public double getPartialPlayPercentage() {
        return 0;
    }

    @Override
    public double getPartialUpdatePercentage() {
        return 0;
    }
}
