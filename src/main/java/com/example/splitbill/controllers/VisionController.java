package com.example.splitbill;

import com.example.splitbill.models.Receipt;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VisionController {
    @Autowired
    ReceiptRepository receiptRepository;

    @PostMapping("/extract-text")
    public String handleFileUpload(@RequestParam(name = "file", required = true) MultipartFile file,
            @RequestParam(name = "userid", required = true) String ownerId,
            @RequestParam(name = "username", required = true) String ownerName,
            Model model) throws IOException {
        Receipt r = Receipt.fromImageRecognitionResponse(detectText(file.getInputStream()));
        //Receipt r = Receipt.newDemoReceipt();
        r.setOwnerId(ownerId);
        r.setOwnerName(ownerName);
        this.receiptRepository.save(r).block();
        model.addAttribute("receipt", r);
        return "receipt";
    }

    // Detects text in the specified image.
    public static AnnotateImageResponse detectText(InputStream fileStream) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(fileStream);

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs
        // to be created
        // once, and can be reused for multiple requests. After completing all of your
        // requests, call
        // the "close" method on the client to safely clean up any remaining background
        // resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            return response.getResponses(0);

        }
    }
}
