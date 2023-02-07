package edu.cwru.bwp18.polygon;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class Grid implements Iterable<IndexPair> {

    /**
     * Underlying rectangle.
     * Guaranteed to have all four bounds.
     */
    private final Rectangle<Integer> rectangle;

    private Grid(Rectangle<Integer> rectangle) {
        assert rectangle != null;

        this.rectangle = rectangle;
    }

    public static Grid from(Rectangle<Integer> rectangle) {
        RectangleException.verifyNonNull(rectangle);

        return new Grid(Rectangle.copyOf(rectangle));
    }

    public Integer top() {
        return rectangle.top();
    }

    public Integer bottom() {
        return rectangle.bottom();
    }

    public Integer left() {
        return rectangle.left();
    }

    public Integer right() {
        return rectangle.right();
    }

    @Override
    public Iterator<IndexPair> iterator() {
        return new GridIterator(this);
    }

    public static class GridIterator implements Iterator<IndexPair> {

        Grid grid;
        int xCoord, yCoord;

        GridIterator(Grid grid) {
            assert grid != null;

            this.grid = grid;
            xCoord = grid.left();
            yCoord = grid.bottom();
        }

        @Override
        public boolean hasNext() {
            return xCoord < grid.right();
        }

        @Override
        public IndexPair next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            // Save our resultant pair
            IndexPair pair = new IndexPair(xCoord, yCoord);

            // Increment coordinates to be ready for the next next() call
            if (yCoord < grid.top() - 1) {
                yCoord++;
            }
            else {
                yCoord = grid.bottom();
                xCoord++;
            }

            return pair;
        }
    }
}
