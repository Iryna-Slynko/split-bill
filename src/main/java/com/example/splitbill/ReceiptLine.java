package com.example.splitbill;

import com.google.cloud.firestore.annotation.DocumentId;

public class ReceiptLine {


    public ReceiptLine() {

    }

    public ReceiptLine(String title, Integer price){
        this.price = price;
        this.title = title;
    }

    private Integer price;
    private String title;
    @DocumentId
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
