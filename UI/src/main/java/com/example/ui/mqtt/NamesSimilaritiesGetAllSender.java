package com.example.ui.mqtt;

import com.example.ui.services.helpers.GsonService;
import com.google.gson.reflect.TypeToken;
import dtos.NamesSimilaritiesDTO;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
public class NamesSimilaritiesGetAllSender {

    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private final GsonService gsonService;

    @Autowired
    public NamesSimilaritiesGetAllSender(RabbitTemplate rabbitTemplate,
                                  @Qualifier("nsUIGetAllQueueBinding") Binding binding,
                                  GsonService gsonService) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
        this.gsonService = gsonService;
    }

    public List<NamesSimilaritiesDTO> getAllNamesSimilarities() {

        // The message sent doesn't matter(empty message produces exception)
        Object latestPostProcessTableTennisWrapper = rabbitTemplate.convertSendAndReceive(
                binding.getExchange(), binding.getRoutingKey(), "getAllNamesSimilarities");

        if (latestPostProcessTableTennisWrapper == null) {
            //TODO exception handling and proper implementation for this scenario.
            return new ArrayList<NamesSimilaritiesDTO>();
        }

        String s = latestPostProcessTableTennisWrapper.toString();

        Type listType = new TypeToken<List<NamesSimilaritiesDTO>>() {
        }.getType();

        return gsonService.getGson().fromJson(s, listType);
    }
}
