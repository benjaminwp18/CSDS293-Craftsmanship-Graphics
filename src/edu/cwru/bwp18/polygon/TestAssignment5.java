package edu.cwru.bwp18.polygon;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAssignment5 {
    @Test
    public void testRectangleGroup() {
        Set<Rectangle<String>> discreteSet = Set.of(
                Rectangle.of("0", "3", "0", "3"),
                Rectangle.of("5", "6", "5", "6")
        );

        Set<Rectangle<String>> cornerSet = Set.of(
                Rectangle.of("0", "3", "0", "3"),
                Rectangle.of("3", "6", "3", "6")
        );

        Set<Rectangle<String>> twoThirdsSet = Set.of(
                Rectangle.of("0", "3", "0", "3"),
                Rectangle.of("1", "6", "2", "6"),
                Rectangle.of("8", "9", "8", "9")
        );

        Set<Rectangle<String>> edgeSet = Set.of(
                Rectangle.of("0", "3", "0", "3"),
                Rectangle.of("3", "6", "0", "6")
        );

        Set<Rectangle<String>> overlapSet = Set.of(
                Rectangle.of("0", "3", "0", "3"),
                Rectangle.of("1", "6", "2", "6")
        );

        assertFalse(RectangleGroup.from(discreteSet) .isConnected());
        assertFalse(RectangleGroup.from(cornerSet)   .isConnected());
        assertFalse(RectangleGroup.from(twoThirdsSet).isConnected());
        assertTrue( RectangleGroup.from(edgeSet)     .isConnected());
        assertTrue( RectangleGroup.from(overlapSet)  .isConnected());
    }
}
