package com.example.bets22experimental.services;

import dtos.TableTennisEventEntityDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Bets22DataNormalizationServiceImpl {

    public Set<TableTennisEventEntityDTO> normalise(Map map) {
        Set<TableTennisEventEntityDTO> resultList = new HashSet<TableTennisEventEntityDTO>();

        if (map != null) {
            Map.Entry<String, Object> value = ((Map<String, Object>) map)
                    .entrySet().stream().filter(en -> en.getKey().equals("Value")).findFirst().get();
            ArrayList<Object> valueAsList = (ArrayList<Object>) value.getValue();

            for (Object v : valueAsList) {
                try {
                    Map<String, Object> valueEntityMap = (Map<String, Object>) v;

                    String firstPlayer = valueEntityMap.get(valueEntityMap.keySet().stream().filter(fp -> fp.equals("O1")).findFirst().get()).toString();
                    String secondPlayer = valueEntityMap.get(valueEntityMap.keySet().stream().filter(fp -> fp.equals("O2")).findFirst().get()).toString();

                    String timeStampAsString = valueEntityMap.get(valueEntityMap.keySet().stream().filter(fp -> fp.equals("S")).findFirst().get()).toString();
                    String resultTimeStamp = String.format("%.0f", Double.parseDouble(timeStampAsString));
                    Long timeStamp = Long.parseLong(resultTimeStamp);
                    Date date = new Date(timeStamp * 1000);
                    if (TimeZone.getDefault().inDaylightTime(new Date())) {
                        date = new Date(timeStamp * 1000 - 3600 * 1000);
                    }

                    ArrayList<Object> odds = (ArrayList<Object>) valueEntityMap.get(valueEntityMap.keySet().stream().filter(fp -> fp.equals("E")).findFirst().get());
                    if (odds.size() > 2) {
                        Map<String, Double> firstPlayerWinningOddMap = (Map<String, Double>) odds.get(0);
                        Map<String, Double> secondPlayerWinningOddMap = (Map<String, Double>) odds.get(1);

                        Double firstPlayerWinningOdd = firstPlayerWinningOddMap.get("C");
                        Double secondPlayerWinningOdd = secondPlayerWinningOddMap.get("C");

                        TableTennisEventEntityDTO tblTnsEventEnt = new TableTennisEventEntityDTO();
                        tblTnsEventEnt.setFirstPlayerName(firstPlayer);
                        tblTnsEventEnt.setSecondPlayerName(secondPlayer);
                        tblTnsEventEnt.setFirstPlayerWinningOdd(firstPlayerWinningOdd);
                        tblTnsEventEnt.setSecondPlayerWinningOdd(secondPlayerWinningOdd);
                        tblTnsEventEnt.setEventDate(date);

                        resultList.add(tblTnsEventEnt);
                    }
                } catch (Exception e) {

                }
            }
        }
        return resultList;
    }
}
