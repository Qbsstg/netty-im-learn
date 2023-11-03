package com.example.common.message.chat;

import com.example.common.dispacher.Message;
import lombok.Data;

/**
 * @author: Qbss
 * @date: 2023/11/2
 * @desc:
 */
@Data
public class ChatSendResponse implements Message {

    private static final String TYPE = "CHAT_SEND_RESPONSE";

    private String msgId;

    private Integer code;

    private String message;
}
