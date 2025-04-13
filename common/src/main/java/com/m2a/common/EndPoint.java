package com.m2a.common;

public interface EndPoint {

    //CONTROLLER
    String JWT = "/jwt";
    String SECURITY_INFORMATION = "/api/security-information";
    //END_CONTROLLER

    //API
    String TOKEN = "/token";
    String CREATE = "/create";
    String DELETE = "/delete/{id}";
    //END_API
}
