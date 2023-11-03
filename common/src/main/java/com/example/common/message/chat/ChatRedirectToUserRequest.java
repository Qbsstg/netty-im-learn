package com.example.common.message.chat;

import com.example.common.dispacher.Message;
import lombok.Data;

/**
 * @author: Qbss
 * @date: 2023/11/2
 * @desc:
 */
@Data
public class ChatRedirectToUserRequest implements Message {

    public static final String TYPE = "CHAT_REDIRECT_TO_USER_REQUEST";

    private String msgId;

    private String content;

    private String fromUser;

}
