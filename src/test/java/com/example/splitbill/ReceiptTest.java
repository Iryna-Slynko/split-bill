package com.example.splitbill;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

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

    void fromListWithStrangeNumbers() {
        var arr = new String[]{"""
                Mount
                Usher
        gardens
        Avoca @ Mount Usher Gardens
                Ashford
        Co.Wicklow
        Tel:+353 404 40116
        Reg : 060
        10:05 22/09/21
        Table : 117
        Ref: 060210922093650
        Description
                Qty
        Price
        PANCAKES - MACADAMIA & BL
        LARGE AMERICANO - TS
        TS
        11.00
        3.65
        3.95
        LARGE CAPPUCINO
        1
        Total Due
        18.60
        """, "Mount", "Usher", "gardens", "Avoca", "@", "Mount", "Usher", "Gardens", "Ashford", "Co.Wicklow", "Tel:+353", "404", "40116", "Reg", ":", "060", "10:05", "22/09/21", "Table", ":", "117", "Ref:", "060210922093650", "Description", "Qty", "Price", "PANCAKES", "-", "MACADAMIA", "&", "BL", "LARGE", "AMERICANO", "-", "TS", "TS", "11.00", "3.65", "3.95", "LARGE", "CAPPUCINO", "1", "Total", "Due", "18.60"
        };
        List<String> list = Arrays.stream(arr).toList();
        Receipt r = Receipt.fromList(list);
        var lines = r.getLines();
        assertEquals(3, lines.size());
        assertEquals("PANCAKES - MACADAMIA & BL", lines.get(0).getTitle());
    }

    void fromColumnList() {
        var arr = new String[]{
                """
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
        """, "HEN", "&", "HOG", "ASHFORD", "WICKLOW", "BROWNIE", "Cappucino", "Americano", "coffee", "deli", "deli", "2.70", "3.20", "3.00", "3.50", "3.50", "Total:", "VAT", "Total:", "15.90", "2.99", "Paid", "By:", "Cards", "15.90", "No", "Change", "Due", "28/09/2021", "14:12", "GUEST", "001-01-09408", "Items", "5"
        };
        List<String> list = Arrays.stream(arr).toList();
        Receipt r = Receipt.fromList(list);
        var lines = r.getLines();
        assertEquals(5, lines.size());
        assertEquals("BROWNIE", lines.get(0).getTitle());

    }

    void fromListWithQuantity() {
        var arr = new String[]{"""                
                SSP UK
        LCY - City Bar & Grill
        8411131
        432 Ismat A
        Tbl 809/2
        Chk 3758
        06May'16 18:14
        Gst 1
        Eat In
        1 Maple Glaze Duck
        1 0live & Bread
        22.95
        2.95
        Total
        25.90
         """, "SSP", "UK", "LCY", "-", "City", "Bar", "&", "Grill", "8411131", "432", "Ismat", "A", "Tbl", "809/2", "Chk", "3758", "06May'16", "18:14", "Gst", "1", "Eat", "In", "1", "Maple", "Glaze", "Duck", "1", "0live", "&", "Bread", "22.95", "2.95", "Total", "25.90"
        };
        List<String> list = Arrays.stream(arr).toList();
        Receipt r = Receipt.fromList(list);
        var lines = r.getLines();
        assertEquals(2, lines.size());
        assertEquals("Maple Glaze Duck", lines.get(0).getTitle());
    }

    void fromListWithGroups() {
        var arr = new String[]{"""
                place Vour
                OUTDOORS
        E001 Eatin
        TABLE: 25
        25 Sep 2021 17:13:14
        Requested for: NOW
        Server: Staff Covers: 5
        Price
        Qty Description
        Food
        Panna Cotta
        Chocolate Cake
        12.00
        Affogato
        Tiramisu Dolcetti
        6.00
        5.00
        5.00
        1
        1
        Drinks
        Hot Chocolate
        1
        2.80
        Stem Count:
        6.
        Total:
        30.80
                ** NOT PAID **
                """
                , "place", "Vour", "OUTDOORS", "E001", "Eatin", "TABLE:", "25", "25", "Sep", "2021", "17:13:14", "Requested", "for:", "NOW", "Server:", "Staff", "Covers:", "5", "Price", "Qty", "Description", "Food", "Panna", "Cotta", "Chocolate", "Cake", "12.00", "Affogato", "Tiramisu", "Dolcetti", "6.00", "5.00", "5.00", "1", "1", "Drinks", "Hot", "Chocolate", "1", "2.80", "Stem", "Count:", "6.", "Total:", "30.80", "**", "NOT", "PAID", "**"
	};
        List<String> list = Arrays.stream(arr).toList();
        Receipt r = Receipt.fromList(list);
        var lines = r.getLines();
        assertEquals(6, lines.size());
        assertEquals("Panna Cotta", lines.get(0).getTitle());
    }
}
