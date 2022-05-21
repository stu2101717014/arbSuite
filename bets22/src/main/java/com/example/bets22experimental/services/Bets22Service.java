package com.example.bets22experimental.services;

import dtos.ResultEntityDTO;
import dtos.TableTennisEventEntityDTO;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@EnableScheduling
public class Bets22Service {

    public static final int DELAY = 30000;

    private static final String BETS_22_TABLE_TENNIS_REQUEST_URL = "https://22betz.com/LineFeed/Get1x2_VZip?sports=10&count=5000&lng=en&tf=3000000&tz=2&mode=4&partner=151&getEmpty=true";

    private  static final String PLATFORM_NAME = "22Bet";

    private final Bets22DataNormalizationServiceImpl dataNormalizationService;

    private final HttpServiceImpl httpService;

    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    @Autowired
    public Bets22Service(Bets22DataNormalizationServiceImpl dataNormalizationService,
                         HttpServiceImpl httpService,
                         RabbitTemplate rabbitTemplate,
                         Binding binding) {
        this.dataNormalizationService = dataNormalizationService;
        this.httpService = httpService;
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
    }

    @Scheduled(fixedDelay = DELAY)
    public void getTableTennisData() {
        ResultEntityDTO resultEntityDTO = new ResultEntityDTO();

        try {

            Date date = new Date(System.currentTimeMillis());
            resultEntityDTO.setTime(date);

            String responseAsString = this.httpService.getResponseAsString(BETS_22_TABLE_TENNIS_REQUEST_URL);
            Map map = this.httpService.mapJSONToMap(responseAsString);
            Set<TableTennisEventEntityDTO> normaliseTableTennisEvents = this.dataNormalizationService.normalise(map);

            resultEntityDTO.setTableTennisEventEntitySet(normaliseTableTennisEvents);

            Set<ResultEntityDTO> ents = new HashSet<>();
            ents.add(resultEntityDTO);

            resultEntityDTO.getTableTennisEventEntitySet().forEach(tte -> tte.setResultEntity(ents));

            resultEntityDTO.setPlatformName(PLATFORM_NAME);

            resultEntityDTO.setFinishedTime(new Date(System.currentTimeMillis()));

            String message = this.httpService.serializeResultEnt(resultEntityDTO);

            rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);

        } catch (Exception e) {
            resultEntityDTO.setException(e);
        }
    }
}
