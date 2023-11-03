package com.example.common.message.chat;

import com.example.common.dispacher.Message;
import lombok.Data;

/**
 * @author: Qbss
 * @date: 2023/11/2
 * @desc:
 */
@Data
public class ChatSendToOneRequest implements Message {

    private static final String TYPE = "CHAT_SEND_TO_ONE_REQUEST";

    /*
     * 发送给的用户
     * */
    private String toUser;

    /*
     * 消息编号
     * */
    private String msgId;

    /*
     * 内容
     * */
    private String content;

}
