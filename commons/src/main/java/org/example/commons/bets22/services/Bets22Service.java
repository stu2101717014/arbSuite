package org.example.commons.bets22.services;

import org.example.commons.entities.ResultEntity;
import org.example.commons.entities.TableTennisEventEntity;
import org.example.commons.repos.ResultEntityRepository;
import org.example.commons.repos.TableTennisEventEntityRepository;
import org.example.commons.services.DataNormalizationService;
import org.example.commons.services.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class Bets22Service {

    private static String BETS_22_TABLE_TENNIS_REQUEST_URL ="https://22bets.me/LineFeed/Get1x2_VZip?sports=10&count=5000&lng=en&tf=3000000&tz=2&mode=4&partner=151&getEmpty=true";

    private DataNormalizationService dataNormalizationService;

    private HttpService httpService;

    private ResultEntityRepository resultEntityRepository;

    private TableTennisEventEntityRepository tableTennisEventEntityRepository;

    @Autowired
    public Bets22Service(DataNormalizationService dataNormalizationService, HttpService httpService, ResultEntityRepository resultEntityRepository, TableTennisEventEntityRepository tableTennisEventEntityRepository) {
        this.dataNormalizationService = dataNormalizationService;
        this.httpService = httpService;
        this.resultEntityRepository = resultEntityRepository;
        this.tableTennisEventEntityRepository = tableTennisEventEntityRepository;
    }

    public String createAndExecuteTableTennisUserStory(){
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


        }catch (Exception e){
            resultEntity.setException(e);
        }

        return httpService.serializeResultEnt(resultEntity);
    }
}