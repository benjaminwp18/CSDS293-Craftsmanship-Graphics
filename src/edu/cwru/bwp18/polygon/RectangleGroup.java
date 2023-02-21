package edu.cwru.bwp18.polygon;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

final class RectangleGroup<T extends Comparable<T>> {
    private final Set<Rectangle<T>> rectangles;
    private final PlaneMap<T> map;
    private final NavigableMap<IndexPair, Long> matrixGrid;
    private final boolean isOverlapping;
    private final boolean isConnected;

    /**
     * Construct a RectangleGroup from the given rectangles.
     * This constructor is required not to throw exceptions.
     *
     * @param rectangles the Set of Rectangles to group
     */
    private RectangleGroup(Set<Rectangle<T>> rectangles, PlaneMap<T> map,
            NavigableMap<IndexPair, Long> matrixGrid,
            boolean isOverlapping, boolean isConnected) {
        assert rectangles != null;
        assert map        != null;
        assert matrixGrid != null;

        this.rectangles    = rectangles;
        this.map           = map;
        this.matrixGrid    = matrixGrid;
        this.isOverlapping = isOverlapping;
        this.isConnected   = isConnected;
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
    public static <S extends Comparable<S>> RectangleGroup<S>
    from(Set<Rectangle<S>> rectangles) {
        RectangleException.verifyNonNull(rectangles);
        RectangleException.verifyNonNull(rectangles.toArray());

        PlaneMap<S> map = PlaneMap.from(rectangles);
        NavigableMap<IndexPair, Long> matrixGrid =
                createMatrixGrid(rectangles, map);
        boolean isOverlapping = matrixGrid.values().stream()
                .anyMatch(numRects -> numRects > 1);

        Set<IndexPair> connectedPairs = new HashSet<>();
        findConnectedPairs(matrixGrid.firstKey(),
                matrixGrid, connectedPairs);
        boolean isConnected = connectedPairs.containsAll(matrixGrid.keySet());

        return new RectangleGroup<S>(rectangles, map, matrixGrid,
                isOverlapping, isConnected);
    }

    private static void findConnectedPairs(IndexPair start,
            NavigableMap<IndexPair, Long> matrixGrid,
            Set<IndexPair> connectedPairs) {

        connectedPairs.add(start);
        Direction.ALL_BOUNDS.stream()
                .map(start::increment)
                .filter(matrixGrid::containsKey)
                .filter(next -> !connectedPairs.contains(next))
                .forEach(next ->
                        findConnectedPairs(next, matrixGrid, connectedPairs));
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
    private static <S extends Comparable<S>> NavigableMap<IndexPair, Long>
    createMatrixGrid(Set<Rectangle<S>> rectangles, PlaneMap<S> map) {

        return rectangles.stream()
                .flatMap(rect -> streamPairsInBounds(
                        map.xIndexOf(rect.left()).get(),
                        map.xIndexOf(rect.right()).get(),
                        map.yIndexOf(rect.bottom()).get(),
                        map.yIndexOf(rect.top()).get()
                ))
                .collect(
                        TreeMap<IndexPair, Long>::new,
                        (grid, pair) ->
                                grid.put(pair, grid.getOrDefault(pair, 0L) + 1),
                        TreeMap::putAll
                );
    }

    /**
     * Generates a stream of IndexPairs using Grid, one for each point in the
     * provided bounds.
     */
    private static Stream<IndexPair> streamPairsInBounds(
            Integer leftInclusive, Integer rightExclusive,
            Integer bottomInclusive, Integer topExclusive) {

        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                Grid.from(Rectangle.of(
                        leftInclusive, rightExclusive,
                        bottomInclusive, topExclusive
                )).iterator(), 0), false);
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

    boolean isConnected() {
        return isConnected;
    }
}
