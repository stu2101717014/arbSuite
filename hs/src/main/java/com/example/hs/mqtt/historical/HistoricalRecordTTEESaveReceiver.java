package com.example.hs.mqtt.historical;

import com.example.hs.data.HistoricalTableTennisEventWrapperDAO;
import com.example.hs.services.GsonService;
import com.example.hs.services.HistoricalService;
import com.google.gson.reflect.TypeToken;
import dtos.TableTennisEventWrapperDTO;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

@Component
public class HistoricalRecordTTEESaveReceiver {

    private final Queue qu;

    private final GsonService gsonService;

    private final HistoricalService historicalService;

    @Autowired
    public HistoricalRecordTTEESaveReceiver(@Qualifier("htteQueue") Queue qu, GsonService gsonService, HistoricalService historicalService) {
        this.qu = qu;
        this.gsonService = gsonService;
        this.historicalService = historicalService;
    }

    @RabbitListener(queues = "#{htteQueue.getName()}")
    public void persistHistoricalTableTennisEvents(final String message) {

        Type listType = new TypeToken<List<HistoricalTableTennisEventWrapperDAO>>() {
        }.getType();

        List<TableTennisEventWrapperDTO> dtos = this.gsonService.getGson().fromJson(message, listType);

        this.historicalService.persistPositiveArbitrageRecords(dtos);

    }
}
