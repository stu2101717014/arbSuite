package org.example.commons.bwin.services;

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
public class BwinService {

    private static String BWIN_TABLE_TENNIS_REQUEST_URL ="https://cds-api.bwin.com/bettingoffer/fixtures?x-bwin-accessid=NTZiMjk3OGMtNjU5Mi00NjA5LWI2MWItZmU4MDRhN2QxZmEz&lang=en&country=BG&userCountry=BG&fixtureTypes=Standard&state=Latest&offerMapping=Filtered&offerCategories=Gridable&fixtureCategories=Gridable,NonGridable,Other&sportIds=56&regionIds=&competitionIds=&skip=0&take=5000&sortBy=Tags";

    private DataNormalizationService dataNormalizationService;

    private HttpService httpService;

    private ResultEntityRepository resultEntityRepository;

    private TableTennisEventEntityRepository tableTennisEventEntityRepository;

    @Autowired
    public BwinService(DataNormalizationService dataNormalizationService, HttpService httpService, ResultEntityRepository resultEntityRepository, TableTennisEventEntityRepository tableTennisEventEntityRepository) {
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

            String responseAsString = this.httpService.getResponseAsString(BWIN_TABLE_TENNIS_REQUEST_URL);
            Map map = this.httpService.mapJSONToMap(responseAsString);
            Set<TableTennisEventEntity> normaliseTableTennisEvents = this.dataNormalizationService.normalise(map);

            resultEntity.setTableTennisEventEntitySet(normaliseTableTennisEvents);

            Set<ResultEntity> ents = new HashSet<>();
            ents.add(resultEntity);

            resultEntity.getTableTennisEventEntitySet().forEach(tte -> tte.setResultEntity(ents));

            this.tableTennisEventEntityRepository.saveAllAndFlush(resultEntity.getTableTennisEventEntitySet());

            this.resultEntityRepository.saveAndFlush(resultEntity);

            return httpService.serializeResultEnt(resultEntity);
        }catch (Exception e){
            resultEntity.setException(e);
        }

        return httpService.serializeResultEnt(resultEntity);
    }
}
