package com.example.dp.services;

import com.example.dp.data.entities.MetricsDAO;
import com.example.dp.data.entities.PostProcessTableTennisWrapperDAO;
import com.example.dp.data.repositories.MetricsRepository;
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

@Service
public class TableTennisServiceImpl implements TableTennisService {

    private final PostProcessTableTennisWrapperRepository postProcessTableTennisWrapperRepository;

    private final ResultEntityRepository resultEntityRepository;

    private final ModelMapper modelMapper;

    private final GsonService gsonService;

    private final MetricsRepository metricsRepository;

    @Autowired
    public TableTennisServiceImpl(PostProcessTableTennisWrapperRepository postProcessTableTennisWrapperRepository
            , ResultEntityRepository resultEntityRepository
            , GsonService gsonService
            , MetricsRepository metricsRepository
            , ModelMapper modelMapper
    ) {

        this.postProcessTableTennisWrapperRepository = postProcessTableTennisWrapperRepository;
        this.resultEntityRepository = resultEntityRepository;
        this.gsonService = gsonService;
        this.modelMapper = modelMapper;
        this.metricsRepository = metricsRepository;
    }


    public List<TableTennisEventWrapperDTO> reshapeTableTennisEventsData(List<ResultEntityDTO> resultEntityDTOList) {

        HashMap<TableTennisEventEntityShortDTO, TableTennisEventWrapperDTO> resultMap = new HashMap<>();
        List<TableTennisEventWrapperDTO> resultAsList = new ArrayList<>();

        try {

            for (ResultEntityDTO re : resultEntityDTOList) {

                Set<TableTennisEventEntityDTO> tableTennisEventEntitySet = re.getTableTennisEventEntitySet();

                for (TableTennisEventEntityDTO tableTennisEventEntity : tableTennisEventEntitySet) {

                    TableTennisEventEntityShortDTO temporaryShort = new TableTennisEventEntityShortDTO();
                    temporaryShort.setFirstPlayer(tableTennisEventEntity.getFirstPlayerName());
                    temporaryShort.setSecondPlayer(tableTennisEventEntity.getSecondPlayerName());
                    temporaryShort.setEventDate(tableTennisEventEntity.getEventDate());

                    TableTennisEventEntityDTO tableTennisEventEntityDTO = this.modelMapper.map(tableTennisEventEntity, TableTennisEventEntityDTO.class);
                    tableTennisEventEntityDTO.setStartExtraction(re.getTime());
                    tableTennisEventEntityDTO.setFinishedExtraction(re.getFinishedTime());

                    if (resultMap.containsKey(temporaryShort)) {
                        resultMap.get(temporaryShort).getEventEntityMap().put(re.getPlatformName(), tableTennisEventEntityDTO);
                    } else {
                        TableTennisEventWrapperDTO tableTennisEventWrapperDTO = new TableTennisEventWrapperDTO();
                        tableTennisEventWrapperDTO.setTableTennisEventEntityShort(temporaryShort);
                        tableTennisEventWrapperDTO.setEventEntityMap(new HashMap<>());
                        tableTennisEventWrapperDTO.getEventEntityMap().put(re.getPlatformName(), tableTennisEventEntityDTO);
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

    @Override
    public PostProcessTableTennisWrapperDAO getLatestData() {
        return this.postProcessTableTennisWrapperRepository.getLatestProcessedData();
    }

    public List<String> getPlatformNames() {
        return this.resultEntityRepository.getAllPlatformNames();
    }

    public void persistMetrics(Long reshapeTime, Long remapNamesSimilaritiesTime) {
        MetricsDAO metricsDAO = new MetricsDAO();
        metricsDAO.setDataReshapeTimeComplexity(reshapeTime);
        metricsDAO.setNameSimilaritiesRemapTimeComplexity(remapNamesSimilaritiesTime);

        this.metricsRepository.deleteAll();
        this.metricsRepository.saveAndFlush(metricsDAO);
    }

    public MetricsDAO getLastMetrics(){
        List<MetricsDAO> all = this.metricsRepository.findAll();
        Optional<MetricsDAO> first = all.stream().findFirst();
        if (first.isPresent()){
            return first.get();
        }
        return new MetricsDAO();
    }

}
