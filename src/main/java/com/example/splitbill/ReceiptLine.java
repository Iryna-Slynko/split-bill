package com.example.splitbill;

public class ReceiptLine {


    public ReceiptLine() {

    }

    public ReceiptLine(String title, Integer price){
        this.price = price;
        this.title = title;
    }

    private Integer price;
    private String title;

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Integer getPrice() {
        return price;
    }
}
