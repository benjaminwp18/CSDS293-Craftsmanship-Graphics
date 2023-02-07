package edu.cwru.bwp18.polygon;

import java.util.*;

public final class PlaneMap<S extends Comparable<S>> {

    // Guaranteed to be non-null
    private final AxisMap<S> x, y;

    private PlaneMap(AxisMap<S> x, AxisMap<S> y) {
        assert x != null;
        assert y != null;

        this.x = x;
        this.y = y;
    }

    public static <S extends Comparable<S>> PlaneMap<S> of(Collection<S> x, Collection<S> y) {
        RectangleException.verifyNonNull(x, y);
        RectangleException.verifyNonNull(x.toArray());
        RectangleException.verifyNonNull(y.toArray());

        return new PlaneMap<S>(AxisMap.from(x), AxisMap.from(y));
    }

    public Optional<Integer> xIndexOf(S value) {
        return x.indexOf(value);
    }

    public Optional<Integer> yIndexOf(S value) {
        return y.indexOf(value);
    }

    public int xSize() {
        return x.size();
    }

    public int ySize() {
        return y.size();
    }

    /**
     * Get the index of value in the AxisMap selected by horizontal, or null if
     * value is null or isn't in that map.
     * @param value the value to find
     * @param horizontal if true, search in horizontal coords, else search in
     *                   vertical coords
     * @return value's index, or null if value == null or value is not in the
     *          selected AxisMap
     */
    Integer indexOf(S value, boolean horizontal) {
        return horizontal ? x.flatIndexOf(value) : y.flatIndexOf(value);
    }

    public static <S extends Comparable<S>> PlaneMap<S> from(Set<Rectangle<S>> rectangles) {
        RectangleException.verifyNonNull(rectangles);           // Set != null
        RectangleException.verifyNonNull(rectangles.toArray()); // Elements != null

        Collection<S> x = new ArrayList<S>();
        Collection<S> y = new ArrayList<S>();

        rectangles.forEach(rect -> {
            rect.getBorders(Direction.HORIZONTAL_BOUNDS)
                    .forEach((dir, coord) -> x.add(coord));
            rect.getBorders(Direction.VERTICAL_BOUNDS)
                    .forEach((dir, coord) -> y.add(coord));
        });

        return PlaneMap.of(x, y);
    }
}
