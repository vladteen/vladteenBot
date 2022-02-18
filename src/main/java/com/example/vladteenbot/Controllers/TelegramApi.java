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
    String state;

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

        switch (state){
            case "Новая игра":
                switch (textFromUser){
                    case "/start":
                    case "Начать новую игру":
                        textFromBot = "Новая игра началась, выберите дальнейшее действие!";
                        state = "Нейтральный режим";
                        break;
                    default:
                        textFromBot = "Не понятно, что вы от меня хотите! Напишите /start для начала новой игры";
                }
                break;
            case "Нейтральный режим":
                switch (textFromUser){
                    case "/rest":
                    case "Отдыхать":
                        textFromBot = "Даже самым лучшим войнам нужен отдых, выберите дальнейшее действие!";
                        state = "Режим отдыха";
                        break;
                    case "/battle":
                    case "Вступить в бой!":
                        textFromBot = "Наконец-то достойный противник, эта схватка будет легендарной! Выберите дальнейщее действие!";
                        state = "Режим боя";
                        break;
                    case "/info":
                    case "Персонаж":
                        textFromBot = "Воин: " + userName;
                        state = "Нейтральный режим";
                        break;
                    default:
                        textFromBot = "Не понятно, что вы от меня хотите! Напишите /rest для отдыха, /battle для вступления в бой," +
                                      "/info для информации о персонаже";
                }
            case "Режим отдыха":
                switch (textFromUser){
                    case "/finish_rest":
                    case "Закончить отдых":
                        textFromBot = "Мы полностью восстановились и готовы, выберите дальнейшее действие!";
                        state = "Нейтральный режим";
                        break;
                    case "/info":
                    case "Персонаж":
                        textFromBot = "Воин: " + userName;
                        state = "Режим отдыха";
                        break;
                    default:
                        textFromBot = "Не понятно, что вы от меня хотите! Напишите /finish_rest для завершения отдыха, " +
                                "/info для информации о персонаже";
                }
            case "Режим боя":
                switch (textFromUser){
                    case "/battle":
                    case "Вступить в бой!":
                        textFromBot = "Наконец-то достойный противник, эта схватка будет легендарной! " +
                                "Выберите дальнейщее действие!";
                        state = "Режим боя";
                        break;
                    case "/kick":
                    case "Ударить":
                        textFromBot = "Вот это ударище О_О";
                        state = "Режим боя";
                        break;
                        // Враг жив или нет, в зависимости от этого поменяем состояние
                }
                break;
        }


//        if (textFromUser.equals("Привет")){
//            textFromBot = "Добро пожаловать, " + userName;
//        }else if(textFromUser.equals("/start") || textFromUser.equals("Начать новую игру")){
//            textFromBot = "Новая игра началась, выберите дальнейшее действие!";
//        }else if(textFromUser.equals("/neutral")){
//            textFromBot = "Приключение началось, выберите дальнейшее действие!";
//        }else if(textFromUser.equals("/rest")){
//            textFromBot = "Даже самым лучшим войнам нужен отдых, выберите дальнейшее действие!";
//        }else if(textFromUser.equals("/battle")){
//            textFromBot = "Наконец-то достойный противник, эта схватка будет легендарной! Выберите дальнейщее действие!";
//        }else{
//            textFromBot = "Не понятно, что вы от меня хотите! Напишите /start для начала новой игры";
//        }

        // Создаём объект с типом сообщение от бота
        var requestSendMessage = new SendMessage(chatId,textFromBot);

        if (textFromUser.equals("/start") || textFromUser.equals("Начать новую игру")){
            var keyboard = GetKeyboardForStart();
            requestSendMessage.replyMarkup(keyboard);
        }else if (textFromUser.equals("/neutral") || textFromUser.equals("Нейтральный режим")){
            var keyboard = GetKeyboardForNeutral();
            requestSendMessage.replyMarkup(keyboard);
        }else if (textFromUser.equals("/rest") || textFromUser.equals("Режим отдыха")){
            var keyboard = GetKeyboardForRest();
            requestSendMessage.replyMarkup(keyboard);
        }else if (textFromUser.equals("/battle") || textFromUser.equals("Режим боя")){
            var keyboard = GetKeyboardForBattle();
            requestSendMessage.replyMarkup(keyboard);
        }else{
            textFromBot = "Не понятно, что вы от меня хотите! Напишите /start для начала новой игры";
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

    private Keyboard GetKeyboardForStart(){
        // Создаём объект с типом клавиатура
        var keyboard = new ReplyKeyboardMarkup(new String[]{"Начать новую игру"});
        keyboard.oneTimeKeyboard(true);
        keyboard.resizeKeyboard(true);
        return keyboard;
    }
    private Keyboard GetKeyboardForNeutral(){
        // Создаём объект с типом клавиатура
        var keyboard = new ReplyKeyboardMarkup(
                new String[]{"Вступить в бой!", "Отдыхать"},
                new String[]{"Персонаж"}
        );
        keyboard.oneTimeKeyboard(true);
        keyboard.resizeKeyboard(true);
        return keyboard;
    }
    private Keyboard GetKeyboardForRest(){
        // Создаём объект с типом клавиатура
        var keyboard = new ReplyKeyboardMarkup(new String[]{"Закончить отдых", "Персонаж"});
        keyboard.oneTimeKeyboard(true);
        keyboard.resizeKeyboard(true);
        return keyboard;
    }
    private Keyboard GetKeyboardForBattle(){
        // Создаём объект с типом клавиатура
        var keyboard = new ReplyKeyboardMarkup(new String[]{"Вступить в бой!", "Ударить"});
        keyboard.oneTimeKeyboard(true);
        keyboard.resizeKeyboard(true);
        return keyboard;
    }
}
