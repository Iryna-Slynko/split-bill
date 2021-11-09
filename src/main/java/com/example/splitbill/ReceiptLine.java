package com.example.splitbill;

import java.math.BigDecimal;

record ReceiptLine(String title, BigDecimal price) {

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
