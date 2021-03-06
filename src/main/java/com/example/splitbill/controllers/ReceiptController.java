package com.example.splitbill.controllers;

import com.example.splitbill.ReceiptRepository;
import com.example.splitbill.models.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReceiptController {
    @Autowired
    ReceiptRepository receiptRepository;

    @GetMapping("/receipt")
    public void receipt(@RequestParam(name = "name", required = false, defaultValue = "Owner") String name,
                        @RequestParam(name = "userid", required = false, defaultValue = "123") String ownerId,
                        Model model) {
        var r = Receipt.newDemoReceipt();
        r.setOwnerId(ownerId);
        r.setOwnerName(name);
        model.addAttribute("name", name);
        model.addAttribute("receipt", r
        );
    }

    @GetMapping("/receipt/{id}")
    public String receiptInfo(@PathVariable(value = "id") String id, Model model) {
        Receipt r = receiptRepository.findById(id).block();
        model.addAttribute("receipt", r);
        return "receipt";
    }

    @GetMapping("/receipt/{id}/data")
    public ResponseEntity<Receipt> receiptDara(@PathVariable(value = "id") String id,
                                               @RequestParam(name = "userId", required = false, defaultValue = "123") String ownerId,
                                               @RequestParam(name = "username", required = false, defaultValue = "123") String ownerName) {
        return ResponseEntity.ok(receiptRepository.findById(id).block());
    }
}
