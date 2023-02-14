package edu.cwru.bwp18.polygon;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class RectangleGroup<T extends Comparable<T>> {
    private final Set<Rectangle<T>> rectangles;
    private final PlaneMap<T> map;
    private final NavigableMap<IndexPair, Long> matrixGrid;
    private final boolean isOverlapping;

    /**
     * Construct a RectangleGroup from the given rectangles.
     * This constructor is required not to throw exceptions.
     *
     * @param rectangles the Set of Rectangles to group
     */
    private RectangleGroup(Set<Rectangle<T>> rectangles, PlaneMap<T> map,
            NavigableMap<IndexPair, Long> matrixGrid, boolean isOverlapping) {
        assert rectangles != null;
        assert map        != null;
        assert matrixGrid != null;

        this.rectangles    = rectangles;
        this.map           = map;
        this.matrixGrid    = matrixGrid;
        this.isOverlapping = isOverlapping;
    }

    /**
     * Generate new RectangleGroup from provided rectangles.
     *
     * @param rectangles the Set of Rectangles to group
     * @param <S>        the type of the rectangles' dimensions
     * @return a Set of Rectangles
     * @throws IllegalArgumentException if rectangles is null or contains null
     *                                  elements
     */
    static <S extends Comparable<S>> RectangleGroup<S> from(
            Set<Rectangle<S>> rectangles) {
        RectangleException.verifyNonNull(rectangles);
        RectangleException.verifyNonNull(rectangles.toArray());

        PlaneMap<S> map = PlaneMap.from(rectangles);
        NavigableMap<IndexPair, Long> matrixGrid =
                createMatrixGrid(rectangles, map);
        boolean isOverlapping = matrixGrid.values().stream()
                .anyMatch(numRects -> numRects > 1);

        return new RectangleGroup<S>(rectangles, map, matrixGrid,
                isOverlapping);
    }

    /**
     * Create matrix grid from provided rectangles & PlaneMap. Semantically
     * coupled to RectangleGroup::from, but that's OK b/c it's private.
     *
     * @param rectangles the rectangles to include in the grid
     * @param map        the map of all rectangle bounds to indices
     * @param <S>        the type of the rectangles' bounds
     * @return a matrix grid from IndexPairs to Longs representing the number
     * of overlapping rectangles at each point
     */
    private static <S extends Comparable<S>> NavigableMap<IndexPair, Long> createMatrixGrid(
            Set<Rectangle<S>> rectangles, PlaneMap<S> map) {

        // Count # appearances of every discrete point in all rectangles
        NavigableMap<IndexPair, Long> overlapsGrid = rectangles.stream()
                // Semantic coupling: we can use Optional::get without checking
                //  here b/c map must contain all rect bounds
                .flatMap(rect -> streamPairsInBounds(
                        map.yIndexOf(rect.bottom()).get(),
                        map.yIndexOf(rect.top()).get(),
                        map.xIndexOf(rect.left()).get(),
                        map.xIndexOf(rect.right()).get()
                ))
                .collect(
                        TreeMap<IndexPair, Long>::new,
                        (grid, pair) ->
                                grid.put(pair, grid.getOrDefault(pair, 0L) + 1),
                        TreeMap::putAll
                );

        // Init the grid with 0s
        NavigableMap<IndexPair, Long> matrixGrid =
                streamPairsInBounds(0, map.ySize(), 0, map.xSize())
                        .collect(
                                TreeMap<IndexPair, Long>::new,
                                (grid, pair) -> grid.put(pair, 0L),
                                TreeMap::putAll
                        );

        // Replace 0s with overlap counts where applicable
        matrixGrid.putAll(overlapsGrid);

        return matrixGrid;
    }

    /**
     * Generates a stream of IndexPairs, one for each point in the provided
     * bounds.
     */
    private static Stream<IndexPair> streamPairsInBounds(
            Integer bottomInclusive, Integer topExclusive,
            Integer leftInclusive, Integer rightExclusive) {
        Stream<Integer> xStream =
                IntStream.range(leftInclusive, rightExclusive).boxed();

        // Must use a supplier for nested streams b/c streams are single-use
        Supplier<Stream<Integer>> yStreamSupplier =
                () -> IntStream.range(bottomInclusive, topExclusive).boxed();

        return xStream.flatMap(
                x -> yStreamSupplier.get().map(y -> new IndexPair(x, y)));
    }

    public Set<Rectangle<T>> getRectangles() {
        return Collections.unmodifiableSet(rectangles);
    }

    public PlaneMap<T> getMap() {
        return map;
    }

    public boolean isOverlapping() {
        return isOverlapping;
    }

    public NavigableMap<IndexPair, Long> getMatrixGrid() {
        return Collections.unmodifiableNavigableMap(matrixGrid);
    }
}
