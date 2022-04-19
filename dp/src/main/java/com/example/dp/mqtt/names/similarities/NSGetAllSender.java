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
public class NSGetAllSender {

    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private final GsonService gsonService;

    @Autowired
    public NSGetAllSender(RabbitTemplate rabbitTemplate,
                          @Qualifier("nsQueueBinding") Binding binding,
                          GsonService gsonService) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
        this.gsonService = gsonService;
    }

    public List<NamesSimilaritiesDTO> getAllNamesSimilarities() {

        // The message sent doesn't matter(empty message produces exception)
        Object getNamesSimilarities = rabbitTemplate.convertSendAndReceive(binding.getExchange(), binding.getRoutingKey(), "getAllNamesSimilarities");
        if (getNamesSimilarities != null) {
            String s = getNamesSimilarities.toString();

            Type listType = new TypeToken<ArrayList<NamesSimilaritiesDTO>>() {
            }.getType();
            return gsonService.getGson().fromJson(s, listType);
        }
        return new ArrayList<>();
    }

}
