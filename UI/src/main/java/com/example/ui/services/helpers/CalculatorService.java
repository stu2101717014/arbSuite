package com.example.ui.services.helpers;

import com.example.ui.entities.helpers.TableTennisEventEntity;
import com.example.ui.entities.helpers.TableTennisEventWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CalculatorService {

    public List<Double> calculateBets(double investment, List<Double> odds, double arbitragePercentage){
        double oddsSize = odds.size();
        List<Double> resultList = new ArrayList<>();

        for(int i = 0; i < oddsSize; i++){
            double individualArbitragePercentage = (1 / odds.get(i)) * 100;
            resultList.add(((investment * individualArbitragePercentage) / arbitragePercentage) / 100);

        }

        return resultList;
    }

    public void checkForArbitrage(List<TableTennisEventWrapper> eventWrapperList) {
        for (TableTennisEventWrapper ttew : eventWrapperList) {
            Map<String, TableTennisEventEntity> eventEntityMap = ttew.getEventEntityMap();

            List<Map.Entry<String, TableTennisEventEntity>> entries = new ArrayList<>(eventEntityMap.entrySet());
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
                ttew.setArbitragePercentage(1d / firstOdd + 1d / secondOdd);
            }
        }
    }
}
