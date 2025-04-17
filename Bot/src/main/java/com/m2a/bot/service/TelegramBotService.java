package com.m2a.bot.service;

import com.m2a.bot.service.api.PasswordManagerService;
import com.m2a.bot.service.api.SignInService;
import com.m2a.bot.service.api.SignUpService;
import com.m2a.common.ResourceBundle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBotService {

    private final SignUpService signUpService;
    private final SignInService signInService;
    private final PasswordManagerService passwordManagerService;

    @Transactional(rollbackFor = Exception.class)
    public String getResponseMessage(Update update, String messageText, List<KeyboardRow> keyboard, String username) {
        System.out.println(update);
        System.out.println(username);
        System.out.println(keyboard);
        String response = "";
        if (messageText.equals("/start"))
            response = ResourceBundle.getMessageByKey("WelcomeToTheBot");
        if (messageText.equals("/signup"))
            response = signUpService.call(username, messageText);
        if (messageText.equals("/login"))
            response = signInService.call(username, messageText);
        if (messageText.equals("/createPassword"))
            response = passwordManagerService.updateOrCreatePassword(username, messageText);
        if (messageText.equals("/updatePassword"))
            response = passwordManagerService.updateOrCreatePassword(username, messageText);
        return response;
    }
}
