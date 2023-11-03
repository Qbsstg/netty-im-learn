package com.example.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: Qbss
 * @date: 2023/10/30
 * @desc:
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private NettyChannelManager channelManager;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 从管理器中添加
        channelManager.add(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        // 从管理器中移除
        channelManager.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Netty Server caught exception{}", ctx.channel().id(), cause);
        // 断开连接
        ctx.channel().close();
    }
}
