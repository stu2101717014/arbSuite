package com.example.ui.services;


import com.example.ui.entities.helpers.*;
import com.example.ui.entities.jpa.PlatformDataRequestWrapperEntity;
import com.example.ui.repos.NamesSimilaritiesRepository;
import com.example.ui.services.interfaces.DataRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TableTennisService {

    private DataRequestService dataRequestService;

    private RequestDataResult lastRequestedData;


    @Autowired
    public TableTennisService(DataRequestService dataRequestService, NamesSimilaritiesRepository namesSimilaritiesRepository) {
        this.dataRequestService = dataRequestService;
    }

    public List<TableTennisEventWrapper> getData() {
        try {
            lastRequestedData = new RequestDataResult();

            ArrayList<PlatformDataRequestWrapperEntity> platformWrapperEntities = getPlatformDataRequestWrapperEntities();
            lastRequestedData = dataRequestService.requestData(lastRequestedData, platformWrapperEntities);

            List<TableTennisEventWrapper> eventWrapperList = reshapeTableTennisEventsData(lastRequestedData);


            return eventWrapperList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<TableTennisEventWrapper> reshapeTableTennisEventsData(RequestDataResult requestDataResult) {

        HashMap<TableTennisEventEntityShort, TableTennisEventWrapper> resultMap = new HashMap<>();
        List<TableTennisEventWrapper> resultAsList = new ArrayList<>();

        try {
            List<ResultEntity> entityList = requestDataResult.getEntityList();

            for (ResultEntity re : entityList) {

                Set<TableTennisEventEntity> tableTennisEventEntitySet = re.getTableTennisEventEntitySet();

                for (TableTennisEventEntity tableTennisEventEntity : tableTennisEventEntitySet) {

                    TableTennisEventEntityShort temporaryShort = new TableTennisEventEntityShort();
                    temporaryShort.setFirstPlayer(tableTennisEventEntity.getFirstPlayerName());
                    temporaryShort.setSecondPlayer(tableTennisEventEntity.getSecondPlayerName());
                    temporaryShort.setEventDate(tableTennisEventEntity.getEventDate());

                    if (resultMap.containsKey(temporaryShort)) {
                        resultMap.get(temporaryShort).getEventEntityMap().put(re.getPlatformName(), tableTennisEventEntity);
                    } else {
                        TableTennisEventWrapper tableTennisEventWrapper = new TableTennisEventWrapper();
                        tableTennisEventWrapper.setTableTennisEventEntityShort(temporaryShort);
                        tableTennisEventWrapper.setEventEntityMap(new HashMap<>());
                        tableTennisEventWrapper.getEventEntityMap().put(re.getPlatformName(), tableTennisEventEntity);
                        resultMap.put(temporaryShort, tableTennisEventWrapper);
                    }
                }
            }

            resultAsList = new ArrayList<>(resultMap.values());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return resultAsList.stream().sorted(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getEventDate()))
                .sorted(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getSecondPlayer()))
                .sorted(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getFirstPlayer()))
                .collect(Collectors.toList());
    }


    private ArrayList<PlatformDataRequestWrapperEntity> getPlatformDataRequestWrapperEntities() {
        //TODO : PlatformDataRequestWrapperEntity must has its own UI and must be persisted in the UI db
        PlatformDataRequestWrapperEntity platformDataRequestWrapperEntity = new PlatformDataRequestWrapperEntity();
        platformDataRequestWrapperEntity.setPlatformName("BWin");
        platformDataRequestWrapperEntity.setUrl("http://localhost:8084/api/bwin");
        platformDataRequestWrapperEntity.setAccessible(true);

        PlatformDataRequestWrapperEntity platformDataRequestWrapperEntity2 = new PlatformDataRequestWrapperEntity();
        platformDataRequestWrapperEntity2.setPlatformName("BetWinner");
        platformDataRequestWrapperEntity2.setUrl("http://localhost:8085/api/betwinner");
        platformDataRequestWrapperEntity2.setAccessible(true);

        PlatformDataRequestWrapperEntity platformDataRequestWrapperEntity3 = new PlatformDataRequestWrapperEntity();
        platformDataRequestWrapperEntity3.setPlatformName("22Bet");
        platformDataRequestWrapperEntity3.setUrl("http://localhost:8082/api/22bets");
        platformDataRequestWrapperEntity3.setAccessible(true);

        PlatformDataRequestWrapperEntity platformDataRequestWrapperEntity4 = new PlatformDataRequestWrapperEntity();
        platformDataRequestWrapperEntity4.setPlatformName("WilliamHill");
        platformDataRequestWrapperEntity4.setUrl("http://localhost:8083/api/williamhill");
        platformDataRequestWrapperEntity4.setAccessible(true);

        ArrayList<PlatformDataRequestWrapperEntity> platformWrapperEntities = new ArrayList<>();
        platformWrapperEntities.add(platformDataRequestWrapperEntity);
        platformWrapperEntities.add(platformDataRequestWrapperEntity2);
        platformWrapperEntities.add(platformDataRequestWrapperEntity3);
        platformWrapperEntities.add(platformDataRequestWrapperEntity4);
        return platformWrapperEntities;
    }

    public List<String> getPlatformNames() {
        List<String> resList = new ArrayList<>();
        resList.add("BWin");
        resList.add("BetWinner");
        resList.add("22Bet");
        resList.add("WilliamHill");

        return resList;
    }
}
