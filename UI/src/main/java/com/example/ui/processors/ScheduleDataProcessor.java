package com.example.ui.processors;


import com.example.ui.entities.helpers.TableTennisEventWrapperDTO;
import com.example.ui.entities.jpa.NamesSimilaritiesDAO;
import com.example.ui.entities.jpa.ResultEntityDAO;
import com.example.ui.repos.PostProcessTableTennisWrapperRepository;
import com.example.ui.repos.ResultEntityRepository;
import com.example.ui.services.interfaces.ArbitrageService;
import com.example.ui.services.interfaces.NamesSimilaritiesService;
import com.example.ui.services.interfaces.TableTennisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    public ScheduleDataProcessor(ResultEntityRepository resultEntityRepository,
                                 TableTennisService tableTennisService,
                                 NamesSimilaritiesService namesSimilaritiesService,
                                 ArbitrageService arbitrageService,
                                 PostProcessTableTennisWrapperRepository postProcessTableTennisWrapperRepository) {
        this.resultEntityRepository = resultEntityRepository;
        this.tableTennisService = tableTennisService;
        this.namesSimilaritiesService = namesSimilaritiesService;
        this.arbitrageService = arbitrageService;
        this.postProcessTableTennisWrapperRepository = postProcessTableTennisWrapperRepository;
    }

    @Scheduled(fixedDelay = PROCESS_TABLE_TENNIS_EVENT_DELAY)
    public void processTableTennisData() {
        List<String> allPlatformNames = this.resultEntityRepository.getAllPlatformNames();

        List<ResultEntityDAO> resultEntityDAOList = new ArrayList<>();

        for(String platformName : allPlatformNames){
            resultEntityDAOList.add(this.resultEntityRepository.getLatestByPlatformName(platformName));
        }

        List<NamesSimilaritiesDAO> all = this.namesSimilaritiesService.getAll();
        namesSimilaritiesService.remapNamesSimilarities(resultEntityDAOList, all);

        List<TableTennisEventWrapperDTO> eventWrapperList = tableTennisService.reshapeTableTennisEventsData(resultEntityDAOList);

        arbitrageService.checkForArbitrage(eventWrapperList);

        tableTennisService.persistPostProcessedData(eventWrapperList);

        this.cleanUpOldData(CLEANUP_OLD_DATA_OFFSET);
    }


    private void cleanUpOldData(long offset) {
        Date date = new Date(System.currentTimeMillis() - offset);
        List<ResultEntityDAO> oldResultEntities = this.resultEntityRepository.selectAllCreatedBefore(date);

        resultEntityRepository.deleteAll(oldResultEntities);
        postProcessTableTennisWrapperRepository.deleteRecordsCreatedEarlierThan(date);

    }
}


