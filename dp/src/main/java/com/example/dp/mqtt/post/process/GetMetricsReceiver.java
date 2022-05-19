package com.example.dp.mqtt.post.process;

import com.example.dp.data.entities.MetricsDAO;
import com.example.dp.services.helpers.GsonService;
import com.example.dp.services.interfaces.TableTennisService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class GetMetricsReceiver {

    private final Queue qu;

    private final GsonService gsonService;

    private final TableTennisService tableTennisService;

    @Autowired
    public GetMetricsReceiver(@Qualifier("dpMetricsQueue") Queue qu, GsonService gsonService, TableTennisService tableTennisService) {
        this.qu = qu;
        this.gsonService = gsonService;
        this.tableTennisService = tableTennisService;
    }


    @RabbitListener(queues = "#{dpMetricsQueue.getName()}")
    public String getNameSimilarity(String message) {
        MetricsDAO metricsDAO = this.tableTennisService.getLastMetrics();
        return this.gsonService.getGson().toJson(metricsDAO);
    }

}
