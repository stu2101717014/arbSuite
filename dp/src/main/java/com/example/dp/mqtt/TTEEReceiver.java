package com.example.dp.mqtt;

import com.example.dp.data.entities.ResultEntityDAO;
import com.example.dp.services.helpers.GsonService;
import com.example.dp.services.interfaces.RawDataReceiverService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TTEEReceiver {

    public final Queue qu;

    private final RawDataReceiverService rawDataReceiverService;

    private final GsonService gsonService;

    @Autowired
    public TTEEReceiver(@Qualifier("tteeQueue") Queue qu, RawDataReceiverService rawDataReceiverService, GsonService gsonService) {
        this.qu = qu;
        this.rawDataReceiverService = rawDataReceiverService;
        this.gsonService = gsonService;
    }

    @RabbitListener(queues = "#{tteeQueue.getName()}")
    public void getMsg(final String resultEntAsString) {
        try {
            ResultEntityDAO resultEntity = this.gsonService.getGson().fromJson(resultEntAsString, ResultEntityDAO.class);

            if (resultEntity != null && resultEntity.getTableTennisEventEntitySet() != null) {
                this.rawDataReceiverService.persistResultEntity(resultEntity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

