package com.example.common.codec;

import com.alibaba.fastjson.JSON;
import com.example.common.dispacher.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Qbss
 * @date: 2023/10/30
 * @desc:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invocation {

    private String type;

    private String message;

    public Invocation(String type, Message message) {
        this.type = type;
        this.message = JSON.toJSONString(message);
    }

}
