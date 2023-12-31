package com.example.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Invocation;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: Qbss
 * @date: 2023/10/30
 * @desc:
 */
@Component
@Slf4j
public class NettyChannelManager {


    /*
     *
     * {@link Channel#attr(AttributeKey)} 的 key，用于获取 Channel 对应的用户
     * */
    private static final AttributeKey<String> CHANNEL_ATTR_KEY_USER = AttributeKey.newInstance("user");

    /*
     *
     * Channel 映射
     * */
    private ConcurrentMap<ChannelId, Channel> channels = new ConcurrentHashMap<>();

    /*
     * 用户与 Channel 的映射
     * 通过它，可以获取用户对应的 Channel 对象。这样，我们就可以向指定用户发送消息。
     * */
    private ConcurrentMap<String, Channel> userChannels = new ConcurrentHashMap<>();

    /*
    * 添加 Channel 到 {@link #channels} 中
    * @param channel Channel
    * */
    public void add(Channel channel) {
        channels.put(channel.id(), channel);
        log.info("[add][一个连接({})加入]", channel.id());
    }

    public void addUser(Channel channel,String user){
        Channel existChannel = channels.get(channel.id());
        if (existChannel== null){
            log.error("[addUser][连接({}) 不存在]", channel.id());
            return;
        }
        // 添加到 userChannels 中
        channel.attr(CHANNEL_ATTR_KEY_USER).set(user);
        // 添加到 userChannels 中
        userChannels.put(user, channel);
    }

    public void remove(Channel channel){
        // 移除 channels 中
        channels.remove(channel.id());
        if (channel.hasAttr(CHANNEL_ATTR_KEY_USER)){
            userChannels.remove(channel.attr(CHANNEL_ATTR_KEY_USER).get());
        }
        log.info("[remove][一个连接({})离开]", channel.id());
    }

    /*
    * 向指定用户发送消息
    *
    * @param user 用户
    * @param invocation 消息体
    * */
    public void send(String user, Invocation invocation){
        Channel channel = userChannels.get(user);
        if (channel == null){
            log.error("[send][连接不存在]");
            return;
        }
        if (!channel.isActive()){
            log.error("[send][连接({})未激活]", channel.id());
            return;
        }
        channel.writeAndFlush(invocation);
    }

    /*
    * 向所有用户发送消息
    * */
    public void sendALL(Invocation invocation){
        channels.values().forEach(channel -> {
            if (!channel.isActive()){
                log.error("[send][连接({})未激活]", channel.id());
                return;
            }
            channel.writeAndFlush(invocation);
        });
    }

}
