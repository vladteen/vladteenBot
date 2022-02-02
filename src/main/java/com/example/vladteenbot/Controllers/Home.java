package com.example.vladteenbot.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

    @RequestMapping("/")
    public String index()
    {
        System.out.println("Сработал метод index");
        return "ok";
    }
}
