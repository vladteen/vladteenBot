package com.example.vladteenbot.Controllers;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelegramApi {
    TelegramBot bot = new TelegramBot(System.getenv("BOT_TOKEN"));


    @RequestMapping("/telegramApi")
    public void telegram(@RequestBody String request)
    {

        System.out.println("Входящий запрос:");
        System.out.println(request);
        var update = BotUtils.parseUpdate(request);
        var sendMessageRequest = new SendMessage(update.message().chat().id(), "привет, " + update.message().from().firstName())
        .replyToMessageId(update.message().messageId());
        var sendMessageResponse = bot.execute(sendMessageRequest);
        var response = BotUtils.toJson(sendMessageResponse);
        System.out.println("Ответ на метод sendMessage:");
        System.out.println(response);

    }
}
