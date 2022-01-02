package org.example.commons.evaluation.services;

import org.example.commons.entities.ResultEntity;
import org.example.commons.entities.TableTennisEventEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class EvaluationService {

    // compare the events by exact match of the names of the players in the table tennis event.
    //TODO 1 : compare the event dates. Currently not possible because the dates are not verified.
    //TODO 2 : add platform as enum in the ResultEntity or TableTennisEventEntity or Some data wrapper. There will be necessary.

    public HashMap<String, HashMap<String, List<TableTennisEventEntity>>> EvaluateTableTennisDataStrategyExactMatchNoSwitchingPositions(List<ResultEntity> resultEntities){
        HashMap<String, HashMap<String, List<TableTennisEventEntity>>> exactMatchMap =  new HashMap<>();

        for (ResultEntity resultEntity: resultEntities) {
            for (TableTennisEventEntity tableTennisEvent: resultEntity.getTableTennisEventEntitySet()) {
                if (!exactMatchMap.containsKey(tableTennisEvent.getFirstPlayerName())){
                    ArrayList<TableTennisEventEntity> tableTennisEventEntities = new ArrayList<>();
                    tableTennisEventEntities.add(tableTennisEvent);
                    HashMap<String, List<TableTennisEventEntity>> secondaryPlayerData = new HashMap<>();
                    secondaryPlayerData.put(tableTennisEvent.getSecondPlayerName(), tableTennisEventEntities);
                    exactMatchMap.put(tableTennisEvent.getFirstPlayerName(), secondaryPlayerData);

                    continue;
                }
                if (exactMatchMap.containsKey(tableTennisEvent.getFirstPlayerName())
                && (!exactMatchMap.get(tableTennisEvent.getFirstPlayerName()).containsKey(tableTennisEvent.getSecondPlayerName()))){
                    ArrayList<TableTennisEventEntity> tableTennisEventEntities = new ArrayList<>();
                    tableTennisEventEntities.add(tableTennisEvent);
                    exactMatchMap.get(tableTennisEvent.getFirstPlayerName()).put(tableTennisEvent.getSecondPlayerName(), tableTennisEventEntities);

                    continue;
                }
                if (exactMatchMap.containsKey(tableTennisEvent.getFirstPlayerName())
                        && exactMatchMap.get(tableTennisEvent.getFirstPlayerName()).containsKey(tableTennisEvent.getSecondPlayerName())){

                    exactMatchMap.get(tableTennisEvent.getFirstPlayerName()).get(tableTennisEvent.getSecondPlayerName()).add(tableTennisEvent);

                    continue;
                }
            }
        }

        return exactMatchMap;
    }

    //TODO 1 : compare the event dates. Currently not possible because the dates are not verified.
    //TODO 2 : add platform as enum in the ResultEntity or TableTennisEventEntity or Some data wrapper. There will be necessary.

    public HashMap<String, HashMap<String, List<TableTennisEventEntity>>> EvaluateTableTennisDataStrategyFirstPlayerNameSimilaritiesNoSwitchingPositions(
            HashMap<String, HashMap<String, List<TableTennisEventEntity>>> afterExactMatch, List<ResultEntity> resultEntities){




        return afterExactMatch;
    }

}
