package com.example.dp.mqtt.ui;

import com.example.dp.data.entities.PostProcessTableTennisWrapperDAO;
import com.example.dp.services.helpers.GsonService;
import com.example.dp.services.interfaces.NamesSimilaritiesService;
import com.example.dp.services.interfaces.TableTennisService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class NamesSimilaritiesGetAllReceiver {
    private final Queue qu;

    private final GsonService gsonService;

    private final NamesSimilaritiesService namesSimilaritiesService;

    @Autowired
    public NamesSimilaritiesGetAllReceiver(@Qualifier("nsUIGetAllQueue") Queue qu, GsonService gsonService, NamesSimilaritiesService namesSimilaritiesService) {
        this.qu = qu;
        this.gsonService = gsonService;
        this.namesSimilaritiesService = namesSimilaritiesService;
    }

    @RabbitListener(queues = "#{nsUIGetAllQueue.getName()}")
    public String getAllNamesSimilarities(String message) {
        return this.gsonService.getGson().toJson(this.namesSimilaritiesService.getAll());
    }
}
