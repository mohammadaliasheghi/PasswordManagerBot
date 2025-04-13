package com.m2a.common;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class SecurityUtil {

    public static String getCurrentUsername(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public static String getTokenFromCurrentRequest() {
        ServletRequestAttributes currentRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest currentRequest = currentRequestAttributes.getRequest();
        return currentRequest.getHeader(Constant.AUTHORIZATION);
    }
}