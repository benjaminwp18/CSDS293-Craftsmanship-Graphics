package edu.cwru.bwp18.polygon;

import java.util.HashSet;
import java.util.Set;

/**
 * An exception for the Rectangle class.
 */
public final class RectangleException extends Exception {
    /**
     * Error code enumeration for RectangleExceptions
     */
    public enum Error {
        NULL_POINTERS, INVALID_BOUNDS
    }

    static final long serialVersionUID = 293L;

    private final Error error;
    private final Set<Integer> indexes;
    private final Object lesserBound;
    private final Object greaterBound;

    RectangleException(Error error) {
        verifyNonNull(error);

        this.error        = error;
        this.indexes      = null;
        this.lesserBound  = null;
        this.greaterBound = null;
    }

    RectangleException(Set<Integer> indexes) {
        verifyNonNull(indexes);

        this.error        = Error.NULL_POINTERS;
        this.indexes      = indexes;
        this.lesserBound  = null;
        this.greaterBound = null;
    }

    RectangleException(Error error, Object lesserBound, Object greaterBound) {
        verifyNonNull(error, lesserBound, greaterBound);

        this.error        = error;
        this.indexes      = null;
        this.lesserBound  = lesserBound;
        this.greaterBound = greaterBound;
    }

    /**
     * Throw an IllegalArgumentException if lesserBound >= greaterBound.
     * @param lesserBound the bound that should be less
     * @param greaterBound the bound that should be greater
     * @param <S> the type of the bounds
     * @throws IllegalArgumentException with RectangleException as its cause if
     *          either of its bounds are null or lessBound >= greaterBound
     */
    public static <S extends Comparable<S>> void verifyBounds(S lesserBound, S greaterBound) {
        verifyNonNull(lesserBound, greaterBound);

        if (lesserBound.compareTo(greaterBound) >= 0) {
            throw new IllegalArgumentException(new RectangleException(
                    Error.INVALID_BOUNDS, lesserBound, greaterBound));
        }
    }

    /**
     * Throw an IllegalArgumentException if one of the provided objects is null.
     * @param nonNullObjects the objects that should not be null
     * @throws IllegalArgumentException with RectangleException as its cause if
     *          any of nonNullObjects is null
     */
    public static void verifyNonNull(Object... nonNullObjects) {
        // We use HashSet to make our Set mutable
        Set<Integer> nullIndexes = new HashSet<>();

        for (int i = 0; i < nonNullObjects.length; i++) {
            if (nonNullObjects[i] == null) {
                nullIndexes.add(i);
            }
        }

        if (!nullIndexes.isEmpty()) {
            throw new IllegalArgumentException(
                    new RectangleException(nullIndexes));
        }
    }

    /**
     * Get this exception's error code.
     * @return this exception's error code
     */
    Error getError() {
        return error;
    }

    /**
     * Get this exception's indexes set.
     * @return this exception's indexes set
     */
    Set<Integer> getIndexes() {
        return indexes;
    }

    /**
     * Get this exception's lesser bound.
     * @return this exception's lesser bound
     */
    Object getLesserBound() {
        return lesserBound;
    }

    /**
     * Get this exception's greater bound.
     * @return this exception's greater bound
     */
    Object getGreaterBound() {
        return greaterBound;
    }
}
