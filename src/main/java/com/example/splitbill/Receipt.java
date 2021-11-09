package com.example.splitbill;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Receipt {
    private ArrayList<ReceiptLine> lines = new ArrayList<>();

    public ArrayList<ReceiptLine> getLines() {
        return lines;
    }

    static public Receipt newDemoReceipt() {
        Receipt r = new Receipt();
        r.lines.add(new ReceiptLine("banana", new BigDecimal("5")));
        return r;
    }
}
