package edu.cwru.bwp18.polygon;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestAssignment2 {
    @Test
    public void testAssignment2() {

        /* Test Factory Method .of */
        boolean threw = false;
        try {
            Rectangle<Double> d = Rectangle.of(5., null, 3., 6.);
        }
        catch (IllegalArgumentException e) {
            if (e.getCause() instanceof RectangleException) {
                threw = (((RectangleException) e.getCause()).getError() == RectangleException.Error.NULL_POINTERS);
            }
            else {
                fail("Unexpected non-RectangleException cause");
            }
        }

        assertTrue(threw, "No null pointer exception thrown by null param rectangle creation");

        threw = false;
        try {
            Rectangle<Double> d = Rectangle.of(5., 3., 6., 3.);
        }
        catch (IllegalArgumentException e) {
            if (e.getCause() instanceof RectangleException re) {
                threw = (
                        re.getError() == RectangleException.Error.INVALID_BOUNDS &&
                                re.getLesserBound().equals(6.) &&
                                re.getGreaterBound().equals(3.)
                );
            }
            else {
                fail("Unexpected non-RectangleException cause");
            }
        }

        assertTrue(threw, "No invalid bounds exception thrown by invalid left/right param rectangle creation");

        Rectangle<Integer> r = null;
        try {
            r = Rectangle.of(2, 1, 3, 4);
        }
        catch (IllegalArgumentException e) {
            fail("Exception thrown by valid rectangle creation");
        }


        /* Test Factory Method .copyOf */
        Rectangle<Integer> r2 = null;
        threw = false;
        try {
            r2 = Rectangle.copyOf(null);
        }
        catch (IllegalArgumentException e) {
            if (e.getCause() instanceof RectangleException re) {
                threw = (((RectangleException) e.getCause()).getError() == RectangleException.Error.NULL_POINTERS);
            }
            else {
                fail("Unexpected non-RectangleException cause");
            }
        }

        assertTrue(threw, "No null pointer exception thrown by null param rectangle copying");

        try {
            r2 = Rectangle.copyOf(r);
        }
        catch (IllegalArgumentException e) {
            fail("Unexpected exception when copying rectangle");
        }

        /* Test public & package-private getters */
        assertEquals(r2.top(),    r.top());
        assertEquals(r2.bottom(), r.bottom());
        assertEquals(r2.left(),   r.left());
        assertEquals(r2.right(),  r.right());

        assertEquals(r2.getBorder(Direction.TOP),    r.top());
        assertEquals(r2.getBorder(Direction.BOTTOM), r.bottom());
        assertEquals(r2.getBorder(Direction.LEFT),   r.left());
        assertEquals(r2.getBorder(Direction.RIGHT),  r.right());

        EnumMap<Direction, Integer> borders = r.getBorders(Set.of(Direction.TOP, Direction.BOTTOM));
        assertEquals(borders.get(Direction.TOP).intValue(),    2);
        assertEquals(borders.get(Direction.BOTTOM).intValue(), 1);
        assertNull(borders.get(Direction.LEFT));
        assertNull(borders.get(Direction.RIGHT));
    }

}
