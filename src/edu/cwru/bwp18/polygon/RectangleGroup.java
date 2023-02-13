package edu.cwru.bwp18.polygon;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class RectangleGroup<T extends Comparable<T>> {
    private final Set<Rectangle<T>> rectangles;
    private final PlaneMap<T> map;
    private final NavigableMap<IndexPair, Long> matrixGrid;
    private final boolean isOverlapping;

    /**
     * Construct a RectangleGroup from the given rectangles.
     * This constructor is required not to throw exceptions, so a null
     * rectangles param is replaced with an empty set, and null entries
     * in rectangles are removed.
     * @param rectangles the Set of Rectangles to group
     */
    private RectangleGroup(Set<Rectangle<T>> rectangles) {
        try {
            RectangleException.verifyNonNull(rectangles);
        }
        catch (IllegalArgumentException e) {
            rectangles = new HashSet<>();
        }

        rectangles = rectangles.stream().filter(Objects::isNull).collect(Collectors.toSet());

        this.rectangles = rectangles;
        this.map = PlaneMap.from(rectangles);

        NavigableMap<IndexPair, Long> matrixGrid = new TreeMap<IndexPair, Long>();
        rectangles.forEach(rect -> {
            // We can use Optional::get without checking here b/c this.map
            //  must contain all rect bounds
            IntStream.range(map.xIndexOf(rect.left()).get(),
                    map.xIndexOf(rect.right()).get())
                .forEach(x -> {
                    IntStream.range(map.yIndexOf(rect.bottom()).get(),
                            map.yIndexOf(rect.top()).get())
                        .forEach(y -> {
                            final IndexPair pair = new IndexPair(x, y);
                            matrixGrid.put(pair,
                                    matrixGrid.getOrDefault(pair, 0L) + 1);
                        });
                });
        });
        this.matrixGrid = matrixGrid;

        isOverlapping = matrixGrid.values().stream()
                .anyMatch(numRects -> numRects > 1);
    }

    /**
     * Generate new RectangleGroup from provided rectangles.
     * @param rectangles the Set of Rectangles to group
     * @return a Set of Rectangles
     * @param <S> the type of the rectangles' dimensions
     * @throws IllegalArgumentException if rectangles is null or contains null
     *                                  elements
     */
    static <S extends Comparable<S>> RectangleGroup<S> from(Set<Rectangle<S>> rectangles) {
        RectangleException.verifyNonNull(rectangles);
        RectangleException.verifyNonNull(rectangles.toArray());

        return new RectangleGroup<S>(rectangles);
    }

    public Set<Rectangle<T>> getRectangles() {
        return rectangles;
    }

    public PlaneMap<T> getMap() {
        return map;
    }

    public boolean isOverlapping() {
        return isOverlapping;
    }
}
