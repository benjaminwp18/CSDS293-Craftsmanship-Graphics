package edu.cwru.bwp18.polygon;

import java.util.*;
import java.util.stream.IntStream;

public final class AxisMap<S extends Comparable<S>> {
    /**
     * Map of coordinates to their indices.
     * Guaranteed to be non-null and to contain no null values.
     */
    private final Map<S, Integer> index;

    /**
     * Create an AxisMap from the given map of coordinates to indices. Null
     * coordinates are illegal.
     * @param index the coordinate -> index map
     */
    private AxisMap(Map<S, Integer> index) {
        assert index != null;
        index.forEach((coord, i) -> {assert coord != null;});

        this.index = index;
    }

    /**
     * Generate an AxisMap from the given coordinates.
     * Null coordinates are illegal.
     * @param coordinates the nonnull coordinates for the new AxisMap
     * @return a new AxisMap with the given coordinates
     * @param <S> the type of a coordinate
     */
    static <S extends Comparable<S>> AxisMap<S> from(Collection<S> coordinates) {
        assert coordinates != null;
        coordinates.forEach(coord -> {assert coord != null;});

        List<S> coordList = coordinates.stream().sorted().distinct().toList();
        Map<S, Integer> index = new HashMap<>();

        IntStream.range(0, coordList.size())
                .forEach(i -> index.put(coordList.get(i), i));

        return new AxisMap<S>(index);
    }

    /**
     * Get the index of value in this AxisMap's index, or null if value is null
     * or isn't in the index.
     * @param value the value to get the index for
     * @return value's index, or null if value == null or value is not in index
     */
    Integer flatIndexOf(S value) {
        return index.get(value);
    }

    public int size() {
        return index.size();
    }

    /**
     * An Optional wrapping: the index of value in AxisMap's index, or null if
     * value is null or isn't in the index.
     * @param value the value to get the index for
     * @return an optional wrapping: value's index, or null if value == null or
     * value is not in index
     */
    public Optional<Integer> indexOf(S value) {
        return Optional.ofNullable(flatIndexOf(value));
    }
}
