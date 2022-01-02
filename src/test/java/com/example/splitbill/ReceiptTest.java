package com.example.splitbill;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.cloud.vision.v1.AnnotateImageResponse;

import org.junit.jupiter.api.Test;

class ReceiptTest {
    @Test
    void testHenFixtureWithColumns() throws IOException {
        FileInputStream fis = new FileInputStream("src/fixtures/HEN");
        var air = AnnotateImageResponse.parseFrom(fis);
        var r = Receipt.fromImageRecognitionResponse(air);
        assertEquals(5, r.getLines().size());
        assertEquals("BROWNIE", r.getLines().get(0).getTitle());
        assertEquals("Americano coffee", r.getLines().get(2).getTitle());
        assertEquals("deli", r.getLines().get(3).getTitle());
        assertEquals("deli", r.getLines().get(4).getTitle());
    }

    @Test
    void testLcyFixtureWithQty() throws IOException {
        FileInputStream fis = new FileInputStream("src/fixtures/SSP");
        var air = AnnotateImageResponse.parseFrom(fis);
        var r = Receipt.fromImageRecognitionResponse(air);

        assertEquals(2, r.getLines().size());
        assertEquals("Maple Glaze Duck", r.getLines().get(0).getTitle());
        assertEquals("Olive & Bread", r.getLines().get(1).getTitle());
    }

    @Test
    void testOutdoorsFixtureWithGroups() throws IOException {
        FileInputStream fis = new FileInputStream("src/fixtures/Outdoors");
        var air = AnnotateImageResponse.parseFrom(fis);
        var r = Receipt.fromImageRecognitionResponse(air);
        assertEquals(6, r.getLines().size());
        assertEquals("Panna Cotta", r.getLines().get(0).getTitle());
        assertEquals("Panna Cotta", r.getLines().get(1).getTitle());
        assertEquals(600, r.getLines().get(0).getPrice());
        assertEquals(600, r.getLines().get(1).getPrice());

    }

    @Test
    void testGardensFixture() throws IOException {
        FileInputStream fis = new FileInputStream("src/fixtures/Mount");
        var air = AnnotateImageResponse.parseFrom(fis);
        var r = Receipt.fromImageRecognitionResponse(air);
        assertEquals(3, r.getLines().size());
        assertEquals("PANCAKES - MACADAMIA & BL", r.getLines().get(0).getTitle());
        assertEquals(1100, r.getLines().get(0).getPrice());
        assertEquals("LARGE AMERICANO - TS", r.getLines().get(1).getTitle());
        assertEquals(365, r.getLines().get(1).getPrice());
        assertEquals("LARGE CAPPUCINO TS", r.getLines().get(2).getTitle());
        assertEquals(395, r.getLines().get(2).getPrice());
    }
}
