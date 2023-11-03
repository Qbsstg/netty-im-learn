package com.example.client.messagehandler.auth;

import com.example.common.dispacher.MessageHandler;
import com.example.common.message.auth.AuthResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: Qbss
 * @date: 2023/11/2
 * @desc:
 */
@Component
@Slf4j
public class AuthResponseHandler implements MessageHandler<AuthResponse> {
    @Override
    public void execute(Channel channel, AuthResponse message) {
        log.info("[execute][认证结果：{}]", message);
    }

    @Override
    public String getType() {
        return AuthResponse.TYPE;
    }
}
