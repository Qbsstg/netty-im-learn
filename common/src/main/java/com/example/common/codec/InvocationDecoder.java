package com.example.common.codec;


import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author: Qbss
 * @date: 2023/10/31
 * @desc:
 */
@Slf4j
public class InvocationDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        // 标记当前读取位置
        in.markReaderIndex();
        // 判断是否能够读取length长度
        //此处读取一个四字节，是因为TCP报头中的关于首部信息的长度为四字节
        if (in.readableBytes() <= 4) {
            return;
        }
        //读取长度
        int length = in.readInt();
        if (length < 0) {
            throw new CorruptedFrameException("negative length: " + length);
        }
        // 如果可读长度小于消息长度，恢复读指针，退出。
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        // 读取内容
        byte[] content = new byte[length];
        in.readBytes(content);
        // 转化成 Invocation 对象
        Invocation invocation = JSON.parseObject(content, Invocation.class);
        list.add(invocation);
        log.info("[decode][连接({}) 解析到一条消息({})]", ctx.channel().id(), invocation.toString());
    }
}
