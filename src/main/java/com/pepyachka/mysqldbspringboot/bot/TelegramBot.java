package com.pepyachka.mysqldbspringboot.bot;

import com.pepyachka.mysqldbspringboot.MainController;
import com.pepyachka.mysqldbspringboot.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final
    MainController mainController;

    private static final String TOKEN = System.getenv("TOKEN");

    private static final String USERNAME = System.getenv("USERNAME");

    private boolean flRate = false;
    private int rate = 0;

    public TelegramBot(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText("Вы ввели неверную команду.\nВведите вашу ставку");

        User user = mainController.getById(update.getMessage().getFrom().getId());
        if (update.getMessage().getText() != null) {
            if (!flRate && isDigit(update.getMessage().getText())) {
                if (user.getCoins() == 0) {
                    sendTextMessage(update.getMessage().getChatId().toString(), "У Вас нет монет для ставки! \nПожалуйста, обратитесь к администратору");
                    return;
                }
                rate = Integer.parseInt(update.getMessage().getText());
                if (rate > user.getCoins()) {
                    rate = 0;
                    sendTextMessage(update.getMessage().getChatId().toString(), "У Вас нет столько монет! \nПожалуйста, введите верное число");
                }
                flRate = true;
                //                sendTextMessage(update.getMessage().getChatId().toString(), "Ваша ставка принята, пожалуйста, отправьте " + "\uD83C\uDFB0");
                sendMessage.setText("Ваша ставка принята, пожалуйста, отправьте " + "\uD83C\uDFB0");
            }

            //
            //            if (!flRate)
            //                sendTextMessage(update.getMessage().getChatId().toString(), "");

            if (update.getMessage().getText().equals("/start")) {
                user = mainController.getById(update.getMessage().getFrom().getId());
                if (user == null) {
                    sendMessage.setText(mainController.addNewUser(createUser(update.getMessage())));
                } else {
                    sendMessage.setText("Вы уже зарегистрированы. Ваше количество монет - "
                        + user.getCoins()
                        + ".\nВведите вашу ставку");
                }
            }

            if (update.getMessage().getText().equals("/countCoins")) {
                user = mainController.getById(update.getMessage().getFrom().getId());
                sendMessage.setText("Ваше количество монет = " + user.getCoins() + ".\nВведите вашу ставку");
            }

            if (update.getMessage().getText().equals("/all")) {
                sendMessage.setText(mainController.getAllUsers(user.getId()) + "\nВведите вашу ставку");
            }
        }

        if (update.getMessage().getText() == null && flRate) {
            if (update.getMessage().getDice().getEmoji().equals("\uD83C\uDFB0")) {
                user = mainController.getById(update.getMessage().getFrom().getId());
                flRate = false;
                switch (update.getMessage().getDice().getValue()) {
                    case 1:
                        mainController.updateCoins(update.getMessage().getFrom().getId(), 0);
                        sendMessage.setText("Возврат.\nВаше количество монет = " + user.getCoins() + "\nВведите вашу ставку");
                        break;
                    case 22:
                        mainController.updateCoins(update.getMessage().getFrom().getId(), rate * 3);
                        user.setCoins(user.getCoins() + rate * 3);
                        sendMessage.setText("x3!\nВаш выигрыш - " + rate * 3 + ".\nВаше количество монет = " + user.getCoins() + "\nВведите вашу ставку");
                        break;
                    case 43:
                        mainController.updateCoins(update.getMessage().getFrom().getId(), rate * 10);
                        user.setCoins(user.getCoins() + rate * 10);
                        sendMessage.setText("x10!!\nВаш выигрыш - " + rate * 10 + ".\nВаше количество монет = " + user.getCoins() + "\nВведите вашу ставку");
                        break;
                    case 64:
                        mainController.updateCoins(update.getMessage().getFrom().getId(), rate * 100);
                        user.setCoins(user.getCoins() + rate * 100);
                        sendMessage.setText("x100!!!\nВаш выигрыш - " + rate * 100 + ".\nВаше количество монет = " + user.getCoins() + "\nВведите вашу ставку");
                        break;
                    default:
                        mainController.updateCoins(update.getMessage().getFrom().getId(), rate * (-1));
                        user.setCoins(user.getCoins() + rate * (-1));
                        sendMessage.setText("Вы проиграли:(\nВаше количество монет = " + user.getCoins() + "\nВведите вашу ставку");
                }
                setRate(0);
            }
        }
        try {
            Thread.sleep(1950);
            execute(sendMessage);
        } catch (TelegramApiException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized void sendTextMessage(String chat_id, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);
        sendMessage.setChatId(chat_id);
        sendMessage.setText(message);
        try {
            setButtonsMainMenu(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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

    private static boolean isDigit(String s) throws NumberFormatException {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void setRate(int rate) {
        flRate = false;
        this.rate = rate;
    }

    @Override
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }

    public void setButtonsMainMenu(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = getReplyKeyboardMarkup(sendMessage);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("Получить бонус"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
}
