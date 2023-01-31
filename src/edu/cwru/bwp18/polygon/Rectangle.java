package edu.cwru.bwp18.polygon;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * A rectangle with four comparable borders that have generic length.
 * @param <T> the comparable type of the border lengths
 */
public final class Rectangle<T extends Comparable<T>> {
    /**
     * EnumMap of edge Directions to position values (generic T).
     * Guaranteed to be non-null and have keys for all four
     * directions with non-null values.
     */
    private final EnumMap<Direction, T> borders;

    private Rectangle(EnumMap<Direction, T> borders) {
        assert borders                       != null;
        assert borders.get(Direction.TOP)    != null;
        assert borders.get(Direction.BOTTOM) != null;
        assert borders.get(Direction.LEFT)   != null;
        assert borders.get(Direction.RIGHT)  != null;

        this.borders = borders;
    }

    /**
     * Factory method to create rectangles with the provided bounds.
     * @param top generic upper bound
     * @param bottom generic lower bound (must be less than top)
     * @param left generic left bound (must be less than right)
     * @param right generic right bound
     * @return a rectangle with the provided bounds
     * @param <S> the Comparable type of the bounds
     * @throws IllegalArgumentException containing a RectangleException as its
     *          cause if the bottom/left bounds are greater than the top/right
     *          bounds (respectively) or any bound is null
     */
    public static <S extends Comparable<S>> Rectangle<S> of(S top, S bottom, S left, S right) {
        RectangleException.verifyNonNull(top, bottom, left, right);
        RectangleException.verifyBounds(bottom, top);
        RectangleException.verifyBounds(left, right);

        return new Rectangle<S>(new EnumMap<Direction, S>(Map.of(
                Direction.TOP,    top,
                Direction.BOTTOM, bottom,
                Direction.LEFT,   left,
                Direction.RIGHT,  right
        )));
    }

    /**
     * Create a copy of the provided rectangle with the same bounds. Individual
     * bounds are not cloned (shallow copy).
     * @param rectangle the rectangle to copy
     * @return a rectangle with the same bounds as the provided rectangle
     * @param <S> the type of the bounds
     * @throws IllegalArgumentException containing a RectangleException as its
     *          cause if the rectangle null
     */
    public static <S extends Comparable<S>> Rectangle<S> copyOf(Rectangle<S> rectangle) {
        RectangleException.verifyNonNull(rectangle);

        return new Rectangle<S>(rectangle.getBorders(Set.of(
                Direction.TOP, Direction.BOTTOM, Direction.LEFT, Direction.RIGHT)));
    }

    /**
     * Get the border corresponding with the given direction.<br>
     * Asserts that the given direction is not null.
     * @param direction which border to get
     * @return the border corresponding with the given direction
     */
    T getBorder(Direction direction) {
        assert direction != null;

        return borders.get(direction);
    }

    /**
     * Get a map of the provided directions to their corresponding borders.
     * @param directions a Collection of directions to get borders from
     * @return an EnumMap from Directions to border lengths
     */
    EnumMap<Direction, T> getBorders(Collection<Direction> directions) {
        assert directions != null;

        EnumMap<Direction, T> map = new EnumMap<Direction, T>(Direction.class);
        directions.forEach(dir -> map.put(dir, borders.get(dir)));
        return map;
    }

    /**
     * Get the top bound of this rectangle.
     * @return the generic top bound of this rectangle
     * @throws RectangleException if the bound is null
     */
    public T top() throws RectangleException {
        RectangleException.verifyNonNull(borders.get(Direction.TOP));

        return borders.get(Direction.TOP);
    }

    /**
     * Get the bottom bound of this rectangle.
     * @return the generic bottom bound of this rectangle
     * @throws RectangleException if the bound is null
     */
    public T bottom() throws RectangleException {
        RectangleException.verifyNonNull(borders.get(Direction.BOTTOM));

        return borders.get(Direction.BOTTOM);
    }

    /**
     * Get the left bound of this rectangle.
     * @return the generic left bound of this rectangle
     * @throws RectangleException if the bound is null
     */
    public T left() throws RectangleException {
        RectangleException.verifyNonNull(borders.get(Direction.LEFT));

        return borders.get(Direction.LEFT);
    }

    /**
     * Get the right bound of this rectangle.
     * @return the generic right bound of this rectangle
     * @throws RectangleException if the bound is null
     */
    public T right() throws RectangleException {
        RectangleException.verifyNonNull(borders.get(Direction.RIGHT));

        return borders.get(Direction.RIGHT);
    }
}
