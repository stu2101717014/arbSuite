package com.example.ui.mqtt;

import com.example.ui.services.helpers.GsonService;

import dtos.PostProcessTableTennisWrapperDTO;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class PPTTWGetLatestSender {

    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private final GsonService gsonService;

    @Autowired
    public PPTTWGetLatestSender(RabbitTemplate rabbitTemplate,
                                @Qualifier("ppttGetLatestQueueBinding") Binding binding,
                                GsonService gsonService) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
        this.gsonService = gsonService;
    }

    public PostProcessTableTennisWrapperDTO getLatestPostProcessTableTennisWrapper() {

        // The message sent doesn't matter(empty message produces exception)
        Object latestPostProcessTableTennisWrapper = rabbitTemplate.convertSendAndReceive(
                binding.getExchange(), binding.getRoutingKey(), "getLatestPostProcessTableTennisWrapper");

        if (latestPostProcessTableTennisWrapper == null) {
            //TODO exception handling and proper implementation for this scenario.
            return new PostProcessTableTennisWrapperDTO();
        }

        String s = latestPostProcessTableTennisWrapper.toString();

        return gsonService.getGson().fromJson(s, PostProcessTableTennisWrapperDTO.class);
    }
}
