package com.example.splitbill;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReceiptController {
        @GetMapping("/receipt")
        public void receipt(@RequestParam(name="name", required=false, defaultValue="Owner") String name, Model model) {
            model.addAttribute("name", name);
            model.addAttribute("receipt",
                    Receipt.newDemoReceipt());
        }

}
