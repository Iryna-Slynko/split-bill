package com.example.splitbill;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        ArrayList<Integer> listPosition = new ArrayList<>();
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


        for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
            int index = 0;
            int itemY = annotation.getBoundingPoly().getVertices(0).getY();
            if (listPosition.size() > 0) {
                for (int i = listPosition.size() - 1; i >= 0; i--) {
                    if (listPosition.get(i) <= itemY) {
                        index = i + 1;
                        break;
                    }
                }
            }
            recognisedList.add(index, annotation.getDescription());
            listPosition.add(index, itemY);
        }

        return fromList(recognisedList);

    }

    @org.jetbrains.annotations.NotNull
    public static Receipt fromList(List<String> recognisedList) {
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
