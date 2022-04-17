package com.example.dp.processors;


import com.example.dp.data.entities.ResultEntityDAO;
import com.example.dp.data.repositories.PostProcessTableTennisWrapperRepository;
import com.example.dp.data.repositories.ResultEntityRepository;
import com.example.dp.services.interfaces.ArbitrageService;
import com.example.dp.services.interfaces.HistoricalService;
import com.example.dp.services.interfaces.NamesSimilaritiesService;
import com.example.dp.services.interfaces.TableTennisService;
import dtos.HistoricalTableTennisEventWrapperDTO;
import dtos.NamesSimilaritiesDTO;
import dtos.ResultEntityDTO;
import dtos.TableTennisEventWrapperDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@EnableScheduling
public class ScheduleDataProcessor {

    private final static long PROCESS_TABLE_TENNIS_EVENT_DELAY = 40000L;

    private final static long CLEANUP_OLD_DATA_OFFSET = 600000L;

    private final ResultEntityRepository resultEntityRepository;

    private final TableTennisService tableTennisService;

    private final NamesSimilaritiesService namesSimilaritiesService;

    private final ArbitrageService arbitrageService;

    private final PostProcessTableTennisWrapperRepository postProcessTableTennisWrapperRepository;

    private final ModelMapper modelMapper;

    private final HistoricalService historicalService;

    @Autowired
    public ScheduleDataProcessor(ResultEntityRepository resultEntityRepository,
                                 TableTennisService tableTennisService,
                                 NamesSimilaritiesService namesSimilaritiesService,
                                 ArbitrageService arbitrageService,
                                 PostProcessTableTennisWrapperRepository postProcessTableTennisWrapperRepository,
                                 HistoricalService historicalService) {
        this.resultEntityRepository = resultEntityRepository;
        this.tableTennisService = tableTennisService;
        this.namesSimilaritiesService = namesSimilaritiesService;
        this.arbitrageService = arbitrageService;
        this.postProcessTableTennisWrapperRepository = postProcessTableTennisWrapperRepository;
        this.modelMapper = new ModelMapper();
        this.historicalService = historicalService;
    }

    @Scheduled(fixedDelay = PROCESS_TABLE_TENNIS_EVENT_DELAY)
    public void processTableTennisData() {

        List<String> allPlatformNames = this.resultEntityRepository.getAllPlatformNames();

        List<ResultEntityDTO> resultEntityDTOList = new ArrayList<>();

        for (String platformName : allPlatformNames) {
            resultEntityDTOList.add(modelMapper.map(this.resultEntityRepository.getLatestByPlatformName(platformName), ResultEntityDTO.class));
        }

        List<NamesSimilaritiesDTO> namesSimilaritiesDTOList = this.namesSimilaritiesService.getAll();
        this.namesSimilaritiesService.remapNamesSimilarities(resultEntityDTOList, namesSimilaritiesDTOList);

        List<TableTennisEventWrapperDTO> eventWrapperList = this.tableTennisService.reshapeTableTennisEventsData(resultEntityDTOList);

        this.arbitrageService.checkForArbitrage(eventWrapperList);

        this.historicalService.persistPositiveArbitrageRecords(eventWrapperList);

        this.tableTennisService.persistPostProcessedData(eventWrapperList);

        this.cleanUpOldData(CLEANUP_OLD_DATA_OFFSET);
    }


    private void cleanUpOldData(long offset) {
        Date date = new Date(System.currentTimeMillis() - offset);
        List<ResultEntityDAO> oldResultEntities = this.resultEntityRepository.selectAllCreatedBefore(date);

        this.resultEntityRepository.deleteAll(oldResultEntities);
        this.postProcessTableTennisWrapperRepository.deleteRecordsCreatedEarlierThan(date);

    }
}

