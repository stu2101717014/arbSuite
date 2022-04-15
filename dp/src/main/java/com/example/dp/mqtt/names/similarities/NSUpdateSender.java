package com.example.dp.mqtt.names.similarities;

import com.example.dp.services.helpers.GsonService;
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
public class NSUpdateSender {

    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private final GsonService gsonService;

    @Autowired
    public NSUpdateSender(RabbitTemplate rabbitTemplate,
                          @Qualifier("nsuQueueBinding") Binding binding,
                          GsonService gsonService) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
        this.gsonService = gsonService;
    }

    public List<NamesSimilaritiesDTO> saveNamesSimilarities(List<NamesSimilaritiesDTO> namesSimilaritiesDTOList) {

        String message = this.gsonService.getGson().toJson(namesSimilaritiesDTOList);
        Object updatedNamesSimilarities = rabbitTemplate.convertSendAndReceive(binding.getExchange(), binding.getRoutingKey(), message);
        if (updatedNamesSimilarities != null) {
            String s = updatedNamesSimilarities.toString();

            Type listType = new TypeToken<ArrayList<NamesSimilaritiesDTO>>() {
            }.getType();
            return gsonService.getGson().fromJson(s, listType);
        }
        return new ArrayList<>();

    }
}
