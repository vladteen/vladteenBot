package com.example.vladteenbot.Controllers;

import kong.unirest.Unirest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelegramApi {
    final String telegramUrl = "https://api.telegram.org/bot";
    @RequestMapping("/telegramApi")
    public void telegram(@RequestBody String request)
    {
        System.out.print(request);
        var response = Unirest.post(telegramUrl + System.getenv("BOT_TOKEN") + "/sendMessage")
                .header("accept", "application/json")
                .field("chat_id", "202867842")
                .field("text", "Плазма форевер")
                .asJson();
        System.out.print(response.getBody().toString());

    }
}
