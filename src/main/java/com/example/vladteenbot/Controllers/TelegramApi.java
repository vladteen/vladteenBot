package com.example.vladteenbot.Controllers;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
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

        // Достаём нужные поля
        var chatId = update.message().from().id();
        var userName = update.message().from().username();
        var textFromUser = update.message().text();

        var textFromBot = "";

        if (textFromUser.equals("Привет")){
            textFromBot = "Добро пожаловать, " + userName;
        }else if(textFromUser.equals("/start")){
            textFromBot = "Новая игра началась, выберите дальнейшее действие!";
        }else{
            textFromBot = "Не понятно, что вы от меня хотите! Напишите /start для начала новой игры";
        }

        // Создаём объект с типом сообщение от бота
        var requestSendMessage = new SendMessage(chatId,textFromBot);

        if (textFromUser.equals("/start")){
            var keyboard = GetKeyboard();
            requestSendMessage.replyMarkup(keyboard);
        }

        bot.execute(requestSendMessage);

//        var sendMessageRequest = new SendMessage(update.message().chat().id(), "привет, " + update.message().from().firstName())
//        .replyToMessageId(update.message().messageId());
//        var sendMessageResponse = bot.execute(sendMessageRequest);
//        var response = BotUtils.toJson(sendMessageResponse);
//        switch (update.message().messageId()){
//            case "/start":
//                var sendMessage = new SendMessage(update.message().chat().id(), "привет, " + update.message().from().firstName())
//                        .replyToMessageId(update.message().messageId());
//        }
//        System.out.println("Ответ на метод sendMessage:");
//        System.out.println(response);

    }

    private Keyboard GetKeyboard(){
        // Создаём объект с типом клавиатура
        var keyboard = new ReplyKeyboardMarkup(
                new String[]{"1 строка 1 столбец", "1 строка 2 столбец"},
                new String[]{"2 строка 1 столбец", "1 строка 2 столбец"}
        );
        keyboard.oneTimeKeyboard(true);
        keyboard.resizeKeyboard(true);

        return keyboard;
    }
}
