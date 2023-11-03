package com.example.server.messagehandler.auth;

import com.example.common.codec.Invocation;
import com.example.common.dispacher.MessageHandler;
import com.example.common.message.auth.AuthRequest;
import com.example.common.message.auth.AuthResponse;
import com.example.server.handler.NettyChannelManager;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author: Qbss
 * @date: 2023/11/2
 * @desc:
 */
@Component
public class AuthRequestHandler implements MessageHandler<AuthRequest> {

    @Autowired
    private NettyChannelManager nettyChannelManager;

    @Override
    public void execute(Channel channel, AuthRequest message) {
        if (!StringUtils.hasLength(message.getAccessToken())){
            AuthResponse authResponse = new AuthResponse().setCode(1).setMessage("认证 accessToken 未传入");
            channel.writeAndFlush(new Invocation(AuthResponse.TYPE, authResponse));
            return;
        }

        nettyChannelManager.addUser(channel, message.getAccessToken());

        AuthResponse authResponse = new AuthResponse().setCode(0);
        channel.writeAndFlush(new Invocation(AuthResponse.TYPE, authResponse));
    }

    @Override
    public String getType() {
        return AuthRequest.TYPE;
    }
}
