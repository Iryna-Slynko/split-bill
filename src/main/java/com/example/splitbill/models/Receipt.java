package com.example.splitbill.models;

import com.example.splitbill.utilities.Rectangle;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.firestore.annotation.PropertyName;
import com.google.cloud.spring.data.firestore.Document;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Receipt class defines the receipt and contains item list
 */
@Document(collectionName = "receipts")
public class Receipt {
    @DocumentId
    private String ID;
    @PropertyName("owner_id")
    private String ownerId;
    @PropertyName("owner_name")
    private String ownerName;
    private Timestamp updatedAt;

    @PropertyName("receipt_lines")
    private ArrayList<ReceiptLine> lines = new ArrayList<>();
    private static Pattern pricePattern = Pattern.compile("^[1-9]\\d*(,|\\.)\\d{2}$");

    public static Receipt fromImageRecognitionResponse(AnnotateImageResponse res) {
        ArrayList<ArrayList<String>> recognisedList = new ArrayList<>();
        ArrayList<Rectangle> listPosition = new ArrayList<>();
            /* save response
            FileOutputStream fos = new FileOutputStream(res.getTextAnnotations(1).getDescription());
            res.writeTo(fos);
            fos.flush();
            fos.close();
             /**/
        if (res.hasError()) {
            System.out.format("Error: %s%n", res.getError().getMessage());
            return null;
        }

        // assumptions:
        // the first element is the whole recognized document - can be verified later
        // array is organised in one of two directions: line by line horizontally(usually), sometimes(vertically)

        for (EntityAnnotation annotation : res.getTextAnnotationsList().stream().skip(1).toList()) {
            Rectangle item = new Rectangle(annotation.getBoundingPoly().getVerticesList());
            Boolean added = false;
            if (listPosition.size() > 0) {
                for (int i = listPosition.size() - 1; i >= 0; i--) {
                    var result = listPosition.get(i).compareLocation(item);
                    if ((result == Rectangle.Position.RIGHT_JOIN) || (result == Rectangle.Position.RIGHT_SEPARATE)) {
                        recognisedList.get(i).add(annotation.getDescription());
                        listPosition.get(i).merge(item);
                        added = true;
                        break;
                    } else if (result == Rectangle.Position.HIGHER) {
                        break;
                    }
                }
            }
            if (!added) {
                ArrayList<String> list = new ArrayList<>();
                recognisedList.add(list);
                list.add(annotation.getDescription());
                listPosition.add(item);
            }
        }
        Receipt r = new Receipt();
        ArrayList<ReceiptLine> lines = new ArrayList<>();
        for (ArrayList<String> recognisedLine : recognisedList) {
            if (isTotalLine(recognisedLine)) {
                break;
            }
            lines.addAll(getReceiptLines(recognisedLine));
        }
        r.lines = lines;

        return r;
    }

    private static ArrayList<ReceiptLine> getReceiptLines(ArrayList<String> recognisedLine) {
        ArrayList<ReceiptLine> list = new ArrayList<>();
        if (recognisedLine.size() == 1) {
            return list;
        }
        String potentialPrice = recognisedLine.remove(recognisedLine.size() - 1);
        Matcher matcher = pricePattern.matcher(potentialPrice);
        if (!matcher.find()) {
            potentialPrice = recognisedLine.remove(recognisedLine.size() - 1);
            matcher = pricePattern.matcher(potentialPrice);
            if (!matcher.find()) {
                return list;
            }
        }

        String itemTitle = "";
        BigDecimal price = new BigDecimal(matcher.group());
        String potentialQuantity = recognisedLine.get(0);
        Integer quantity = 1;
        if (potentialQuantity.matches("\\d")) {
            recognisedLine.remove(0);
            quantity = Integer.parseInt(potentialQuantity);
        }
        potentialQuantity = recognisedLine.get(recognisedLine.size()-1);
        if (potentialQuantity.matches("\\d")) {
            recognisedLine.remove(recognisedLine.size() - 1);
            quantity = Integer.parseInt(potentialQuantity);
        }
        for (String word :
                recognisedLine) {
            itemTitle += word + " ";
        }
        itemTitle = itemTitle.strip();
        Integer priceInCents = price.divide(BigDecimal.valueOf(quantity)).multiply(BigDecimal.valueOf(100)).intValue();
        for (int i = 0; i < quantity; i++) {
            list.add(new ReceiptLine(itemTitle, priceInCents));
        }

        return list;
    }

    private static boolean isTotalLine(ArrayList<String> recognisedLine) {
        for (String word : recognisedLine) {
            if (word.toUpperCase().contains("TOTAL")) {
                return true;
            }
        }
        return false;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public void setLines(ArrayList<ReceiptLine> lines) {
        this.lines = lines;
    }

    public ArrayList<ReceiptLine> getLines() {
        return lines;
    }

    public String getID() {
        return ID;
    }

    static public Receipt newDemoReceipt() {
        Receipt r = new Receipt();
        r.lines.add(new ReceiptLine("banana", 500));
        r.lines.add(new ReceiptLine("Ice cream", 493));
        r.lines.add(new ReceiptLine("Capuccino", 315));
        r.lines.add(new ReceiptLine("Bread", 5000));
        r.lines.add(new ReceiptLine("Water", 120000));

        return r;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
