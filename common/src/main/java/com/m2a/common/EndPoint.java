package com.m2a.common;

public interface EndPoint {

    //CONTROLLER
    String JWT = "/jwt";
    String SECURITY_INFORMATION = "/api/security-information";
    String PASSWORD_MANAGER = "/api/password-manager";
    //END_CONTROLLER

    //API
    String TOKEN = "/token";
    String CREATE = "/create";
    String DELETE = "/delete/{id}";
    String UPDATE = "/update";
    String GET = "/get/{id}";
    String PASSWORD_MANAGER_LIST = "/list/{securityInformationId}";
    //END_API
}
