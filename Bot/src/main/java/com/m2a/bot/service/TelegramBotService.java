package com.m2a.bot.service;

import com.m2a.bot.model.ApiResponse;
import com.m2a.bot.model.SecurityInformationModel;
import com.m2a.common.EndPoint;
import com.m2a.common.ResourceBundle;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBotService {

    private final RestTemplateService restTemplateService;

    @Transactional(rollbackFor = Exception.class)
    public String getResponseMessage(Update update, String messageText, List<KeyboardRow> keyboard, String username) {
        System.out.println(update);
        System.out.println(username);
        System.out.println(keyboard);
        String response = "";
        if (messageText.equals("/start"))
            response = ResourceBundle.getMessageByKey("WelcomeToTheBot");
        if (messageText.equals("/signup"))
            response = this.signUp(username, messageText);
        return response;
    }

    private String signUp(String username, String password) {
        ApiResponse apiResponse = restTemplateService.callApiWithToken(
                EndPoint.JWT + EndPoint.CREATE,
                null,
                SecurityInformationModel.builder()
                        .username(username)
                        .password(password)
                        .build(),
                HttpMethod.POST
        );
        return (String) apiResponse.getData();
    }
}
