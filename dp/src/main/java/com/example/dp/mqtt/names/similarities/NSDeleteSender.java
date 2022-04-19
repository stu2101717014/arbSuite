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
public class NSDeleteSender {

    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private final GsonService gsonService;

    @Autowired
    public NSDeleteSender(RabbitTemplate rabbitTemplate,
                          @Qualifier("nsdQueueBinding") Binding binding,
                          GsonService gsonService) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
        this.gsonService = gsonService;
    }

    public List<NamesSimilaritiesDTO> deleteNamesSimilarities(List<NamesSimilaritiesDTO> namesSimilaritiesDTOList) {

        String message = this.gsonService.getGson().toJson(namesSimilaritiesDTOList);
        Object namesSimilarities = rabbitTemplate.convertSendAndReceive(binding.getExchange(), binding.getRoutingKey(), message);

        if (namesSimilarities != null) {
            String s = namesSimilarities.toString();

            Type listType = new TypeToken<ArrayList<NamesSimilaritiesDTO>>() {
            }.getType();
            return gsonService.getGson().fromJson(s, listType);
        }
        return new ArrayList<>();
    }
}
