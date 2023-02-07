package edu.cwru.bwp18.polygon;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestAssignment3 {
    @Test
    public void testPlaneMap() {
        Collection<Integer> collectionNull = new ArrayList<>();
        collectionNull.add(null);
        Collection<Integer> collectionA = List.of(1, 2, 3);
        Collection<Integer> collectionB = List.of(5, 6, 7, 8);

        assertThrows(IllegalArgumentException.class,
                () -> PlaneMap.of(null ,null));
        assertThrows(IllegalArgumentException.class,
                () -> PlaneMap.of(collectionNull ,collectionA));
        assertThrows(IllegalArgumentException.class,
                () -> PlaneMap.of(collectionA, collectionNull));

        PlaneMap<Integer> map = PlaneMap.of(collectionA, collectionB);

        assertEquals(3, map.xSize());
        assertEquals(4, map.ySize());

        assertEquals(Optional.empty(), map.xIndexOf(null));
        assertEquals(Optional.empty(), map.xIndexOf(-56));
        assertEquals(Optional.empty(), map.yIndexOf(null));
        assertEquals(Optional.empty(), map.yIndexOf(400));

        assertEquals(Optional.of(1), map.xIndexOf(2));
        assertEquals(Optional.of(2), map.yIndexOf(7));


        Set<Rectangle<Integer>> setNull = new HashSet<Rectangle<Integer>>();
        setNull.add(null);
        Set<Rectangle<Integer>> setRect =
                Set.of(Rectangle.of(5, 2, 0, 1));

        assertThrows(IllegalArgumentException.class,
                () -> PlaneMap.from(null));
        assertThrows(IllegalArgumentException.class,
                () -> PlaneMap.from(setNull));

        map = PlaneMap.from(setRect);
        assertNotNull(map.indexOf(5, false));
        assertNotNull(map.indexOf(2, false));
        assertNotNull(map.indexOf(0, true));
        assertNotNull(map.indexOf(1, true));
        assertNull(map.indexOf(7, true));
    }

    @Test
    public void testIndexPair() {
        IndexPair og = new IndexPair(1, 2);
        IndexPair right = og.increment(Direction.RIGHT);
        IndexPair bottom = og.increment(Direction.BOTTOM);
        IndexPair copy = og.copy();

        assertEquals(-1, og.compareTo(right));
        assertEquals(1, og.compareTo(bottom));
        assertEquals(0, og.compareTo(copy));
    }

    @Test
    public void testGrid() {
        assertThrows(IllegalArgumentException.class,
                () -> Grid.from(null));

        Grid g = Grid.from(Rectangle.of(3, 1, 2, 4));
        assertEquals(3, g.top());
        assertEquals(1, g.bottom());
        assertEquals(2, g.left());
        assertEquals(4, g.right());

        ArrayList<IndexPair> pairs = new ArrayList<>();
        for (IndexPair pair : g) {
            pairs.add(pair);
        }

        assertEquals(List.of(
                new IndexPair(2, 1),
                new IndexPair(2, 2),

                new IndexPair(3, 1),
                new IndexPair(3, 2)
        ), pairs);
    }
}
