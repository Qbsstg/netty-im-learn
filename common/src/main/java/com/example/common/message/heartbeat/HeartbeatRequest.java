package com.example.common.message.heartbeat;

import com.example.common.dispacher.Message;

/**
 * @author: Qbss
 * @date: 2023/11/2
 * @desc:
 */
public class HeartbeatRequest implements Message {


    public static final String TYPE = "HEARTBEAT_REQUEST";

    @Override
    public String toString() {
        return "HeartbeatRequest{}";
    }
}
