package com.example.splitbill;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class VisionController {

    @PostMapping("/extract-text")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        List<String> list = detectText(file.getInputStream());
        Receipt r = Receipt.fromList(list);
       // Receipt r = Receipt.newDemoReceipt2();
        model.addAttribute("receipt",r
                );
        return "receipt";
    }
    // Detects text in the specified image.
    public static List<String> detectText(InputStream fileStream) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(fileStream);

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION ).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            ArrayList<String> list = new ArrayList<>();
            ArrayList<Integer> listPosition = new ArrayList<>();
            for (AnnotateImageResponse res : responses) {
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
                        for(int i = listPosition.size() - 1; i >=0; i--) {
                            if (listPosition.get(i) <= itemY) {
                                index = i + 1;
                                break;
                            }
                        }
                    }
                    list.add(index, annotation.getDescription());
                    listPosition.add(index, itemY);
                }
            }
            return list;
        }
    }
}
