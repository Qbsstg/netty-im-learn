package com.example.server.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author: Qbss
 * @date: 2023/10/27
 * @desc:
 */
@Component
@Slf4j
public class NettyServer {

    @Value("${netty.port}")
    private Integer port;

    @Autowired
    private NettyServerHandlerInitializer nettyServerHandlerInitializer;

    //boss线程组，用于处理连接工作
    private EventLoopGroup bossGroup = new NioEventLoopGroup();

    //work线程组，用于数据处理
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    private Channel channel;

    @PostConstruct
    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .option(ChannelOption.SO_BACKLOG,1024)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childOption(ChannelOption.TCP_NODELAY,true)
                .childHandler(nettyServerHandlerInitializer);

        ChannelFuture future = bootstrap.bind().sync();
        if (future.isSuccess()){
            channel = future.channel();
            log.info("启动 Netty Server");
        }
    }

    @PreDestroy
    public void shutdown(){
        if (channel != null){
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }


}
