package com.example.splitbill;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import org.junit.jupiter.api.Test;

class ReceiptTest {
    @Test
    void testHenFixtureWithColumns() throws IOException {
        FileInputStream fis = new FileInputStream("src/fixtures/HEN");
        var air = AnnotateImageResponse.parseFrom(fis);
        var r = Receipt.fromImageRecognitionResponse(air);
        assertEquals(5, r.getLines().size());
    }
}
