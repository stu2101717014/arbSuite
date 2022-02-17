package com.example.ui.services;

import com.example.ui.entities.helpers.*;
import com.example.ui.entities.jpa.NamesSimilarities;
import com.example.ui.entities.jpa.PlatformDataRequestWrapperEntity;
import com.example.ui.services.helpers.CalculatorService;
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

    private NamesSimilaritiesService namesSimilaritiesService;

    private CalculatorService calculatorService;

    @Autowired
    public TableTennisService(DataRequestService dataRequestService,
                              ApisService apisService,
                              NamesSimilaritiesService namesSimilaritiesService,
                              CalculatorService calculatorService) {
        this.dataRequestService = dataRequestService;
        this.apisService = apisService;
        this.namesSimilaritiesService = namesSimilaritiesService;
        this.calculatorService = calculatorService;
    }

    public List<TableTennisEventWrapper> getData() {
        try {

            lastRequestedData = new RequestDataResult();

            List<NamesSimilarities> all = this.namesSimilaritiesService.getAll();

            //Get accessible apis
            List<PlatformDataRequestWrapperEntity> platformWrapperEntities = getPlatformDataRequestWrapperEntities();

            //Request Table Tennis Events
            lastRequestedData = dataRequestService.requestData(lastRequestedData, platformWrapperEntities);

            //Remap names similarities
            remapNamesSimilarities(lastRequestedData, all);

            //Reshape table Tennis Events from different platforms to convenient data structure
            List<TableTennisEventWrapper> eventWrapperList = reshapeTableTennisEventsData(lastRequestedData);

            //Sort data for UI
            List<TableTennisEventWrapper> sortedData = sortReshapedData(eventWrapperList);

            //Check For Arbitrage
            calculatorService.checkForArbitrage(sortedData);

            return sortedData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // TODO TEST remapNamesSimilarities
    private void remapNamesSimilarities(RequestDataResult lastRequestedData, List<NamesSimilarities> namesSimilaritiesList) {

        HashMap<String, HashMap<String, NamesSimilarities>> helperMap =  new HashMap<>();

        for(NamesSimilarities namesSimilarity : namesSimilaritiesList){
            if (!helperMap.containsKey(namesSimilarity.getPlatformName())){
                helperMap.put(namesSimilarity.getPlatformName(), new HashMap<String, NamesSimilarities>());
            }
            helperMap.get(namesSimilarity.getPlatformName()).put(namesSimilarity.getPlatformSpecificPlayerName(), namesSimilarity);
        }

        List<ResultEntity> entityList = lastRequestedData.getEntityList();
        for (ResultEntity resultEntity : entityList){
            Set<TableTennisEventEntity> tableTennisEventEntitySet = resultEntity.getTableTennisEventEntitySet();
            for (TableTennisEventEntity ttee : tableTennisEventEntitySet){
                if (helperMap.containsKey(resultEntity.getPlatformName()) && helperMap.get(resultEntity.getPlatformName()).containsKey(ttee.getFirstPlayerName())){
                    NamesSimilarities forReplace = helperMap.get(resultEntity.getPlatformName()).get(ttee.getFirstPlayerName());
                    ttee.setFirstPlayerName(forReplace.getUniversalPlayerName());
                }
                if (helperMap.containsKey(resultEntity.getPlatformName()) && helperMap.get(resultEntity.getPlatformName()).containsKey(ttee.getSecondPlayerName())){
                    NamesSimilarities forReplace = helperMap.get(resultEntity.getPlatformName()).get(ttee.getSecondPlayerName());
                    ttee.setSecondPlayerName(forReplace.getUniversalPlayerName());
                }
            }
        }
    }

    public List<TableTennisEventWrapper> sortReshapedData(List<TableTennisEventWrapper> tableTennisEventWrapperList){
        return tableTennisEventWrapperList.stream().sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getEventDate(), nullsLast(naturalOrder()))))
                .sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getSecondPlayer(), nullsLast(naturalOrder()))))
                .sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getFirstPlayer(), nullsLast(naturalOrder()))))
                .collect(Collectors.toList());
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


        return resultAsList;

    }


    private List<PlatformDataRequestWrapperEntity> getPlatformDataRequestWrapperEntities() {
        return this.apisService.getAll().stream().filter(PlatformDataRequestWrapperEntity::getAccessible).collect(Collectors.toList());
    }

    public List<String> getPlatformNames() {
        return this.apisService.getAll().stream().filter(PlatformDataRequestWrapperEntity::getAccessible).map(PlatformDataRequestWrapperEntity::getPlatformName)
                .collect(Collectors.toList());
    }
}
