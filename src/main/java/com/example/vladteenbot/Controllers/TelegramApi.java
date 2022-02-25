package com.example.vladteenbot.Controllers;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.web.bind.annotation.*;

@RestController
public class TelegramApi {
    TelegramBot bot = new TelegramBot(System.getenv("BOT_TOKEN"));
    String state = "";

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
            default:
                StatusForNewGame(chatId, textFromUser, textFromBot);
                break;
            case "Нейтральный режим":
                StatusForNeutral(chatId, textFromUser, textFromBot, userName);
                break;
            case "Режим отдыха":
                StatusForRest(chatId, textFromUser, textFromBot, userName);
                break;
            case "Режим боя":
                StatusForBattle(chatId, textFromUser, textFromBot, userName);
                break;
        }

        // Создаём объект с типом сообщение от бота
//        var requestSendMessage = new SendMessage(chatId,textFromBot);
//        textFromBot = "Не понятно, что вы от меня хотите! Напишите /start для начала новой игры";
//
//
//        bot.execute(requestSendMessage);
    }

    private void StatusForNewGame(long chatId,String textFromUser, String textFromBot) {
        switch (textFromUser){
            case "/start":
            case "Начать новую игру":
                StartActionOnStatusForNewGame(chatId);
                break;
            case "/restart":
            case "Рестарт":
                RestartActionOnStatusForNewGame(chatId);
                break;
            default:
                DefaultActionOnStatusForNewGame(chatId);
        }
    }

    private void StartActionOnStatusForNewGame(long chatId) {
        var textFromBot = "Новая игра началась, выберите дальнейшее действие!";
        state = "Нейтральный режим";
        var requestSendMessage = new SendMessage(chatId,textFromBot);
        var keyboard = GetKeyboardForNeutral();
        requestSendMessage.replyMarkup(keyboard);
        bot.execute(requestSendMessage);
    }

    private void RestartActionOnStatusForNewGame(long chatId) {
        var textFromBot = "Вы выбрали рестарт, игра возвращена в начальное состояние, выберите дальнейшее действие!";
        state = "Новая игра";
        var requestSendMessage = new SendMessage(chatId,textFromBot);
        var keyboard = GetKeyboardForStart();
        requestSendMessage.replyMarkup(keyboard);
        bot.execute(requestSendMessage);
    }

    private void DefaultActionOnStatusForNewGame(long chatId) {
        var textFromBot = "Не понятно, что вы от меня хотите! Напишите /start для начала новой игры";
        var requestSendMessage = new SendMessage(chatId,textFromBot);
        var keyboard = GetKeyboardForStart();
        requestSendMessage.replyMarkup(keyboard);
        bot.execute(requestSendMessage);
    }

    private void StatusForNeutral(Long chatId, String textFromUser, String textFromBot, String userName) {
        switch (textFromUser){
            case "/rest":
            case "Отдыхать":
                RestActionOnStatusForNeutral(chatId);
                break;
            case "/battle":
            case "Вступить в бой!":
                BattleActionOnStatusForNeutral(chatId);
                break;
            case "/info":
            case "Персонаж":
                InfoActionOnStatusForNeutral(chatId, userName);
                break;
            default:
                DefaultActionOnStatusForNeutral(chatId);
        }
    }

    private void RestActionOnStatusForNeutral(long chatId) {
        var textFromBot = "Даже самым лучшим войнам нужен отдых, выберите дальнейшее действие!";
        state = "Режим отдыха";
        var requestSendMessage = new SendMessage(chatId,textFromBot);
        var keyboard = GetKeyboardForRest();
        requestSendMessage.replyMarkup(keyboard);
        bot.execute(requestSendMessage);
    }

    private void BattleActionOnStatusForNeutral(long chatId) {
        var textFromBot = "Наконец-то достойный противник, эта схватка будет легендарной! Выберите дальнейщее действие!";
        state = "Режим боя";
        var requestSendMessage = new SendMessage(chatId,textFromBot);
        var keyboard = GetKeyboardForBattle();
        requestSendMessage.replyMarkup(keyboard);
        bot.execute(requestSendMessage);
    }

    private void InfoActionOnStatusForNeutral(long chatId, String userName) {
        var textFromBot = "Воин: " + userName;
        state = "Нейтральный режим";
        var requestSendMessage = new SendMessage(chatId,textFromBot);
        var keyboard = GetKeyboardForNeutral();
        requestSendMessage.replyMarkup(keyboard);
        bot.execute(requestSendMessage);
    }

    private void DefaultActionOnStatusForNeutral(long chatId) {
        var textFromBot = "Не понятно, что вы от меня хотите! Напишите /rest для отдыха, /battle для вступления в бой," +
                "/info для информации о персонаже";
        var requestSendMessage = new SendMessage(chatId,textFromBot);
        var keyboard = GetKeyboardForNeutral();
        requestSendMessage.replyMarkup(keyboard);
        bot.execute(requestSendMessage);

    }

    private void StatusForRest(Long chatId, String textFromUser, String textFromBot, String userName) {
        switch (textFromUser){
            case "/finish_rest":
            case "Закончить отдых":
                FinishRestActionOnStatusForRest(chatId);
                break;
            case "/info":
            case "Персонаж":
                InfoActionOnStatusForRest(chatId, userName);
                break;
            default:
                DefaultActionOnStatusForRest(chatId);
        }
    }

    private void FinishRestActionOnStatusForRest(long chatId) {
        var textFromBot = "Мы полностью восстановились и готовы, выберите дальнейшее действие!";
        state = "Нейтральный режим";
        var requestSendMessage = new SendMessage(chatId,textFromBot);
        var keyboard = GetKeyboardForNeutral();
        requestSendMessage.replyMarkup(keyboard);
        bot.execute(requestSendMessage);
    }

    private void InfoActionOnStatusForRest(long chatId, String userName) {
        var textFromBot = "Воин: " + userName;
        state = "Режим отдыха";
        var requestSendMessage = new SendMessage(chatId,textFromBot);
        var keyboard = GetKeyboardForRest();
        requestSendMessage.replyMarkup(keyboard);
        bot.execute(requestSendMessage);
    }

    private void DefaultActionOnStatusForRest(long chatId) {
        var textFromBot = "Не понятно, что вы от меня хотите! Напишите /finish_rest для завершения отдыха, " +
                "/info для информации о персонаже";
        var requestSendMessage = new SendMessage(chatId,textFromBot);
        var keyboard = GetKeyboardForRest();
        requestSendMessage.replyMarkup(keyboard);
        bot.execute(requestSendMessage);

    }

    private void StatusForBattle(Long chatId, String textFromUser, String textFromBot, String userName) {
        switch (textFromUser){
            case "/kick_and_win":
            case "Ударить и победить":
                KickAndWinActionOnStatusForBattle(chatId);
                break;
            case "/kick_and_lose":
            case "Ударить и проиграть":
                KickAndLoseActionOnStatusForBattle(chatId);
                break;
            case "/kick_and_alive":
            case "Ударить и оба выжили":
                KickAndAliveActionOnStatusForBattle(chatId);
                break;
            default:
                DefaultActionOnStatusForBattle(chatId);
        }
    }

    private void KickAndWinActionOnStatusForBattle(long chatId) {
        var textFromBot = "Ты его уничтожил! Победа твоя, прими поздравления!";
        state = "Нейтральный режим";
        var requestSendMessage = new SendMessage(chatId,textFromBot);
        var keyboard = GetKeyboardForNeutral();
        requestSendMessage.replyMarkup(keyboard);
        bot.execute(requestSendMessage);
    }

    private void KickAndLoseActionOnStatusForBattle(long chatId) {
        var textFromBot = "Завтра ты станешь сильнее, продолжай верить в себя!";
        state = "Новая игра";
        var requestSendMessage = new SendMessage(chatId,textFromBot);
        var keyboard = GetKeyboardForStart();
        requestSendMessage.replyMarkup(keyboard);
        bot.execute(requestSendMessage);
    }

    private void KickAndAliveActionOnStatusForBattle(long chatId) {
        var textFromBot = "Вот это удар О_О, но враг всё ещё жив!";
        state = "Режим боя";
        var requestSendMessage = new SendMessage(chatId,textFromBot);
        var keyboard = GetKeyboardForBattle();
        requestSendMessage.replyMarkup(keyboard);
        bot.execute(requestSendMessage);
    }

    private void DefaultActionOnStatusForBattle(long chatId) {
        var textFromBot = "Не понятно, что вы от меня хотите! Обратитесь к кнопкам!";
        var requestSendMessage = new SendMessage(chatId,textFromBot);
        var keyboard = GetKeyboardForBattle();
        requestSendMessage.replyMarkup(keyboard);
        bot.execute(requestSendMessage);
    }


    private Keyboard GetKeyboardForStart(){
        // Создаём объект с типом клавиатура
        var keyboard = new ReplyKeyboardMarkup(new String[]{"Начать новую игру", "Рестарт"});
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
        var keyboard = new ReplyKeyboardMarkup(new String[]{"Ударить и победить", "Ударить и проиграть", "Ударить и оба выжили"});
        keyboard.oneTimeKeyboard(true);
        keyboard.resizeKeyboard(true);
        return keyboard;
    }
}
