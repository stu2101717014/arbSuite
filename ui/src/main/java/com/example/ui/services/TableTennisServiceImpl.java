package com.example.ui.services;

import com.example.ui.entities.helpers.*;
import com.example.ui.entities.jpa.PostProcessTableTennisWrapperDAO;
import com.example.ui.entities.jpa.ResultEntityDAO;
import com.example.ui.entities.jpa.TableTennisEventEntityDAO;
import com.example.ui.repos.PostProcessTableTennisWrapperRepository;
import com.example.ui.repos.ResultEntityRepository;
import com.example.ui.services.helpers.GsonService;
import com.example.ui.services.interfaces.TableTennisService;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

@Service
public class TableTennisServiceImpl implements TableTennisService {

    private final PostProcessTableTennisWrapperRepository postProcessTableTennisWrapperRepository;

    private final ResultEntityRepository resultEntityRepository;

    private final GsonService gsonService;

    @Autowired
    public TableTennisServiceImpl(PostProcessTableTennisWrapperRepository postProcessTableTennisWrapperRepository,
                                  ResultEntityRepository resultEntityRepository,
                                  GsonService gsonService) {

        this.postProcessTableTennisWrapperRepository = postProcessTableTennisWrapperRepository;
        this.resultEntityRepository = resultEntityRepository;
        this.gsonService = gsonService;
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
                        resultMap.get(temporaryShort).getEventEntityMap().put(re.getPlatformName(), remapDAOToDTO(tableTennisEventEntity));
                    } else {
                        TableTennisEventWrapperDTO tableTennisEventWrapperDTO = new TableTennisEventWrapperDTO();
                        tableTennisEventWrapperDTO.setTableTennisEventEntityShort(temporaryShort);
                        tableTennisEventWrapperDTO.setEventEntityMap(new HashMap<>());
                        tableTennisEventWrapperDTO.getEventEntityMap().put(re.getPlatformName(), remapDAOToDTO(tableTennisEventEntity));
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

    public void persistPostProcessedData(List<TableTennisEventWrapperDTO> eventWrapperList) {
        Gson gson = gsonService.getGson();
        String toJson = gson.toJson(eventWrapperList);
        PostProcessTableTennisWrapperDAO postProcessTableTennisWrapperDAO = new PostProcessTableTennisWrapperDAO();
        postProcessTableTennisWrapperDAO.setTime(new Date(System.currentTimeMillis()));
        postProcessTableTennisWrapperDAO.setResultAsJson(toJson);
        postProcessTableTennisWrapperRepository.saveAndFlush(postProcessTableTennisWrapperDAO);
    }

    public PostProcessTableTennisWrapperDAO getProcessedData() {
        return this.postProcessTableTennisWrapperRepository.getLatestProcessedData();
    }

    public List<String> getPlatformNames() {
        return this.resultEntityRepository.getAllPlatformNames();
    }


    private TableTennisEventEntityDTO remapDAOToDTO(TableTennisEventEntityDAO tableTennisEventEntity) {
        TableTennisEventEntityDTO tableTennisEventEntityDTO = new TableTennisEventEntityDTO();

        tableTennisEventEntityDTO.setResultEntity(tableTennisEventEntity.getResultEntity());
        tableTennisEventEntityDTO.setEventDate(tableTennisEventEntity.getEventDate());
        tableTennisEventEntityDTO.setFirstPlayerName(tableTennisEventEntity.getFirstPlayerName());
        tableTennisEventEntityDTO.setFirstPlayerWinningOdd(tableTennisEventEntity.getFirstPlayerWinningOdd());
        tableTennisEventEntityDTO.setSecondPlayerName(tableTennisEventEntity.getSecondPlayerName());
        tableTennisEventEntityDTO.setSecondPlayerWinningOdd(tableTennisEventEntity.getSecondPlayerWinningOdd());

        return tableTennisEventEntityDTO;
    }
}


