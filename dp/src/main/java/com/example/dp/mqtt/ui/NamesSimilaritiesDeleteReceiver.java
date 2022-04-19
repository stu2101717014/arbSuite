package com.example.dp.mqtt.ui;

import com.example.dp.services.helpers.GsonService;
import com.example.dp.services.interfaces.NamesSimilaritiesService;
import com.google.gson.reflect.TypeToken;
import dtos.NamesSimilaritiesDTO;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Component
public class NamesSimilaritiesDeleteReceiver {
    private final Queue qu;

    private final GsonService gsonService;

    private final NamesSimilaritiesService namesSimilaritiesService;

    @Autowired
    public NamesSimilaritiesDeleteReceiver(@Qualifier("nsUIDeleteQueue") Queue qu, GsonService gsonService, NamesSimilaritiesService namesSimilaritiesService) {
        this.qu = qu;
        this.gsonService = gsonService;
        this.namesSimilaritiesService = namesSimilaritiesService;
    }

    @RabbitListener(queues = "#{nsUIDeleteQueue.getName()}")
    public String deleteNamesSimilarities(String message) {

        Type listType = new TypeToken<ArrayList<NamesSimilaritiesDTO>>() {
        }.getType();
        ArrayList<NamesSimilaritiesDTO> namesSimilarities = this.gsonService.getGson().fromJson(message, listType);
        List<NamesSimilaritiesDTO> namesSimilaritiesDTOList = this.namesSimilaritiesService.deleteNamesSimilarities(namesSimilarities);
        return this.gsonService.getGson().toJson(namesSimilaritiesDTOList);

    }
}
