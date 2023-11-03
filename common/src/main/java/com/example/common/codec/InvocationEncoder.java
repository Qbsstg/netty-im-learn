package com.example.common.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Qbss
 * @date: 2023/10/31
 * @desc:
 */
@Slf4j
public class InvocationEncoder extends MessageToByteEncoder<Invocation> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Invocation invocation, ByteBuf byteBuf) throws Exception {
        byte[] content = JSON.toJSONBytes(invocation);
        byteBuf.writeInt(content.length);
        byteBuf.writeBytes(content);
        log.info("[encode][连接({}) 编码了一条消息({})]", ctx.channel().id(), invocation.toString());
    }
}
