package com.m2a.bot.service.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.m2a.bot.entity.TempEntity;
import com.m2a.bot.model.ApiResponse;
import com.m2a.bot.model.PasswordManagerModel;
import com.m2a.bot.service.RestTemplateService;
import com.m2a.bot.service.TempService;
import com.m2a.common.EndPoint;
import com.m2a.common.ResourceBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PasswordManagerService {

    private TempService tempService;
    private RestTemplateService restTemplateService;

    @Autowired
    public void setRestTemplateService(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    @Autowired
    public void setTempService(TempService tempService) {
        this.tempService = tempService;
    }

    @Transactional(rollbackFor = Exception.class)
    public String updateOrCreatePassword(String username, String messageText) {
        messageText = "{" + messageText + "}";
        TempEntity entity = tempService.findByUsername(username);
        if (entity == null || entity.getToken() == null)
            return ResourceBundle.getMessageByKey("TokenUnavailable");
        PasswordManagerModel model;
        try {
            Gson gson = new Gson();
            model = gson.fromJson(messageText, PasswordManagerModel.class);
        } catch (JsonSyntaxException e) {
            e.fillInStackTrace();
            return ResourceBundle.getMessageByKey("JsonObjectNotFound");
        }
        if (model.getTitle() == null || model.getTitle().isEmpty())
            return ResourceBundle.getMessageByKey("TitleNotFound");
        if (model.getPassword() == null || model.getPassword().isEmpty())
            return ResourceBundle.getMessageByKey("PasswordEmpty");
        model.setSecurityInformationId(
                tempService.getSecurityInformationIdByUsername(username)
        );
        if (model.getId() == null)
            return this.createPassword(model, entity);
        else
            return this.updatePassword(model, entity);
    }

    private String createPassword(PasswordManagerModel model, TempEntity entity) {
        ApiResponse apiResponse = restTemplateService.callApiWithToken(
                EndPoint.SECURITY_INFORMATION + EndPoint.CREATE,
                entity.getToken(),
                model,
                HttpMethod.POST
        );
        return (String) apiResponse.getData();
    }

    private String updatePassword(PasswordManagerModel model, TempEntity entity) {
        ApiResponse apiResponse = restTemplateService.callApiWithToken(
                EndPoint.SECURITY_INFORMATION + EndPoint.UPDATE,
                entity.getToken(),
                model,
                HttpMethod.PUT
        );
        return (String) apiResponse.getData();
    }
}
