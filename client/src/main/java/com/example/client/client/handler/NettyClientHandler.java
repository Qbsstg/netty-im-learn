package com.example.client.client.handler;

import com.example.client.client.NettyClient;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Invocation;
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
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private NettyClient nettyClient;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        nettyClient.reconnect();
        super.channelInactive(ctx);
    }

    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause){
        log.error("[exceptionCaught][连接({}) 发生异常]",ctx.channel().id(),cause);
        ctx.channel().close();
    }

    public void userEventTriggered(ChannelHandlerContext ctx,Object event) throws Exception{
        if (event instanceof IdleStateEvent){
            log.info("[userEventTriggered][发生(空闲事件)]");
            //HeartbeatRequest heartbeatRequest = new HeartbeatRequest();
            //ctx.writeAndFlush(new Invocation(HeartbeatRequest.TYPE,heartbeatRequest))
                    //.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }else {
            super.userEventTriggered(ctx,event);
        }
    }



}
