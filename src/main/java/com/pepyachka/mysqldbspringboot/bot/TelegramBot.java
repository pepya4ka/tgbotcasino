package com.pepyachka.mysqldbspringboot.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static final String TOKEN = "1428726749:AAF4gt0ptFpMrnCpR53vxsH8CCv6AccWYOE";

    private static final String USERNAME = "PepyachkaCasino_bot";

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            final long chat_id = update.getMessage().getChatId();
            sendTextMessage(chat_id, "Привет!");
        }
        if (update.getMessage().getText() == null) {
            int i = 0;
            i += 20;
            Sticker sticker = update.getMessage().getSticker();
            sticker.getEmoji();//1612709158
        }
    }

    private synchronized void sendTextMessage(long chat_id, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false)
                .setChatId(chat_id)
                .setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}
