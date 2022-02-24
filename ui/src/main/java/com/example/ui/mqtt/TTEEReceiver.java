package com.example.ui.mqtt;


import com.example.ui.entities.jpa.ResultEntityDAO;
import com.example.ui.services.DataReceiverService;
import com.google.gson.Gson;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TTEEReceiver {

    private final Queue qu;

    private final DataReceiverService dataReceiverService;

    @Autowired
    public TTEEReceiver(Queue qu, DataReceiverService dataReceiverService) {
        this.qu = qu;
        this.dataReceiverService = dataReceiverService;
    }

    @RabbitListener(queues = "#{qu.getName()}")
    public void getMsg(final String resultEntAsString) {
        try {
            ResultEntityDAO resultEntity = new Gson().fromJson(resultEntAsString, ResultEntityDAO.class);

            this.dataReceiverService.persistResultEntity(resultEntity);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
