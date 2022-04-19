package com.example.dp.services;

import com.example.dp.mqtt.historical.HistoricalRecordTTEESaveSender;
import com.example.dp.services.helpers.GsonService;
import com.example.dp.services.interfaces.ArbitrageService;
import com.google.gson.Gson;
import dtos.HistoricalTableTennisEventWrapperDTO;
import dtos.TableTennisEventEntityDTO;
import dtos.TableTennisEventWrapperDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ArbitrageServiceImpl implements ArbitrageService {

    private final GsonService gsonService;

    private final HistoricalRecordTTEESaveSender historicalRecordTTEESaveSender;

    @Autowired
    public ArbitrageServiceImpl(GsonService gsonService, HistoricalRecordTTEESaveSender historicalRecordTTEESaveSender) {
        this.gsonService = gsonService;
        this.historicalRecordTTEESaveSender = historicalRecordTTEESaveSender;
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
            Map<String, TableTennisEventEntityDTO> eventEntityMap = ttew.getEventEntityMap();

            List<Map.Entry<String, TableTennisEventEntityDTO>> entries = new ArrayList<>(eventEntityMap.entrySet());
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


}

