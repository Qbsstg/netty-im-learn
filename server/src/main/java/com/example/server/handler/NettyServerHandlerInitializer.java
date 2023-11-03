package com.example.server.handler;

import com.example.common.codec.InvocationDecoder;
import com.example.common.codec.InvocationEncoder;
import com.example.common.dispacher.MessageDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author: Qbss
 * @date: 2023/10/27
 * @desc:
 */
@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {

    /*
     * 心跳超时时间
     * */
    private static final Integer READ_TIMEOUT_SECONDS = 3 * 60;

    @Autowired
    private MessageDispatcher messageDispatcher;

    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Override
    protected void initChannel(Channel ch) {
        // <1> 获得 Channel 对应的 ChannelPipeline
        ChannelPipeline pipeline = ch.pipeline();

        pipeline
                // 空闲检测
                .addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS))
                // 编码器
                .addLast(new InvocationEncoder())
                // 解码器
                .addLast(new InvocationDecoder())
                // 消息分发器
                .addLast(messageDispatcher)
                // 服务端处理器
                .addLast(nettyServerHandler)
        ;
    }
}
