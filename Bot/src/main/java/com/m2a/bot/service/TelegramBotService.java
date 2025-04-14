package com.m2a.bot.service;

import com.m2a.common.ResourceBundle;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Service
public class TelegramBotService {

    @Transactional(rollbackFor = Exception.class)
    public String getResponseMessage(Update update, String messageText, List<KeyboardRow> keyboard, String username) {
        System.out.println(update);
        System.out.println(username);
        System.out.println(keyboard);
        String response = "";
        if (messageText.equals("/start"))
            response = ResourceBundle.getMessageByKey("WelcomeToTheBot");
        return response;
    }
}
