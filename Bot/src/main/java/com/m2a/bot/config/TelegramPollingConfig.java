package com.m2a.bot.config;

import com.m2a.bot.service.TelegramBotService;
import com.m2a.common.ResourceBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramPollingConfig extends TelegramLongPollingBot {

    private final String botToken;
    private final String botUsername;

    private TelegramBotService telegramBotService;

    @Autowired
    public void setTelegramBotService(TelegramBotService telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    public TelegramPollingConfig(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername) {
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();
            String response;

            // Create keyboard
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            keyboardMarkup.setResizeKeyboard(true);
            List<KeyboardRow> keyboard = new ArrayList<>();

            if (username == null) {
                response = ResourceBundle.getMessageByKey("UsernameNotFound");
                KeyboardRow row = new KeyboardRow();
                row.add("/start");
                keyboard.add(row);
            } else {
                response = telegramBotService
                        .getResponseMessage(update, messageText, keyboard, username);
            }

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(response);
            keyboardMarkup.setKeyboard(keyboard);
            message.setReplyMarkup(keyboardMarkup);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.fillInStackTrace();
            }
        }
    }
}