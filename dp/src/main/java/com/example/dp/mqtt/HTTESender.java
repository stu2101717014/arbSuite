package com.example.dp.mqtt;

import com.example.dp.services.helpers.GsonService;
import dtos.HistoricalTableTennisEventWrapperDTO;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class HTTESender {
    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private final GsonService gsonService;

    @Autowired
    public HTTESender(RabbitTemplate rabbitTemplate,
                      @Qualifier("htteQueueBinding") Binding binding,
                      GsonService gsonService) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
        this.gsonService = gsonService;
    }

    public void sendHistoricalRecords(List<HistoricalTableTennisEventWrapperDTO> historicalTableTennisEventWrapperDTOList) {
        String message = this.gsonService.getGson().toJson(historicalTableTennisEventWrapperDTOList);
        rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);
    }
}
