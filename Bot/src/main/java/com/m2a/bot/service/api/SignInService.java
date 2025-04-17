package com.m2a.bot.service.api;

import com.m2a.bot.entity.TempEntity;
import com.m2a.bot.model.ApiResponse;
import com.m2a.bot.model.SecurityInformationModel;
import com.m2a.bot.service.RestTemplateService;
import com.m2a.bot.service.TempService;
import com.m2a.common.EndPoint;
import com.m2a.common.ResourceBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class SignInService {

    private RestTemplateService restTemplateService;
    private TempService tempService;

    @Autowired
    public void setTempService(TempService tempService) {
        this.tempService = tempService;
    }

    @Autowired
    public void setRestTemplateService(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    @Transactional(rollbackFor = Exception.class)
    public String call(String username, String password) {
        TempEntity entity = tempService.findByUsername(username);
        if (entity == null || entity.getId() == null) {
            String message = this.successLogin(username, password, entity);
            if (message != null) return message;
            tempService.create(username, null, "");
            return ResourceBundle.getMessageByKey("IncorrectPassword");
        } else {
            if (entity.getEnableLoginDate() != null
                    && entity.getEnableLoginDate().getTime() > System.currentTimeMillis()) {
                return ResourceBundle.getMessageByKey("IncorrectPasswordV3");
            }
            String message = this.successLogin(username, password, entity);
            if (message != null) return message;
            //after 10 second
            if (entity.getLoginAttempts() >= 3) {
                entity.setLoginAttempts(0);
                entity.setEnableLoginDate(new Date(System.currentTimeMillis() + (1000 * 10)));
                tempService.update(entity);
                return ResourceBundle.getMessageByKey("IncorrectPasswordV2");
            } else {
                entity.setLoginAttempts(entity.getLoginAttempts() + 1);
                entity.setEnableLoginDate(null);
                tempService.update(entity);
            }
        }
        return ResourceBundle.getMessageByKey("IncorrectPassword");
    }

    private String successLogin(String username, String password, TempEntity entity) {
        ApiResponse apiResponse = restTemplateService.callApiWithToken(
                EndPoint.JWT + EndPoint.TOKEN,
                null,
                SecurityInformationModel.builder()
                        .username(username)
                        .password(password)
                        .build(),
                HttpMethod.POST
        );
        if (apiResponse.getStatus() == HttpStatus.OK.value()) {
            if (entity == null || entity.getId() == null) {
                tempService.create(username, (String) apiResponse.getData(), "");
            } else {
                entity.setLoginAttempts(0);
                entity.setEnableLoginDate(null);
                entity.setToken((String) apiResponse.getData());
                tempService.update(entity);
            }
            return ResourceBundle.getMessageByKey("LoginSuccess");
        } else return null;
    }
}
