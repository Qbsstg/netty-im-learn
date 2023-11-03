package com.example.common.message.auth;

import com.example.common.dispacher.Message;

/**
 * @author: Qbss
 * @date: 2023/11/2
 * @desc:
 */
public class AuthRequest implements Message {

    public static final String TYPE = "AUTH_REQUEST";

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public AuthRequest setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    @Override
    public String toString() {
        return "AuthRequest{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
