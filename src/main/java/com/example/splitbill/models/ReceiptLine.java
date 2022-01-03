package com.example.splitbill.models;

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
    private String claimedByName;

    public String getClaimedByName() {
        return claimedByName;
    }

    public void setClaimedByName(String claimedByName) {
        this.claimedByName = claimedByName;
    }

    public String getClaimedByID() {
        return claimedByID;
    }

    public void setClaimedByID(String claimedByID) {
        this.claimedByID = claimedByID;
    }

    private String claimedByID;

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
