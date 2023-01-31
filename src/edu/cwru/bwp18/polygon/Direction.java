package edu.cwru.bwp18.polygon;

import java.util.Set;

/**
 * Direction enumeration for use in defining borders.<br>
 * Includes TOP, BOTTOM, LEFT, RIGHT, HORIZONTAL_BOUNDS (set of LEFT, RIGHT),
 * and VERTICAL_BOUNDS (set of BOTTOM, TOP).
 */
enum Direction {
    TOP     (false, true),
    BOTTOM  (false, false),
    LEFT    (true,  false),
    RIGHT   (true,  true);

    static final Set<Direction> HORIZONTAL_BOUNDS = Set.of(LEFT, RIGHT);
    static final Set<Direction> VERTICAL_BOUNDS   = Set.of(BOTTOM, TOP);

    private final boolean horizontal, increment;

    /**
     * Create a Direction with the given horizontal and increment values.
     * @param horizontal whether this direction is on the horizontal axis
     * @param increment whether this direction is considered positive
     */
    Direction(boolean horizontal, boolean increment) {
        this.horizontal = horizontal;
        this.increment  = increment;
    }
}
