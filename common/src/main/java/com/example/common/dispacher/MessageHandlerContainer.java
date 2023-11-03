package com.example.common.dispacher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: Qbss
 * @date: 2023/10/31
 * @desc:
 */
@Slf4j
public class MessageHandlerContainer implements InitializingBean {

    private final Map<String, MessageHandler> handlers = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        applicationContext.getBeansOfType(MessageHandler.class)
                .values()
                .forEach(m -> handlers.put(m.getType(), m));
        log.info("[afterPropertiesSet][消息处理器数量：{}]", handlers.size());
    }

    MessageHandler getMessageHandler(String type) {
        MessageHandler handler = handlers.get(type);
        if (handler == null) {
            throw new IllegalArgumentException(String.format("类型(%s) 找不到匹配的 MessageHandler 处理器", type));
        }
        return handler;
    }

    static Class<? extends Message> getMessageClass(MessageHandler handler) {
        // 获得 Bean 对应的 Class 类名。因为有可能被 AOP 代理过。
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(handler);
        // 获得接口的 Type 数组
        Type[] interfaces = targetClass.getGenericInterfaces();
        Class<?> superclass = targetClass.getSuperclass();

        while ((Objects.isNull(interfaces) || 0 == interfaces.length) && Objects.nonNull(superclass)) { // 此处，是以父类的接口为准
            interfaces = superclass.getGenericInterfaces();
            superclass = targetClass.getSuperclass();
        }

        if (Objects.nonNull(interfaces)) {
            // 遍历 Type 数组，找到 Message 接口的子接口
            for (Type type : interfaces) {
                // 判断是否是泛型，并且是否是 MessageHandler 接口的子接口
                if (type instanceof ParameterizedType parameterizedType) {
                    if (Objects.equals(parameterizedType.getRawType(), MessageHandler.class)) {
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        //取首个元素
                        if (Objects.nonNull(actualTypeArguments) && actualTypeArguments.length > 0){
                            return (Class<Message>)actualTypeArguments[0];
                        }else {
                            throw new IllegalStateException(String.format("类型(%s) 获得不到消息类型", handler));
                        }
                    }
                }
            }
        }
        throw new IllegalStateException(String.format("类型(%s) 获得不到消息类型", handler));
    }

}
