package com.example.server.config;

import com.example.common.dispacher.MessageDispatcher;
import com.example.common.dispacher.MessageHandlerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Qbss
 * @date: 2023/11/1
 * @desc:
 */
@Configuration
public class NettyServerConfig {

    @Bean
    public MessageDispatcher messageDispatcher() {
        return new MessageDispatcher();
    }

    @Bean
    public MessageHandlerContainer messageHandlerContainer() {
        return new MessageHandlerContainer();
    }
}
