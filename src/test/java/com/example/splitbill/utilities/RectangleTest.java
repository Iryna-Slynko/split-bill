package com.example.splitbill.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {
    @Test
    void testOverlapRectangle() {
        Rectangle r = new Rectangle(181, 248, 348, 393);
        Rectangle r1 = new Rectangle(276, 295, 348, 393);
        Rectangle r2 = new Rectangle(322, 389, 348, 393);
        assertEquals(Rectangle.Position.RIGHT_JOIN, r.compareLocation(r1));
        assertEquals(Rectangle.Position.RIGHT_JOIN, r1.compareLocation(r2));
    }
}
