package com.example.vladteenbot.Controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelegramApi {

    @RequestMapping("/telegramApi")
    public void telegram(@RequestBody String request)
    {
        System.out.println(request);
    }
}
