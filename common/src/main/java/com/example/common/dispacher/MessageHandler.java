package com.example.common.dispacher;

import io.netty.channel.Channel;

/**
 * @author: Qbss
 * @date: 2023/10/31
 * @desc:
 */
public interface MessageHandler<T extends Message> {

    /*
    * 执行处理消息
    *
    * @param channel 通道
    * @param message 消息
    * */
    void execute(Channel channel,T message);

    /*
    * @return 消息类型
    * */
    String getType();

}
