package com.example.common.message.heartbeat;

import com.example.common.dispacher.Message;

/**
 * @author: Qbss
 * @date: 2023/11/1
 * @desc:
 */
public class HeartbeatResponse implements Message {

    public static final String TYPE = "HEARTBEAT_RESPONSE";

    @Override
    public String toString() {
        return "HeartbeatResponse{}";
    }
}
