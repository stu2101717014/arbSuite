package com.example.dp.mqtt;

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
public class NSSender {

    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private final GsonService gsonService;

    @Autowired
    public NSSender(RabbitTemplate rabbitTemplate,
                    @Qualifier("nsQueueBinding") Binding binding,
                    GsonService gsonService) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
        this.gsonService = gsonService;
    }

    public List<NamesSimilaritiesDTO> getAllNamesSimilarities() {
        // The message sent doesn't matter
        Object get_names_similarities = rabbitTemplate.convertSendAndReceive(binding.getExchange(), binding.getRoutingKey(), "getAllNamesSimilarities");
        if (get_names_similarities != null) {
            String s = get_names_similarities.toString();

            Type listType = new TypeToken<ArrayList<NamesSimilaritiesDTO>>() {
            }.getType();
            return gsonService.getGson().fromJson(s, listType);
        }
        return new ArrayList<>();
    }
}
