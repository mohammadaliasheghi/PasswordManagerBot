package com.m2a.common;

import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;

@Component("resourceBundle")
public class ResourceBundle {

    public static String location = "fa"; //LocaleContextHolder.getLocale();

    public ResourceBundle() {
    }

    public String getMessage(String key) {
        try {
            java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("messages", Locale.forLanguageTag(location));
            return resourceBundle.getString(key);
        } catch (MissingResourceException ex) {
            return ex.getMessage();
        }
    }

    public static String getMessageByKey(String key) {
        try {
            java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("messages", Locale.forLanguageTag(location));
            return resourceBundle.getString(key);
        } catch (MissingResourceException ex) {
            return ex.getMessage();
        }
    }

    public static String getMessageByKeyAndParam(String key, String... paramValue) {
        java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("messages", Locale.forLanguageTag(location));
        String msgValue = resourceBundle.getString(key);
        MessageFormat messageFormat = new MessageFormat(msgValue);
        return messageFormat.format(paramValue);
    }
}