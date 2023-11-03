package com.example.client.controller;

import com.example.client.client.NettyClient;
import com.example.common.codec.Invocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Qbss
 * @date: 2023/11/2
 * @desc:
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private NettyClient nettyClient;

    @PostMapping("/mock")
    public String mock(String type, String message) {
        Invocation invocation = new Invocation(type, message);
        nettyClient.send(invocation);
        return "success";
    }

}
