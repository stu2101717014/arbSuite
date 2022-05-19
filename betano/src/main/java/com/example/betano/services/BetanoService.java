package com.example.betano.services;

import dtos.ResultEntityDTO;
import dtos.TableTennisEventEntityDTO;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@EnableScheduling
public class BetanoService {

    private static String BETANO_TABLE_TENNIS_REQUEST_URL ="https://sb2frontend-altenar2.biahosted.com/api/Sportsbook/GetEvents?timezoneOffset=-180&langId=6&skinName=alphawin2&configId=12&culture=bg-bg&countryCode=BG&deviceType=Desktop&numformat=en&integration=alphawin2&sportids=0&categoryids=669%2C962%2C1184&champids=0&group=AllEvents&period=periodall&withLive=false&outrightsDisplay=none&marketTypeIds=&couponType=0";

    public static final int DELAY = 30000;

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

    @Scheduled(fixedDelay = DELAY)
    public void getTableTennisData(){
        ResultEntityDTO resultEntityDTO = new ResultEntityDTO();

        try {

            Date date = new Date(System.currentTimeMillis());
            resultEntityDTO.setTime(date);

            String responseAsString = this.httpService.getResponseAsString(BETANO_TABLE_TENNIS_REQUEST_URL);
            Map map = this.httpService.mapJSONToMap(responseAsString);
            Set<TableTennisEventEntityDTO> normaliseTableTennisEvents = this.betanoDataNormalizationService.normalise(map);

            resultEntityDTO.setTableTennisEventEntitySet(normaliseTableTennisEvents);

            Set<ResultEntityDTO> ents = new HashSet<>();
            ents.add(resultEntityDTO);

            resultEntityDTO.getTableTennisEventEntitySet().forEach(tte -> tte.setResultEntity(ents));

            resultEntityDTO.setPlatformName(PLATFORM_NAME);

            String message = this.httpService.serializeResultEnt(resultEntityDTO);

            rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);
        }catch (Exception e){
            resultEntityDTO.setException(e);
        }
    }
}
