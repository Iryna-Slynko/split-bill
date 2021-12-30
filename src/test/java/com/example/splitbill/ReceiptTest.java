package com.example.splitbill;

import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.EntityAnnotationOrBuilder;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReceiptTest {
    @Test
    void fromList() {
        String[] arr = new String[]{"""
                HEN & HOG
                ASHFORD
                WICKLOW
                BROWNIE
                Cappucino
                Americano coffee
                deli
                deli
                2.70
                3.20
                3.00
                3.50
                3.50
                Total:
        VAT Total:
        15.90
        2.99
        Paid By:
        Cards
        15.90
        No Change Due
        28/09/2021 14:12
        GUEST
        001-01-09408
        Items 5
        """,
        "finnbees",
                "Order Number",
                "204145 (01)",
                "Item Description",
                "PANINI HC",
                "5.45",
                "PANINI HC",
                "5.45",
                "SAMBO CHIC",
                "5.45",
                "OPEN DEP",
                "3.00",
                "ORANGE JUICE E/I",
                "2.75",
                "ORANGE JUICE E/I",
                "2.75",
                "ORANGE JUICE E/I",
                "2.75",
                "AMERICANO GRANDE",
                "3.00",
                "GLAZED DONUT",
                "2.50",
                "FLAPJACK E/I",
                "2.95",
                "OPEN DEP",
                "3.25",
                "SUB TOTAL",
                "39.30",
                "39.30",
                "Credit Card",
                "Total Items",
                "11",
                "RCPT NO: 20414501",
                "TILL 1",
                "TID: ****5018",
                "MID: ***43444",
                "KEEP THIS COPY FOR YOUR RECORDS",
                "16/10/21 15:04",
                "00080226",
                "(VISA CREDIT)",
                "VISA",
                "APP ID: A000000031010",
                "PAN SEQ: 01",
                "TC: FOBCAE676AE2A7B8",
                "**** 7714",
                "**本中",
                "SALE",
                "AMOUNT",
                "TOTAL",
                "EUR39.30",
                "EUR39.30",
                "Youn ACCOUNT",
                ", finnbees, Order, Number, 204145, (01), Item, Description, PANINI, HC, 5.45, PANINI, HC, 5.45, SAMBO, CHIC, 5.45, OPEN, DEP, 3.00, ORANGE, JUICE, E/I, 2.75, ORANGE, JUICE, E/I, 2.75, ORANGE, JUICE, E/I, 2.75, AMERICANO, GRANDE, 3.00, GLAZED, DONUT, 2.50, FLAPJACK, E/I, 2.95, OPEN, DEP, 3.25, SUB, TOTAL, 39.30, 39.30, Credit, Card, Total, Items, 11, RCPT, NO:, 20414501, TILL, 1, TID:, ****5018, MID:, ***43444, KEEP, THIS, COPY, FOR, YOUR, RECORDS, 16/10/21, 15:04, 00080226, (VISA, CREDIT), VISA, APP, ID:, A000000031010, PAN, SEQ:, 01, TC:, FOBCAE676AE2A7B8, ****, 7714, **, 本, 中, SALE, AMOUNT, TOTAL, EUR39.30, EUR39.30, Youn, ACCOUNT]",
                ""};
        List<String> list = Arrays.stream(arr).toList();
        Receipt r = Receipt.fromList(list);
        var lines = r.getLines();
        assertEquals(11, lines.size());
        assertEquals(new BigDecimal("5.45"), lines.get(0).getPrice());
        assertEquals("PANINI HC", lines.get(0).getTitle());
    }
}