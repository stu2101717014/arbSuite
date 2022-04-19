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
public class NamesSimilaritiesDeleteSender {

    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private final GsonService gsonService;

    @Autowired
    public NamesSimilaritiesDeleteSender(RabbitTemplate rabbitTemplate,
                                         @Qualifier("nsUIDeleteQueueBinding") Binding binding,
                                         GsonService gsonService) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
        this.gsonService = gsonService;
    }

    public List<NamesSimilaritiesDTO> deleteNamesSimilarities(List<NamesSimilaritiesDTO> namesSimilaritiesDTOList) {

        String message = this.gsonService.getGson().toJson(namesSimilaritiesDTOList);

        Object namesSimilarities = rabbitTemplate.convertSendAndReceive(binding.getExchange(), binding.getRoutingKey(), message);

        if (namesSimilarities == null) {
            //TODO exception handling and proper implementation for this scenario.
            return new ArrayList<NamesSimilaritiesDTO>();
        }

        String s = namesSimilarities.toString();

        Type listType = new TypeToken<List<NamesSimilaritiesDTO>>() {
        }.getType();

        return gsonService.getGson().fromJson(s, listType);
    }
}
