package com.m2a.bot.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApiResponse {
    private int status;
    private String message;
    private Object data;
}