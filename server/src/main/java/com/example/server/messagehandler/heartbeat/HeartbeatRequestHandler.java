package com.example.server.messagehandler.heartbeat;

import com.example.common.dispacher.Message;
import com.example.common.dispacher.MessageHandler;
import com.example.common.message.heartbeat.HeartbeatRequest;
import com.example.common.message.heartbeat.HeartbeatResponse;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: Qbss
 * @date: 2023/11/1
 * @desc:
 */
@Slf4j
@Component
public class HeartbeatRequestHandler implements MessageHandler<Message> {
    @Override
    public void execute(Channel channel, Message message) {
        log.info("[execute][收到连接({}) 的心跳请求]", channel.id());
        HeartbeatResponse heartbeatResponse = new HeartbeatResponse();
        channel.writeAndFlush(heartbeatResponse);
    }

    @Override
    public String getType() {
        return HeartbeatRequest.TYPE;
    }
}
