package com.example.ui.services;

import com.example.ui.entities.helpers.*;
import com.example.ui.entities.jpa.NamesSimilaritiesDAO;
import com.example.ui.entities.jpa.PostProcessTableTennisWrapperDAO;
import com.example.ui.entities.jpa.ResultEntityDAO;
import com.example.ui.entities.jpa.TableTennisEventEntityDAO;
import com.example.ui.repos.PostProcessTableTennisWrapperRepository;
import com.example.ui.repos.ResultEntityRepository;
import com.example.ui.services.helpers.ArbitrageService;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

@Service
public class TableTennisService {


    private NamesSimilaritiesService namesSimilaritiesService;

    private ArbitrageService arbitrageService;

    private final PostProcessTableTennisWrapperRepository postProcessTableTennisWrapperRepository;

    private final ResultEntityRepository resultEntityRepository;

    @Autowired
    public TableTennisService(NamesSimilaritiesService namesSimilaritiesService,
                              ArbitrageService arbitrageService,
                              PostProcessTableTennisWrapperRepository postProcessTableTennisWrapperRepository,
                              ResultEntityRepository resultEntityRepository) {
        this.namesSimilaritiesService = namesSimilaritiesService;
        this.arbitrageService = arbitrageService;
        this.postProcessTableTennisWrapperRepository = postProcessTableTennisWrapperRepository;
        this.resultEntityRepository = resultEntityRepository;
    }


    // TODO TEST remapNamesSimilarities
    public void remapNamesSimilarities(List<ResultEntityDAO> resultEntityDAOList, List<NamesSimilaritiesDAO> namesSimilaritiesDAOList) {

        HashMap<String, HashMap<String, NamesSimilaritiesDAO>> helperMap = new HashMap<>();

        for (NamesSimilaritiesDAO namesSimilarity : namesSimilaritiesDAOList) {
            if (!helperMap.containsKey(namesSimilarity.getPlatformName())) {
                helperMap.put(namesSimilarity.getPlatformName(), new HashMap<String, NamesSimilaritiesDAO>());
            }
            helperMap.get(namesSimilarity.getPlatformName()).put(namesSimilarity.getPlatformSpecificPlayerName(), namesSimilarity);
        }

        for (ResultEntityDAO resultEntity : resultEntityDAOList) {
            Set<TableTennisEventEntityDAO> tableTennisEventEntitySet = resultEntity.getTableTennisEventEntitySet();
            for (TableTennisEventEntityDAO ttee : tableTennisEventEntitySet) {
                if (helperMap.containsKey(resultEntity.getPlatformName()) && helperMap.get(resultEntity.getPlatformName()).containsKey(ttee.getFirstPlayerName())) {
                    NamesSimilaritiesDAO forReplace = helperMap.get(resultEntity.getPlatformName()).get(ttee.getFirstPlayerName());
                    ttee.setFirstPlayerName(forReplace.getUniversalPlayerName());
                }
                if (helperMap.containsKey(resultEntity.getPlatformName()) && helperMap.get(resultEntity.getPlatformName()).containsKey(ttee.getSecondPlayerName())) {
                    NamesSimilaritiesDAO forReplace = helperMap.get(resultEntity.getPlatformName()).get(ttee.getSecondPlayerName());
                    ttee.setSecondPlayerName(forReplace.getUniversalPlayerName());
                }
            }
        }
    }

    public List<TableTennisEventWrapperDTO> sortReshapedData(List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOList) {
        return tableTennisEventWrapperDTOList.stream().sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getEventDate(), nullsLast(naturalOrder()))))
                .sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getSecondPlayer(), nullsLast(naturalOrder()))))
                .sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getFirstPlayer(), nullsLast(naturalOrder()))))
                .collect(Collectors.toList());
    }

    public List<TableTennisEventWrapperDTO> reshapeTableTennisEventsData(List<ResultEntityDAO> resultEntityDAOList) {

        HashMap<TableTennisEventEntityShortDTO, TableTennisEventWrapperDTO> resultMap = new HashMap<>();
        List<TableTennisEventWrapperDTO> resultAsList = new ArrayList<>();

        try {

            for (ResultEntityDAO re : resultEntityDAOList) {

                Set<TableTennisEventEntityDAO> tableTennisEventEntitySet = re.getTableTennisEventEntitySet();

                for (TableTennisEventEntityDAO tableTennisEventEntity : tableTennisEventEntitySet) {

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

    public void persistProcessedData(List<TableTennisEventWrapperDTO> eventWrapperList) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        String toJson = gson.toJson(eventWrapperList);
        PostProcessTableTennisWrapperDAO postProcessTableTennisWrapperDAO = new PostProcessTableTennisWrapperDAO();
        postProcessTableTennisWrapperDAO.setTime(new Date(System.currentTimeMillis()));
        postProcessTableTennisWrapperDAO.setResultAsJson(toJson);
        postProcessTableTennisWrapperRepository.saveAndFlush(postProcessTableTennisWrapperDAO);
    }

    public PostProcessTableTennisWrapperDAO getProcessedData(){
        return this.postProcessTableTennisWrapperRepository.getLatestProcessedData();
    }

    public List<String> getPlatformNames(){
        return this.resultEntityRepository.getAllPlatformNames();
    }
}

class LocalDateAdapter implements JsonSerializer<LocalDate> {
    @Override
    public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
