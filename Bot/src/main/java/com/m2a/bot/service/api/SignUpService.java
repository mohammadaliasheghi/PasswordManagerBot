package com.m2a.bot.service.api;

import com.m2a.bot.model.ApiResponse;
import com.m2a.bot.model.SecurityInformationModel;
import com.m2a.bot.service.RestTemplateService;
import com.m2a.common.EndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignUpService {

    private RestTemplateService restTemplateService;

    @Autowired
    public void setRestTemplateService(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    @Transactional(rollbackFor = Exception.class)
    public String call(String username, String password) {
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
