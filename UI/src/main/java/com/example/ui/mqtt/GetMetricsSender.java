package com.example.ui.mqtt;

import com.example.ui.services.helpers.GsonService;
import com.google.gson.reflect.TypeToken;
import dtos.MetricsDTO;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class GetMetricsSender {
    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private final GsonService gsonService;

    @Autowired
    public GetMetricsSender(RabbitTemplate rabbitTemplate,
                            @Qualifier("dpMetricsQueueBinding") Binding binding,
                            GsonService gsonService) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
        this.gsonService = gsonService;
    }

    public MetricsDTO getMetrics() {

        Object latestPostProcessTableTennisWrapper = rabbitTemplate.convertSendAndReceive(
                binding.getExchange(), binding.getRoutingKey(), "getMetrics");

        String s = latestPostProcessTableTennisWrapper.toString();

        Type listType = new TypeToken<MetricsDTO>() {
        }.getType();

        return gsonService.getGson().fromJson(s, listType);
    }
}
