package com.example.ui.services;

import com.example.ui.entities.helpers.TableTennisEventWrapperDTO;
import com.example.ui.entities.jpa.HistoricalTableTennisEventWrapperDAO;
import com.example.ui.entities.jpa.TableTennisEventEntityDAO;
import com.example.ui.repos.HistoricalTableTennisEventWrapperRepository;
import com.example.ui.services.helpers.GsonService;
import com.example.ui.services.interfaces.ArbitrageService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ArbitrageServiceImpl implements ArbitrageService {

    private final GsonService gsonService;

    private final HistoricalTableTennisEventWrapperRepository historicalTableTennisEventWrapperRepository;

    @Autowired
    public ArbitrageServiceImpl(GsonService gsonService,
                                HistoricalTableTennisEventWrapperRepository historicalTableTennisEventWrapperRepository) {
        this.gsonService = gsonService;
        this.historicalTableTennisEventWrapperRepository = historicalTableTennisEventWrapperRepository;
    }

    public List<Double> calculateBets(double investment, List<Double> odds, double arbitragePercentage) {
        double oddsSize = odds.size();
        List<Double> resultList = new ArrayList<>();

        for (int i = 0; i < oddsSize; i++) {
            double individualArbitragePercentage = (1 / odds.get(i)) * 100;
            resultList.add(((investment * individualArbitragePercentage) / arbitragePercentage) / 100);

        }

        return resultList;
    }

    public void checkForArbitrage(List<TableTennisEventWrapperDTO> eventWrapperList) {
        for (TableTennisEventWrapperDTO ttew : eventWrapperList) {
            Map<String, TableTennisEventEntityDAO> eventEntityMap = ttew.getEventEntityMap();

            List<Map.Entry<String, TableTennisEventEntityDAO>> entries = new ArrayList<>(eventEntityMap.entrySet());
            if (entries.size() >= 2) {
                double firstOdd = entries.get(0).getValue().getFirstPlayerWinningOdd();
                double secondOdd = entries.get(0).getValue().getSecondPlayerWinningOdd();
                ttew.setWinningPlatformOne(entries.get(0).getKey());
                ttew.setWinningPlatformTwo(entries.get(0).getKey());
                for (int i = 1; i < entries.size(); i++) {
                    if (entries.get(i).getValue().getFirstPlayerWinningOdd() > firstOdd) {
                        firstOdd = entries.get(i).getValue().getFirstPlayerWinningOdd();
                        ttew.setWinningPlatformOne(entries.get(i).getKey());
                    }
                    if (entries.get(i).getValue().getSecondPlayerWinningOdd() > secondOdd) {
                        secondOdd = entries.get(i).getValue().getSecondPlayerWinningOdd();
                        ttew.setWinningPlatformTwo(entries.get(i).getKey());
                    }
                }
                double arbitrageNumber = 1d / firstOdd + 1d / secondOdd;
                ttew.setArbitragePercentage(arbitrageNumber);
            }
        }
    }

    @Override
    public void persistPositiveArbitrageRecords(List<TableTennisEventWrapperDTO> eventWrapperList) {
        Gson gson = gsonService.getGson();

        try {
            for (TableTennisEventWrapperDTO eventWrapperDTO : eventWrapperList) {

                if (eventWrapperDTO.getArbitragePercentage() != null &&
                        eventWrapperDTO.getArbitragePercentage() < 1d) {
                    String currentEventWrapperAsJsonString = gson.toJson(eventWrapperDTO);
                    Date detected = new Date(System.currentTimeMillis());

                    HistoricalTableTennisEventWrapperDAO historicalTableTennisEventWrapperDAO = new HistoricalTableTennisEventWrapperDAO();
                    historicalTableTennisEventWrapperDAO.setTime(detected);
                    historicalTableTennisEventWrapperDAO.setHistoricalRecordEventWrapperAsJson(currentEventWrapperAsJsonString);

                    this.historicalTableTennisEventWrapperRepository.saveAndFlush(historicalTableTennisEventWrapperDAO);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
