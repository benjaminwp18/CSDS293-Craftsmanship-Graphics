package edu.cwru.bwp18.polygon;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestAssignment4 {
    @Test
    public void testRectangleGroup() {
        HashSet<Rectangle<Integer>> setWithNulls = new HashSet<Rectangle<Integer>>();
        setWithNulls.add(Rectangle.of(0, 1, 2, 3));
        setWithNulls.add(null);

        assertThrows(IllegalArgumentException.class,
                () -> RectangleGroup.from(null));
        assertThrows(IllegalArgumentException.class,
                () -> RectangleGroup.from(setWithNulls));

        Set<Rectangle<Integer>> set = Set.of(
                Rectangle.of(4, 5, 3, 6),
                Rectangle.of(1, 4, 5, 6),
                Rectangle.of(1, 5, 1, 4),
                Rectangle.of(1, 5, 3, 4)
        );

        RectangleGroup<Integer> rectangleGroup = RectangleGroup.from(set);

        assertTrue(rectangleGroup.isOverlapping());

        Set<Rectangle<Integer>> rects = rectangleGroup.getRectangles();
        assertTrue(rects.containsAll(set));
        assertTrue(set.containsAll(rects));
        assertThrows(UnsupportedOperationException.class,
                () -> rects.add(null));

        PlaneMap<Integer> map = rectangleGroup.getMap();
        assertEquals(Optional.of(0), map.xIndexOf(1));
        assertEquals(Optional.of(1), map.xIndexOf(4));
        assertEquals(Optional.of(2), map.xIndexOf(5));
        assertEquals(Optional.of(0), map.yIndexOf(1));
        assertEquals(Optional.of(1), map.yIndexOf(3));
        assertEquals(Optional.of(2), map.yIndexOf(4));
        assertEquals(Optional.of(3), map.yIndexOf(5));
        assertEquals(Optional.of(4), map.yIndexOf(6));


        /*
        Rectangle layout:
            6 +-----+-+
              | 1   |1|
            5 +-----+ |
                    | |
            4 +-----+-+
              | 2   |3|
            3 +-----+-+
              |       |
            2 |  1    |
              |       |
            1 +-------+
            0 1 2 3 4 5

         Plane map layout:
            4 +-+-+
              |1|1|
            3 +-+ |
                | |
            2 +-+-+
              |2|3|
            1 +-+-+
              | 1 |
            0 +---+
              0 1 2

        Matrix grid values:
            3 11
            2 01
            1 23
            0 11
              01
         */

        Map<IndexPair, Long> matrixGrid = rectangleGroup.getMatrixGrid();
        assertThrows(UnsupportedOperationException.class,
                () -> matrixGrid.put(null, 0L));

        List<List<Long>> expectedGrid = List.of(
                List.of(1L, 1L),
                List.of(2L, 3L),
                List.of(0L, 1L),
                List.of(1L, 1L)
        );

        for (int r = 0; r < expectedGrid.size(); r++) {
            for (int c = 0; c < expectedGrid.get(r).size(); c++) {
                // We don't put 0s in our results, as per the assignment
                if (expectedGrid.get(r).get(c) == 0) continue;

                assertEquals(expectedGrid.get(r).get(c),
                        matrixGrid.get(new IndexPair(c, r)));
            }
        }
    }
}
