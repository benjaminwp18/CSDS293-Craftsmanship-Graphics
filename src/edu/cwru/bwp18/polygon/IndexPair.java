package edu.cwru.bwp18.polygon;

import java.util.Objects;

public record IndexPair(int xIndex, int yIndex) implements Comparable<IndexPair> {

    public IndexPair increment(Direction direction) {
        Objects.requireNonNull(direction);

        int xIndex = this.xIndex();
        int yIndex = this.yIndex();

        if (direction.isHorizontal()) {
            xIndex += direction.getIncrementValue();
        }
        else {
            yIndex += direction.getIncrementValue();
        }

        return new IndexPair(xIndex, yIndex);
    }

    @Override
    public int compareTo(IndexPair that) {
        Objects.requireNonNull(that);

        final int xDiff = this.xIndex() - that.xIndex();
        final int yDiff = this.yIndex() - that.yIndex();
        return xDiff != 0 ? xDiff : yDiff;
    }

    IndexPair copy() {
        return new IndexPair(this.xIndex(), this.yIndex());
    }
}
