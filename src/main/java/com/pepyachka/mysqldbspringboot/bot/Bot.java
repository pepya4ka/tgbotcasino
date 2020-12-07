package com.pepyachka.mysqldbspringboot.bot;

import com.pepyachka.mysqldbspringboot.MainController;
import com.pepyachka.mysqldbspringboot.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {


    @Override
    public String getBotUsername() {
        return "PepyachkaCasino_bot";
    }

    @Override
    public String getBotToken() {
        return "1428726749:AAF4gt0ptFpMrnCpR53vxsH8CCv6AccWYOE";
    }

    @Override
    public void onUpdateReceived(Update update) {
        MainController mainController = new MainController();

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        if (update.getMessage().getText().equals("/start")) {
            sendMessage.setChatId(update.getMessage().getChatId().toString());

            sendMessage.setText(mainController.addNewUser(createUser(update.getMessage())));
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
//        update.getMessage().getChatId()
        if (update.getMessage().getDice().getEmoji().equals("\uD83C\uDFB0")) {
//            SendMessage sendMessage = new SendMessage();
//            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            sendMessage.setText("Вы проиграли");
            switch (update.getMessage().getDice().getValue()) {
                case 1:
                    sendMessage.setText("Возврат!");
                    break;
                case 22:
                    sendMessage.setText("x3");
                    break;
                case 43:
                    sendMessage.setText("x10");
                    break;
                case 64:
                    sendMessage.setText("x100");
                    break;
            }
            try {
                    Thread.sleep(2000);
                execute(sendMessage);
            } catch (TelegramApiException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private User createUser(Message message) {
        User user = new User();
        user.setId(message.getFrom().getId());
        user.setChatId(Math.toIntExact(message.getChatId()));
        user.setUsername(message.getFrom().getUserName());
        user.setCoins(1000);
        user.setMaxPrize(0);

        return user;
    }
}
