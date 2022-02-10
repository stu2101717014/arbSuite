package com.example.ui.services;

import com.example.ui.entities.helpers.*;
import com.example.ui.entities.jpa.PlatformDataRequestWrapperEntity;
import com.example.ui.services.interfaces.DataRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

@Service
public class TableTennisService {

    private DataRequestService dataRequestService;

    private RequestDataResult lastRequestedData;

    private ApisService apisService;

    @Autowired
    public TableTennisService(DataRequestService dataRequestService, ApisService apisService) {
        this.dataRequestService = dataRequestService;
        this.apisService = apisService;
    }

    public List<TableTennisEventWrapper> getData() {
        try {
            lastRequestedData = new RequestDataResult();

            List<PlatformDataRequestWrapperEntity> platformWrapperEntities = getPlatformDataRequestWrapperEntities();
            lastRequestedData = dataRequestService.requestData(lastRequestedData, platformWrapperEntities);

            List<TableTennisEventWrapper> eventWrapperList = reshapeTableTennisEventsData(lastRequestedData);

            checkForArbitrage(eventWrapperList);

            return eventWrapperList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void checkForArbitrage(List<TableTennisEventWrapper> eventWrapperList) {
        for (TableTennisEventWrapper ttew : eventWrapperList) {
            Map<String, TableTennisEventEntity> eventEntityMap = ttew.getEventEntityMap();
            List<TableTennisEventEntity> values = new ArrayList<>(eventEntityMap.values());
            if (values.size() >= 2) {
                double firstOdd = values.get(0).getFirstPlayerWinningOdd();
                double secondOdd = values.get(0).getSecondPlayerWinningOdd();
                for (int i = 1; i < values.size(); i++) {
                    if (values.get(i).getFirstPlayerWinningOdd() > firstOdd) {
                        firstOdd = values.get(i).getFirstPlayerWinningOdd();
                    }
                    if (values.get(i).getSecondPlayerWinningOdd() > secondOdd) {
                        secondOdd = values.get(i).getSecondPlayerWinningOdd();
                    }
                }
                ttew.setArbitragePercentage(1d / firstOdd + 1d / secondOdd);
            }
        }
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


        return resultAsList.stream().sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getEventDate(), nullsLast(naturalOrder()))))
                .sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getSecondPlayer(), nullsLast(naturalOrder()))))
                .sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getFirstPlayer(), nullsLast(naturalOrder()))))
                .collect(Collectors.toList());

    }


    private List<PlatformDataRequestWrapperEntity> getPlatformDataRequestWrapperEntities() {
        return this.apisService.getAll().stream().filter(PlatformDataRequestWrapperEntity::getAccessible).collect(Collectors.toList());
    }

    public List<String> getPlatformNames() {
        return this.apisService.getAll().stream().filter(PlatformDataRequestWrapperEntity::getAccessible).map(PlatformDataRequestWrapperEntity::getPlatformName)
                .collect(Collectors.toList());
    }
}
