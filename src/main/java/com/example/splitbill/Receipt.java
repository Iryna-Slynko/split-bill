package com.example.splitbill;

import com.example.splitbill.utilities.Rectangle;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Receipt class defines the receipt and contains item list
 */
public class Receipt {

    private ArrayList<ReceiptLine> lines = new ArrayList<>();
    private static Pattern pricePattern = Pattern.compile("^\\d+((,|.)\\d{1,2})?$");

    public static Receipt fromImageRecognitionResponse(AnnotateImageResponse res) {
        ArrayList<String> recognisedList = new ArrayList<>();
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
                    if (result == Rectangle.Position.RIGHT_JOIN) {
                        recognisedList.set(i, recognisedList.get(i) + " " + annotation.getDescription());
                        listPosition.get(i).merge(item);
                        added = true;
                        break;
                    } else if ((result == Rectangle.Position.RIGHT_SEPARATE) || (result == Rectangle.Position.LOWER)) {
                        if (i < (recognisedList.size()) - 1) {
                            recognisedList.add(i, annotation.getDescription());
                            listPosition.add(i, item);
                            added = true;
                        }
                        break;
                    }
                }
            }
            if (!added) {
                recognisedList.add(annotation.getDescription());
                listPosition.add(item);
            }
        }
        Receipt r = new Receipt();
        ArrayList<ReceiptLine> lines = new ArrayList<>();
        String itemTitle = "";
        for (String recognisedLine : recognisedList.stream().skip(1).toList()) {
            if (recognisedLine.toUpperCase().contains("TOTAL")) {
                break;
            }
            Matcher matcher = pricePattern.matcher(recognisedLine);
            if (matcher.find()) {
                BigDecimal price = new BigDecimal(matcher.group());
                lines.add(new ReceiptLine(itemTitle, price));
            } else {
                itemTitle = recognisedLine;
            }
        }
        r.lines = lines;
        return r;
    }

    public ArrayList<ReceiptLine> getLines() {
        return lines;
    }

    static public Receipt newDemoReceipt() {
        Receipt r = new Receipt();
        r.lines.add(new ReceiptLine("banana", new BigDecimal("5")));
        r.lines.add(new ReceiptLine("Ice cream", new BigDecimal("5")));
        r.lines.add(new ReceiptLine("Capuccino", new BigDecimal("3")));
        r.lines.add(new ReceiptLine("Bread", new BigDecimal("50")));
        r.lines.add(new ReceiptLine("Water", new BigDecimal("1000")));

        return r;
    }
}
