package com.example.bwinexperimental.services;

import com.example.bwinexperimental.data.ResultEntity;
import com.example.bwinexperimental.data.ResultEntityRepository;
import com.example.bwinexperimental.data.TableTennisEventEntity;
import com.example.bwinexperimental.data.TableTennisEventEntityRepository;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@EnableScheduling
public class BwinService {

    private static String BWIN_TABLE_TENNIS_REQUEST_URL ="https://cds-api.bwin.com/bettingoffer/fixtures?x-bwin-accessid=NTZiMjk3OGMtNjU5Mi00NjA5LWI2MWItZmU4MDRhN2QxZmEz&lang=en&country=BG&userCountry=BG&fixtureTypes=Standard&state=Latest&offerMapping=Filtered&offerCategories=Gridable&fixtureCategories=Gridable,NonGridable,Other&sportIds=56&regionIds=&competitionIds=&skip=0&take=5000&sortBy=Tags";

    public static final int DELAY = 30000;

    private BwinDataNormalizationServiceImpl dataNormalizationService;

    private HttpServiceImpl httpService;

    private ResultEntityRepository resultEntityRepository;

    private TableTennisEventEntityRepository tableTennisEventEntityRepository;

    private RabbitTemplate rabbitTemplate;

    private Binding binding;

    private static final String PLATFORM_NAME = "BWin";

    @Autowired
    public BwinService(BwinDataNormalizationServiceImpl dataNormalizationService,
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
    public void createAndExecuteTableTennisUserStory(){
        ResultEntity resultEntity = new ResultEntity();

        try {

            Date date = new Date(System.currentTimeMillis());
            resultEntity.setTime(date);

            String responseAsString = this.httpService.getResponseAsString(BWIN_TABLE_TENNIS_REQUEST_URL);
            Map map = this.httpService.mapJSONToMap(responseAsString);
            Set<TableTennisEventEntity> normaliseTableTennisEvents = this.dataNormalizationService.normalise(map);

            resultEntity.setTableTennisEventEntitySet(normaliseTableTennisEvents);

            Set<ResultEntity> ents = new HashSet<>();
            ents.add(resultEntity);

            resultEntity.getTableTennisEventEntitySet().forEach(tte -> tte.setResultEntity(ents));

            this.tableTennisEventEntityRepository.saveAllAndFlush(resultEntity.getTableTennisEventEntitySet());

            this.resultEntityRepository.saveAndFlush(resultEntity);

            String message = httpService.serializeResultEnt(resultEntity);

            Map.Entry<String, String> platformNameResultMessagePair = new AbstractMap.SimpleEntry<>(PLATFORM_NAME, message);

            rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), platformNameResultMessagePair);
        }catch (Exception e){
            resultEntity.setException(e);
        }
    }
}
