package com.example.dp.services;

import com.example.dp.data.entities.PostProcessTableTennisWrapperDAO;
import com.example.dp.data.entities.ResultEntityDAO;
import com.example.dp.data.entities.TableTennisEventEntityDAO;
import com.example.dp.data.repositories.PostProcessTableTennisWrapperRepository;
import com.example.dp.data.repositories.ResultEntityRepository;
import com.example.dp.services.helpers.GsonService;
import com.example.dp.services.interfaces.TableTennisService;
import com.google.gson.Gson;
import dtos.ResultEntityDTO;
import dtos.TableTennisEventEntityDTO;
import dtos.TableTennisEventEntityShortDTO;
import dtos.TableTennisEventWrapperDTO;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;

    private final GsonService gsonService;

    @Autowired
    public TableTennisServiceImpl(PostProcessTableTennisWrapperRepository postProcessTableTennisWrapperRepository
            , ResultEntityRepository resultEntityRepository
            , GsonService gsonService
    ) {

        this.postProcessTableTennisWrapperRepository = postProcessTableTennisWrapperRepository;
        this.resultEntityRepository = resultEntityRepository;
        this.gsonService = gsonService;
        this.modelMapper = new ModelMapper();
    }


    public List<TableTennisEventWrapperDTO> sortReshapedData(List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOList) {
        return tableTennisEventWrapperDTOList.stream().sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getEventDate(), nullsLast(naturalOrder()))))
                .sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getSecondPlayer(), nullsLast(naturalOrder()))))
                .sorted(nullsLast(Comparator.comparing(e -> e.getTableTennisEventEntityShort().getFirstPlayer(), nullsLast(naturalOrder()))))
                .collect(Collectors.toList());
    }

    public List<TableTennisEventWrapperDTO> reshapeTableTennisEventsData(List<ResultEntityDTO> resultEntityDAOList) {

        HashMap<TableTennisEventEntityShortDTO, TableTennisEventWrapperDTO> resultMap = new HashMap<>();
        List<TableTennisEventWrapperDTO> resultAsList = new ArrayList<>();

        try {

            for (ResultEntityDTO re : resultEntityDAOList) {

                Set<TableTennisEventEntityDTO> tableTennisEventEntitySet = re.getTableTennisEventEntitySet();

                for (TableTennisEventEntityDTO tableTennisEventEntity : tableTennisEventEntitySet) {

                    TableTennisEventEntityShortDTO temporaryShort = new TableTennisEventEntityShortDTO();
                    temporaryShort.setFirstPlayer(tableTennisEventEntity.getFirstPlayerName());
                    temporaryShort.setSecondPlayer(tableTennisEventEntity.getSecondPlayerName());
                    temporaryShort.setEventDate(tableTennisEventEntity.getEventDate());

                    if (resultMap.containsKey(temporaryShort)) {
                        resultMap.get(temporaryShort).getEventEntityMap().put(re.getPlatformName(), this.modelMapper.map(tableTennisEventEntity, TableTennisEventEntityDTO.class));
                    } else {
                        TableTennisEventWrapperDTO tableTennisEventWrapperDTO = new TableTennisEventWrapperDTO();
                        tableTennisEventWrapperDTO.setTableTennisEventEntityShort(temporaryShort);
                        tableTennisEventWrapperDTO.setEventEntityMap(new HashMap<>());
                        tableTennisEventWrapperDTO.getEventEntityMap().put(re.getPlatformName(), this.modelMapper.map(tableTennisEventEntity, TableTennisEventEntityDTO.class));
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

}
