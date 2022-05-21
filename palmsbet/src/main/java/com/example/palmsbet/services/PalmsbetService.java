package com.example.palmsbet.services;

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
public class PalmsbetService {

    private static String PALMSBET_TABLE_TENNIS_REQUEST_URL ="https://fp.palmsbet.com/api/line/league/142813067/headers?languageId=9";

    public static final int DELAY = 30000;

    private final HttpServiceImpl httpService;

    private final PalmsbetDataNormalizationServiceImpl palmsbetDataNormalizationService;

    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private static final String PLATFORM_NAME = "Palmsbet";

    @Autowired
    public PalmsbetService(RabbitTemplate rabbitTemplate,
                           Binding binding,
                           HttpServiceImpl httpService,
                           PalmsbetDataNormalizationServiceImpl palmsbetDataNormalizationService) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
        this.httpService = httpService;
        this.palmsbetDataNormalizationService = palmsbetDataNormalizationService;
    }

    @Scheduled(fixedDelay = DELAY)
    public void getTableTennisData(){
        ResultEntityDTO resultEntityDTO = new ResultEntityDTO();

        try {

            Date date = new Date(System.currentTimeMillis());
            resultEntityDTO.setTime(date);

            String responseAsString = this.httpService.getResponseAsString(PALMSBET_TABLE_TENNIS_REQUEST_URL);
            Map map = this.httpService.mapJSONToMap(responseAsString);
            Set<TableTennisEventEntityDTO> normaliseTableTennisEvents = this.palmsbetDataNormalizationService.normalise(map);

            resultEntityDTO.setTableTennisEventEntitySet(normaliseTableTennisEvents);

            Set<ResultEntityDTO> ents = new HashSet<>();
            ents.add(resultEntityDTO);

            resultEntityDTO.getTableTennisEventEntitySet().forEach(tte -> tte.setResultEntity(ents));

            resultEntityDTO.setPlatformName(PLATFORM_NAME);

            resultEntityDTO.setFinishedTime(new Date(System.currentTimeMillis()));

            String message = this.httpService.serializeResultEnt(resultEntityDTO);

            rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);
        }catch (Exception e){
            resultEntityDTO.setException(e);
        }
    }
}
