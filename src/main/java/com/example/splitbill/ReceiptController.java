package com.example.splitbill;

import org.springframework.beans.factory.annotation.Autowired;
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
                        Model model) {
                model.addAttribute("name", name);
                model.addAttribute("receipt",
                                Receipt.newDemoReceipt());
        }

        @GetMapping("/receipt/{id}")
        public String receiptInfo(@PathVariable(value = "id") String id, Model model) {
                Receipt r = receiptRepository.findById(id).block();
                model.addAttribute("receipt", r);
                return "receipt";
        }

}
