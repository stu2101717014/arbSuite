package com.example.ui.mqtt;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TTEEReceiver {

    private final Queue qu;

    public TTEEReceiver(Queue qu) {
        this.qu = qu;
    }

    @RabbitListener(queues = "#{qu.getName()}")
    public void getMsg(final Map.Entry<String, String> platformNameResultMessagePair) {
        System.out.println(platformNameResultMessagePair.getKey());

    }
}
