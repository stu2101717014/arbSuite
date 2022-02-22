package com.example.ui.services;

import com.example.ui.entities.helpers.*;
import com.example.ui.entities.jpa.NamesSimilaritiesDAO;
import com.example.ui.entities.jpa.PlatformDataRequestWrapperEntityDAO;
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

    private RequestDataResultDTO lastRequestedData;

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

    public List<TableTennisEventWrapperDTO> getData() {
        try {

            lastRequestedData = new RequestDataResultDTO();

            List<NamesSimilaritiesDAO> all = this.namesSimilaritiesService.getAll();

            //Get accessible apis
            List<PlatformDataRequestWrapperEntityDAO> platformWrapperEntities = getPlatformDataRequestWrapperEntities();

            //Request Table Tennis Events
            lastRequestedData = dataRequestService.requestData(lastRequestedData, platformWrapperEntities);

            //Remap names similarities
            remapNamesSimilarities(lastRequestedData, all);

            //Reshape table Tennis Events from different platforms to convenient data structure
            List<TableTennisEventWrapperDTO> eventWrapperList = reshapeTableTennisEventsData(lastRequestedData);

            //Sort data for UI
            List<TableTennisEventWrapperDTO> sortedData = sortReshapedData(eventWrapperList);

            //Check For Arbitrage
            calculatorService.checkForArbitrage(sortedData);

            return sortedData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // TODO TEST remapNamesSimilarities
    private void remapNamesSimilarities(RequestDataResultDTO lastRequestedData, List<NamesSimilaritiesDAO> namesSimilaritiesDAOList) {

        HashMap<String, HashMap<String, NamesSimilaritiesDAO>> helperMap =  new HashMap<>();

        for(NamesSimilaritiesDAO namesSimilarity : namesSimilaritiesDAOList){
            if (!helperMap.containsKey(namesSimilarity.getPlatformName())){
                helperMap.put(namesSimilarity.getPlatformName(), new HashMap<String, NamesSimilaritiesDAO>());
            }
            helperMap.get(namesSimilarity.getPlatformName()).put(namesSimilarity.getPlatformSpecificPlayerName(), namesSimilarity);
        }

        List<ResultEntity> entityList = lastRequestedData.getEntityList();
        for (ResultEntity resultEntity : entityList){
            Set<TableTennisEventEntity> tableTennisEventEntitySet = resultEntity.getTableTennisEventEntitySet();
            for (TableTennisEventEntity ttee : tableTennisEventEntitySet){
                if (helperMap.containsKey(resultEntity.getPlatformName()) && helperMap.get(resultEntity.getPlatformName()).containsKey(ttee.getFirstPlayerName())){
                    NamesSimilaritiesDAO forReplace = helperMap.get(resultEntity.getPlatformName()).get(ttee.getFirstPlayerName());
                    ttee.setFirstPlayerName(forReplace.getUniversalPlayerName());
                }
                if (helperMap.containsKey(resultEntity.getPlatformName()) && helperMap.get(resultEntity.getPlatformName()).containsKey(ttee.getSecondPlayerName())){
                    NamesSimilaritiesDAO forReplace = helperMap.get(resultEntity.getPlatformName()).get(ttee.getSecondPlayerName());
                    ttee.setSecondPlayerName(forReplace.getUniversalPlayerName());
                }
            }
        }
    }

    public List<TableTennisEventWrapperDTO> sortReshapedData(List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOList){
        return tableTennisEventWrapperDTOList.stream().sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getEventDate(), nullsLast(naturalOrder()))))
                .sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getSecondPlayer(), nullsLast(naturalOrder()))))
                .sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getFirstPlayer(), nullsLast(naturalOrder()))))
                .collect(Collectors.toList());
    }

    public List<TableTennisEventWrapperDTO> reshapeTableTennisEventsData(RequestDataResultDTO requestDataResultDTO) {

        HashMap<TableTennisEventEntityShortDTO, TableTennisEventWrapperDTO> resultMap = new HashMap<>();
        List<TableTennisEventWrapperDTO> resultAsList = new ArrayList<>();

        try {
            List<ResultEntity> entityList = requestDataResultDTO.getEntityList();

            for (ResultEntity re : entityList) {

                Set<TableTennisEventEntity> tableTennisEventEntitySet = re.getTableTennisEventEntitySet();

                for (TableTennisEventEntity tableTennisEventEntity : tableTennisEventEntitySet) {

                    TableTennisEventEntityShortDTO temporaryShort = new TableTennisEventEntityShortDTO();
                    temporaryShort.setFirstPlayer(tableTennisEventEntity.getFirstPlayerName());
                    temporaryShort.setSecondPlayer(tableTennisEventEntity.getSecondPlayerName());
                    temporaryShort.setEventDate(tableTennisEventEntity.getEventDate());

                    if (resultMap.containsKey(temporaryShort)) {
                        resultMap.get(temporaryShort).getEventEntityMap().put(re.getPlatformName(), tableTennisEventEntity);
                    } else {
                        TableTennisEventWrapperDTO tableTennisEventWrapperDTO = new TableTennisEventWrapperDTO();
                        tableTennisEventWrapperDTO.setTableTennisEventEntityShort(temporaryShort);
                        tableTennisEventWrapperDTO.setEventEntityMap(new HashMap<>());
                        tableTennisEventWrapperDTO.getEventEntityMap().put(re.getPlatformName(), tableTennisEventEntity);
                        resultMap.put(temporaryShort, tableTennisEventWrapperDTO);
                    }
                }
            }

            resultAsList = new ArrayList<>(resultMap.values());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return resultAsList;

    }


    private List<PlatformDataRequestWrapperEntityDAO> getPlatformDataRequestWrapperEntities() {
        return this.apisService.getAll().stream().filter(PlatformDataRequestWrapperEntityDAO::getAccessible).collect(Collectors.toList());
    }

    public List<String> getPlatformNames() {
        return this.apisService.getAll().stream().filter(PlatformDataRequestWrapperEntityDAO::getAccessible).map(PlatformDataRequestWrapperEntityDAO::getPlatformName)
                .collect(Collectors.toList());
    }
}
