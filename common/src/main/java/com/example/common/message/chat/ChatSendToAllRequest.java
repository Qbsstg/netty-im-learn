package com.example.common.message.chat;

import com.example.common.dispacher.Message;
import lombok.Data;

/**
 * @author: Qbss
 * @date: 2023/11/2
 * @desc:
 */
@Data
public class ChatSendToAllRequest implements Message {

    public static final String TYPE = "CHAT_SEND_TO_ALL_REQUEST";

    private String msgId;

    private String content;
}
