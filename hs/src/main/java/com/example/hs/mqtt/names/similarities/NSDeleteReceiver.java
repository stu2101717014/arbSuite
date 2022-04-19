package com.example.hs.mqtt.names.similarities;

import com.example.hs.data.NamesSimilaritiesDAO;
import com.example.hs.services.GsonService;
import com.example.hs.services.NamesSimilaritiesService;
import com.google.gson.reflect.TypeToken;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class NSDeleteReceiver {
    private final Queue qu;

    private final NamesSimilaritiesService namesSimilaritiesService;

    private final GsonService gsonService;

    @Autowired
    public NSDeleteReceiver(@Qualifier("nsdQueue") Queue qu, NamesSimilaritiesService namesSimilaritiesService, GsonService gsonService) {
        this.qu = qu;
        this.namesSimilaritiesService = namesSimilaritiesService;
        this.gsonService = gsonService;
    }

    @RabbitListener(queues = "#{nsdQueue.getName()}")
    public String getNameSimilarity(String message) {
        Type listType = new TypeToken<List<NamesSimilaritiesDAO>>() {
        }.getType();
        List<NamesSimilaritiesDAO> daosList = this.gsonService.getGson().fromJson(message, listType);

        List<NamesSimilaritiesDAO> daos = this.namesSimilaritiesService.deleteNamesSimilarities(daosList);
        return this.gsonService.getGson().toJson(daos);
    }
}
