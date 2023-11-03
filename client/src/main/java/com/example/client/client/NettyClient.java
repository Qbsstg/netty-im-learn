package com.example.client.client;

import com.example.client.client.handler.NettyClientHandlerInitializer;
import com.example.common.codec.Invocation;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author: Qbss
 * @date: 2023/10/30
 * @desc:
 */
@Component
@Slf4j
public class NettyClient {

    /*
     * 重连频率，单位：秒
     * */
    private static final Integer RECONNECT_SECONDS = 20;

    @Value("${netty.server.host}")
    private String serverHost;

    @Value("${netty.server.port}")
    private Integer serverPort;

    @Autowired
    private NettyClientHandlerInitializer nettyClientHandlerInitializer;

    private EventLoopGroup eventGroup = new NioEventLoopGroup();

    private volatile Channel channel;

    @PostConstruct
    public void start() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(serverHost, serverPort)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(nettyClientHandlerInitializer);

        bootstrap.connect().addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                log.error("[start][Netty Client 连接服务器({}:{}) 失败]", serverHost, serverPort);
                reconnect();
                return;
            }
            channel = future.channel();
            log.info("[start][Netty Client 连接服务器({}:{}) 成功]", serverHost, serverPort);
        });
    }

    public void reconnect() {
        eventGroup.schedule(() -> {
            log.info("[reconnect][开始重连]");
            try {
                start();
            } catch (InterruptedException e) {
                log.error("[reconnect][重连失败]", e);
            }
        }, RECONNECT_SECONDS, TimeUnit.SECONDS);
        log.info("[reconnect][{} 秒后将发起重连]", RECONNECT_SECONDS);
    }

    @PreDestroy
    public void shutdowm() {
        if (channel != null) {
            channel.close();
        }
        eventGroup.shutdownGracefully();
    }

    public void send(Invocation invocation) {
        if (channel == null) {
            log.error("[send][连接不存在]");
            return;
        }
        if (!channel.isActive()) {
            log.error("[send][连接({})未激活]", channel.id());
            return;
        }
        channel.writeAndFlush(invocation);
    }
}
