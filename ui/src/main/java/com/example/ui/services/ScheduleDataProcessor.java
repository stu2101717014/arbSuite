package com.example.ui.services;


import com.example.ui.entities.helpers.TableTennisEventWrapperDTO;
import com.example.ui.entities.jpa.NamesSimilaritiesDAO;
import com.example.ui.entities.jpa.ResultEntityDAO;
import com.example.ui.entities.jpa.TableTennisEventEntityDAO;
import com.example.ui.repos.PostProcessTableTennisWrapperRepository;
import com.example.ui.repos.ResultEntityRepository;
import com.example.ui.repos.TableTennisEventEntityRepository;
import com.example.ui.services.helpers.ArbitrageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@EnableScheduling
public class ScheduleDataProcessor {

    private final static int DELAY = 40000;

    private final ResultEntityRepository resultEntityRepository;

    private final DataReceiverService dataReceiverService;

    private final TableTennisService tableTennisService;

    private final NamesSimilaritiesService namesSimilaritiesService;

    private final ArbitrageService arbitrageService;

    private final TableTennisEventEntityRepository tableTennisEventEntityRepository;

    private final PostProcessTableTennisWrapperRepository postProcessTableTennisWrapperRepository;

    @Autowired
    public ScheduleDataProcessor(ResultEntityRepository resultEntityRepository,
                                 DataReceiverService dataReceiverService,
                                 TableTennisService tableTennisService,
                                 NamesSimilaritiesService namesSimilaritiesService,
                                 ArbitrageService arbitrageService,
                                 TableTennisEventEntityRepository tableTennisEventEntityRepository,
                                 PostProcessTableTennisWrapperRepository postProcessTableTennisWrapperRepository) {
        this.resultEntityRepository = resultEntityRepository;
        this.dataReceiverService = dataReceiverService;
        this.tableTennisService = tableTennisService;
        this.namesSimilaritiesService = namesSimilaritiesService;
        this.arbitrageService = arbitrageService;
        this.tableTennisEventEntityRepository = tableTennisEventEntityRepository;
        this.postProcessTableTennisWrapperRepository = postProcessTableTennisWrapperRepository;
    }

    @Scheduled(fixedDelay = DELAY)
    public void processTableTennisData() {
        List<String> allPlatformNames = this.resultEntityRepository.getAllPlatformNames();

        List<ResultEntityDAO> resultEntityDAOList = new ArrayList<>();

        for(String platformName : allPlatformNames){
            resultEntityDAOList.add(this.resultEntityRepository.getLatestByPlatformName(platformName));
        }

        List<NamesSimilaritiesDAO> all = this.namesSimilaritiesService.getAll();
        tableTennisService.remapNamesSimilarities(resultEntityDAOList, all);

        List<TableTennisEventWrapperDTO> eventWrapperList = tableTennisService.reshapeTableTennisEventsData(resultEntityDAOList);

        arbitrageService.checkForArbitrage(eventWrapperList);

        tableTennisService.persistProcessedData(eventWrapperList);

        this.cleanUpOldData();
    }


    private void cleanUpOldData() {
        Date date = new Date(System.currentTimeMillis() - 10 * 60 * 1000);
        List<ResultEntityDAO> oldResultEntities = this.resultEntityRepository.selectAllCreatedBefore(date);

        resultEntityRepository.deleteAll(oldResultEntities);
        postProcessTableTennisWrapperRepository.deleteRecordsCreatedEarlierThan(date);

    }
}


