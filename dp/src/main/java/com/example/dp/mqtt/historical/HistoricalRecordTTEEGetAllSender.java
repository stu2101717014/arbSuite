package com.example.dp.mqtt.historical;

import com.example.dp.services.helpers.GsonService;
import com.google.gson.reflect.TypeToken;
import dtos.HistoricalTableTennisEventWrapperDTO;
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
public class HistoricalRecordTTEEGetAllSender {
    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private final GsonService gsonService;

    @Autowired
    public HistoricalRecordTTEEGetAllSender(RabbitTemplate rabbitTemplate,
                                            @Qualifier("htteGetAllQueueBinding") Binding binding,
                                            GsonService gsonService) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
        this.gsonService = gsonService;
    }

    public List<HistoricalTableTennisEventWrapperDTO> getAllHistoricalRecords() {

        // The message sent doesn't matter(empty message produces exception)
        Object getAllHistoricalRecords = rabbitTemplate.convertSendAndReceive(binding.getExchange(), binding.getRoutingKey(), "getAllHistoricalRecords");

        if (getAllHistoricalRecords != null) {
            String s = getAllHistoricalRecords.toString();

            Type listType = new TypeToken<ArrayList<HistoricalTableTennisEventWrapperDTO>>() {
            }.getType();
            return gsonService.getGson().fromJson(s, listType);
        }
        return new ArrayList<>();
    }
}
