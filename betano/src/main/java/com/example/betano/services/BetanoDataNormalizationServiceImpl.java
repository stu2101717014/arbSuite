package com.example.betano.services;

import dtos.TableTennisEventEntityDTO;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BetanoDataNormalizationServiceImpl {

    public Set<TableTennisEventEntityDTO> normalise(Map map) {

        Set<TableTennisEventEntityDTO> resultList = new HashSet<TableTennisEventEntityDTO>();
        try {
            if (map != null) {

                if (map.containsKey("Result")) {
                    Map<String, Object> resultKeyMap = (Map<String, Object>) map.get("Result");
                    if (resultKeyMap.containsKey("Items")) {
                        List<Object> items = (List<Object>) resultKeyMap.get("Items");
                        for (Object item : items) {
                            Map<String, Object> itemKeyMap = (Map<String, Object>) item;
                            if (itemKeyMap.containsKey("Events")) {
                                List<Object> eventsList = (List<Object>) itemKeyMap.get("Events");
                                for (Object event : eventsList) {
                                    Map<String, Object> eventMap = (Map<String, Object>) event;
                                    if (eventMap.containsKey("EventDate")) {
                                        TableTennisEventEntityDTO tableTennisEventEntityDTO = new TableTennisEventEntityDTO();

                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                                        Date eventDate = formatter.parse(eventMap.get("EventDate").toString());
                                        Calendar c = Calendar.getInstance();
                                        c.setTime(eventDate);
                                        c.add(Calendar.HOUR, 2);
                                        tableTennisEventEntityDTO.setEventDate(c.getTime());


                                        if (eventMap.containsKey("Items")) {
                                            List<Object> itemsInner = (List<Object>) eventMap.get("Items");

                                            Map<String, Object> oddsItemMap = null;

                                            for (Object innerItem : itemsInner) {
                                                Map<String, Object> innerItemMap = (Map<String, Object>) innerItem;
                                                if (innerItemMap.containsKey("Name") && innerItemMap.get("Name").toString().contains("Победител")) {
                                                    oddsItemMap = (Map<String, Object>) innerItem;
                                                    break;
                                                }
                                            }

                                            if (oddsItemMap != null && oddsItemMap.containsKey("Items")) {
                                                List<Object> innerItemsList = (List<Object>) oddsItemMap.get("Items");
                                                if (innerItemsList.size() >= 2) {
                                                    Map<String, Object> oddFirstPlayerMap = (Map<String, Object>) innerItemsList.get(0);
                                                    Map<String, Object> oddSecondPlayerMap = (Map<String, Object>) innerItemsList.get(1);

                                                    if (oddFirstPlayerMap.containsKey("Name") && oddFirstPlayerMap.containsKey("Price")) {
                                                        String[] names = oddFirstPlayerMap.get("Name").toString().split(", ");
                                                        Collections.reverse(Arrays.asList(names));

                                                        String normalisedFirstPlayerName = String.join(" ", names);
                                                        String price = oddFirstPlayerMap.get("Price").toString();

                                                        tableTennisEventEntityDTO.setFirstPlayerName(normalisedFirstPlayerName);
                                                        tableTennisEventEntityDTO.setFirstPlayerWinningOdd(Double.parseDouble(price));
                                                    }

                                                    if (oddSecondPlayerMap.containsKey("Name") && oddSecondPlayerMap.containsKey("Price")) {
                                                        String[] names = oddSecondPlayerMap.get("Name").toString().split(", ");
                                                        Collections.reverse(Arrays.asList(names));

                                                        String normalisedSecondPlayerName = String.join(" ", names);
                                                        String price = oddSecondPlayerMap.get("Price").toString();

                                                        tableTennisEventEntityDTO.setSecondPlayerName(normalisedSecondPlayerName);
                                                        tableTennisEventEntityDTO.setSecondPlayerWinningOdd(Double.parseDouble(price));
                                                    }

                                                    resultList.add(tableTennisEventEntityDTO);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
