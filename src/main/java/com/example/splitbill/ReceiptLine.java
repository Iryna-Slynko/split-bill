package com.example.splitbill;

import java.math.BigDecimal;

record ReceiptLine(String title, Integer price) {

    public String getTitle() {
        return title;
    }

    public Integer getPrice() {
        return price;
    }
}
