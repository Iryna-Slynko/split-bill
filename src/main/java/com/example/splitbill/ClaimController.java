package com.example.splitbill;

import com.example.splitbill.models.Receipt;
import com.example.splitbill.models.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ClaimController {
    @Autowired
    ReceiptRepository receiptRepository;

    @PutMapping("/receipt/{id}/claim/{item_number}")
    public ResponseEntity<Receipt> claim(@PathVariable(value = "id") String id, @PathVariable(value = "item_number") int item_number,
                                         @RequestBody UserInfo user) {
        Receipt r = receiptRepository.findById(id).block();
        var line = r.getLines().get(item_number);
        if (line.getClaimedByID() == null) {
            line.setClaimedByID(user.getUserId());
            line.setClaimedByName(user.getUsername());
            receiptRepository.save(r);
        }
        return ResponseEntity.ok(r);
    }

    @PutMapping("/receipt/{id}/unclaim/{item_number}")
    public ResponseEntity<Receipt> unclaim(@PathVariable(value = "id") String id, @PathVariable(value = "item_number") int item_number,
                                           @RequestBody UserInfo user) {
        Receipt r = receiptRepository.findById(id).block();
        var line = r.getLines().get(item_number);
        if (line.getClaimedByID() == user.getUserId()) {
            line.setClaimedByID(null);
            line.setClaimedByName(null);
            receiptRepository.save(r);
        }
        return ResponseEntity.ok(r);
    }

}
