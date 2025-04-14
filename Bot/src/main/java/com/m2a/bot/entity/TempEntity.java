package com.m2a.bot.entity;

import com.m2a.common.BasePO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "temp")
public class TempEntity extends BasePO {

    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "token")
    private String token;
    @Column(name = "state")
    private String state;
    @Column(name = "login_attempts")
    private Integer loginAttempts = 0;
    @Column(name = "enable_login_date")
    private Date enableLoginDate;

    public TempEntity() {
    }

    public TempEntity(String username,
                      String token,
                      String state,
                      Integer loginAttempts,
                      Date enableLoginDate) {
        this.username = username;
        this.token = token;
        this.state = state;
        this.loginAttempts = loginAttempts;
        this.enableLoginDate = enableLoginDate;
    }
}
