package com.example.bets22experimental.services;

import com.example.bets22experimental.data.ResultEntity;
import com.example.bets22experimental.data.ResultEntityRepository;
import com.example.bets22experimental.data.TableTennisEventEntity;
import com.example.bets22experimental.data.TableTennisEventEntityRepository;
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

    private static String BETS_22_TABLE_TENNIS_REQUEST_URL = "https://22betz.com/LineFeed/Get1x2_VZip?sports=10&count=5000&lng=en&tf=3000000&tz=2&mode=4&partner=151&getEmpty=true";

    private  static final String PLATFORM_NAME = "22Bet";

    private Bets22DataNormalizationServiceImpl dataNormalizationService;

    private HttpServiceImpl httpService;

    private ResultEntityRepository resultEntityRepository;

    private TableTennisEventEntityRepository tableTennisEventEntityRepository;

    private RabbitTemplate rabbitTemplate;

    private Binding binding;

    @Autowired
    public Bets22Service(Bets22DataNormalizationServiceImpl dataNormalizationService,
                         HttpServiceImpl httpService,
                         ResultEntityRepository resultEntityRepository,
                         TableTennisEventEntityRepository tableTennisEventEntityRepository,
                         RabbitTemplate rabbitTemplate,
                         Binding binding) {
        this.dataNormalizationService = dataNormalizationService;
        this.httpService = httpService;
        this.resultEntityRepository = resultEntityRepository;
        this.tableTennisEventEntityRepository = tableTennisEventEntityRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
    }

    @Scheduled(fixedDelay = DELAY)
    public void get22BetsData() {
        ResultEntity resultEntity = new ResultEntity();

        try {

            Date date = new Date(System.currentTimeMillis());
            resultEntity.setTime(date);

            String responseAsString = this.httpService.getResponseAsString(BETS_22_TABLE_TENNIS_REQUEST_URL);
            Map map = this.httpService.mapJSONToMap(responseAsString);
            Set<TableTennisEventEntity> normaliseTableTennisEvents = this.dataNormalizationService.normalise(map);

            resultEntity.setTableTennisEventEntitySet(normaliseTableTennisEvents);

            Set<ResultEntity> ents = new HashSet<>();
            ents.add(resultEntity);

            resultEntity.getTableTennisEventEntitySet().forEach(tte -> tte.setResultEntity(ents));

            this.tableTennisEventEntityRepository.saveAllAndFlush(resultEntity.getTableTennisEventEntitySet());

            this.resultEntityRepository.saveAndFlush(resultEntity);

            String message = httpService.serializeResultEnt(resultEntity);

            Map.Entry<String, String> platformNameResultMessagePair = new AbstractMap.SimpleEntry<String, String>(PLATFORM_NAME, message);

            rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), platformNameResultMessagePair);

        } catch (Exception e) {
            resultEntity.setException(e);
        }



    }
}
