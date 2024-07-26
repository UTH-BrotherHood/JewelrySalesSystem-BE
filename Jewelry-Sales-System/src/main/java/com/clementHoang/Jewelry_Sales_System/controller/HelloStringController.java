package com.clementHoang.Jewelry_Sales_System.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// heath-check
@RestController
public class HelloStringController {
    @GetMapping("/hello")
    String sayHello() {
        return "Hello World!";
    }
}
