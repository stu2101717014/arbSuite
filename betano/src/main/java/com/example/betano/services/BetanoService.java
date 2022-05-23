package com.example.betano.services;

import dtos.ResultEntityDTO;
import dtos.TableTennisEventEntityDTO;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BetanoService implements ApplicationRunner {

    private static String BETANO_TABLE_TENNIS_REQUEST_URL = "https://sb2frontend-altenar2.biahosted.com/api/Sportsbook/GetEvents?timezoneOffset=-180&langId=6&skinName=alphawin2&configId=12&culture=bg-bg&countryCode=BG&deviceType=Desktop&numformat=en&integration=alphawin2&sportids=0&categoryids=669%2C962%2C1184&champids=0&group=AllEvents&period=periodall&withLive=false&outrightsDisplay=none&marketTypeIds=&couponType=0";

    private final HttpServiceImpl httpService;

    private final BetanoDataNormalizationServiceImpl betanoDataNormalizationService;

    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private static final String PLATFORM_NAME = "Betano";

    @Autowired
    public BetanoService(RabbitTemplate rabbitTemplate,
                         Binding binding,
                         HttpServiceImpl httpService,
                         BetanoDataNormalizationServiceImpl betanoDataNormalizationService) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
        this.httpService = httpService;
        this.betanoDataNormalizationService = betanoDataNormalizationService;
    }

    @Override
    public void run(ApplicationArguments args) {
        getTableTennisData();
    }

    public void getTableTennisData() {
        ResultEntityDTO resultEntityDTO = new ResultEntityDTO();

        try {

            resultEntityDTO.setTime(new Date(System.currentTimeMillis()));

            String responseAsString = this.httpService.getResponseAsString(BETANO_TABLE_TENNIS_REQUEST_URL);
            Map map = this.httpService.mapJSONToMap(responseAsString);
            Set<TableTennisEventEntityDTO> normaliseTableTennisEvents = this.betanoDataNormalizationService.normalise(map);

            resultEntityDTO.setTableTennisEventEntitySet(normaliseTableTennisEvents);

            Set<ResultEntityDTO> ents = new HashSet<>();
            ents.add(resultEntityDTO);

            resultEntityDTO.getTableTennisEventEntitySet().forEach(tte -> tte.setResultEntity(ents));

            resultEntityDTO.setPlatformName(PLATFORM_NAME);

            resultEntityDTO.setFinishedTime(new Date(System.currentTimeMillis()));

            String message = this.httpService.serializeResultEnt(resultEntityDTO);

            rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);

            System.exit(0);
        } catch (Exception e) {
            resultEntityDTO.setException(e);
        }
    }
}
