package com.example.ui.mqtt;

import com.example.ui.services.helpers.GsonService;
import com.google.gson.reflect.TypeToken;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
public class GetPlatformNamesSender {
    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private final GsonService gsonService;

    @Autowired
    public GetPlatformNamesSender(RabbitTemplate rabbitTemplate,
                                  @Qualifier("getPlatformNamesQueueBinding") Binding binding,
                                  GsonService gsonService) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
        this.gsonService = gsonService;
    }

    public List<String> getPlatformNames() {

        // The message sent doesn't matter(empty message produces exception)
        Object latestPostProcessTableTennisWrapper = rabbitTemplate.convertSendAndReceive(
                binding.getExchange(), binding.getRoutingKey(), "getPlatformNames");

        if (latestPostProcessTableTennisWrapper == null) {
            //TODO exception handling and proper implementation for this scenario.
            return new ArrayList<String>();
        }

        String s = latestPostProcessTableTennisWrapper.toString();

        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();

        return gsonService.getGson().fromJson(s, listType);
    }
}
