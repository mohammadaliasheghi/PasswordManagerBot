package com.m2a.web.config;

import com.m2a.web.entity.SecurityInformationEntity;
import com.m2a.web.service.SecurityInformationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtConfig {

    @Value("${security.password.secret-key}")
    private String SECRET_KEY;

    private SecurityInformationService securityInformationService;

    @Autowired
    public void setUsersService(SecurityInformationService securityInformationService) {
        this.securityInformationService = securityInformationService;
    }

    public String generateToken(String username) {
        SecurityInformationEntity sie = securityInformationService.loadUserByUsername(username);
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .claim("id", sie.getId())
                .claim("enabled", sie.isEnabled())
                .claim("username", sie.getUsername())
                .claim("authorities", sie.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + (1000 * 60 * 10)))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return false;
    }
}