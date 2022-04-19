package com.example.dp.mqtt.post.process;

import com.example.dp.data.entities.PostProcessTableTennisWrapperDAO;
import com.example.dp.services.helpers.GsonService;
import com.example.dp.services.interfaces.TableTennisService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class GetPlatformNamesReceiver {

    private final Queue qu;

    private final GsonService gsonService;

    private final TableTennisService tableTennisService;

    @Autowired
    public GetPlatformNamesReceiver(@Qualifier("getPlatformNamesQueue") Queue qu, GsonService gsonService, TableTennisService tableTennisService) {
        this.qu = qu;
        this.gsonService = gsonService;
        this.tableTennisService = tableTennisService;
    }

    @RabbitListener(queues = "#{getPlatformNamesQueue.getName()}")
    public String getNameSimilarity(String message) {
        List<String> platformNames = this.tableTennisService.getPlatformNames();
        return this.gsonService.getGson().toJson(platformNames);
    }
}
