package com.example.splitbill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClaimController {
  @Autowired
  ReceiptRepository receiptRepository;

  @PostMapping("/receipt/{id}/claim/{item_number}")
  public String claim(@PathVariable(value = "id") String id, @PathVariable(value = "item_number") String item_number) {
          Receipt r = receiptRepository.findById(id).block();
          return "receipt";
  }
}
