package com.example.common.dispacher;

import com.alibaba.fastjson.JSON;
import com.example.common.codec.Invocation;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: Qbss
 * @date: 2023/11/1
 * @desc:
 */
@ChannelHandler.Sharable
public class MessageDispatcher extends SimpleChannelInboundHandler<Invocation> {

    @Autowired
    private MessageHandlerContainer messageHandlerContainer;

    private final ExecutorService executor = Executors.newFixedThreadPool(200);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation msg) throws Exception {
        // 获得 type 对应的 MessageHandler 处理器
        MessageHandler messageHandler = messageHandlerContainer.getMessageHandler(msg.getType());
        // 获得 MessageHandler 处理器的消息类
        Class<? extends Message> messageClass = MessageHandlerContainer.getMessageClass(messageHandler);
        // 解析消息
        Message message = JSON.parseObject(msg.getMessage(), messageClass);
        // 提交到线程池中，进行后续处理
        executor.submit(() -> {
            // 调用 MessageHandler 处理器的 action 方法，处理消息
            messageHandler.execute(ctx.channel(), message);
        });
    }
}
