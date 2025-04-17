package com.m2a.bot.model;

import com.m2a.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PasswordManagerModel extends BaseModel {
    private String title;
    private String username = "-";
    private String password;
    private String description = "-";
    private Long securityInformationId;
}
