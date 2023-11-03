package com.example.client.client.handler;

import com.example.common.codec.InvocationDecoder;
import com.example.common.codec.InvocationEncoder;
import com.example.common.dispacher.MessageDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Qbss
 * @date: 2023/10/30
 * @desc:
 */
@Component
public class NettyClientHandlerInitializer extends ChannelInitializer<Channel> {

    private static final Integer READ_TIMEOUT_SECONDS = 60;

    @Autowired
    private MessageDispatcher messageDispatcher;

    @Autowired
    private NettyClientHandler nettyClientHandler;


    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                .addLast(new IdleStateHandler(READ_TIMEOUT_SECONDS,0,0))
                .addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS*3))
                .addLast(new InvocationEncoder())
                .addLast(new InvocationDecoder())
                .addLast(messageDispatcher)
                .addLast(nettyClientHandler);
    }
}
