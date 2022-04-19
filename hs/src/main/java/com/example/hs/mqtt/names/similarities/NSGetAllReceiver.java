package com.example.hs.mqtt.names.similarities;

import com.example.hs.data.NamesSimilaritiesDAO;
import com.example.hs.services.GsonService;
import com.example.hs.services.NamesSimilaritiesService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NSGetAllReceiver {

    private final Queue qu;

    private final NamesSimilaritiesService namesSimilaritiesService;

    private final GsonService gsonService;

    @Autowired
    public NSGetAllReceiver(@Qualifier("nsQueue") Queue qu, NamesSimilaritiesService namesSimilaritiesService, GsonService gsonService) {
        this.qu = qu;
        this.namesSimilaritiesService = namesSimilaritiesService;
        this.gsonService = gsonService;
    }

    @RabbitListener(queues = "#{nsQueue.getName()}")
    public String getMsg() {

        List<NamesSimilaritiesDAO> all = namesSimilaritiesService.getAll();
        return this.gsonService.getGson().toJson(all);
    }
}
