package com.example.common.message.auth;

import com.example.common.dispacher.Message;
import lombok.Builder;
import lombok.Data;

/**
 * @author: Qbss
 * @date: 2023/11/2
 * @desc:
 */
@Data
public class AuthResponse implements Message {

    public static final String TYPE = "AUTH_RESPONSE";

    private Integer code;

    private String message;

    public AuthResponse setCode(Integer code) {
        this.code = code;
        return this;
    }

    public AuthResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}
