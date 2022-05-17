package com.example.palmsbet.services;

import dtos.TableTennisEventEntityDTO;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PalmsbetDataNormalizationServiceImpl {

    public Set<TableTennisEventEntityDTO> normalise(Map map) {
        Set<TableTennisEventEntityDTO> resultList = new HashSet<TableTennisEventEntityDTO>();
        try {
            if (map != null) {

                Map<String, String> usefulDates = new HashMap<>();
                if (map.containsKey("dates")) {
                    Map<String, Object> dates = (Map<String, Object>) map.get("dates");
                    if (dates.containsKey("add")) {
                        Map<String, Object> inAddTag = (Map<String, Object>) dates.get("add");
                        for (Map.Entry<String, Object> ent : inAddTag.entrySet()) {
                            usefulDates.put(String.format("%10.0f", Double.parseDouble(ent.getKey())), ent.getValue().toString());
                        }
                    }
                }

                Map<String, String> eventIdToOutcomeIdMap = new HashMap<>();
                if (map.containsKey("outcomes")) {
                    Map<String, Object> outcomes = (Map<String, Object>) map.get("outcomes");
                    if (outcomes.containsKey("add")) {
                        ArrayList<Object> add = (ArrayList<Object>) outcomes.get("add");
                        for (Object ad : add) {
                            Map<String, Object> adAsMap = (Map<String, Object>) ad;
                            if (adAsMap.containsKey("id") && adAsMap.containsKey("e")) {
                                String eventId = String.format("%10.0f", Double.parseDouble(adAsMap.get("e").toString()));
                                String outcomeId = String.format("%10.0f", Double.parseDouble(adAsMap.get("id").toString()));

                                eventIdToOutcomeIdMap.put(eventId, outcomeId);
                            }
                        }
                    }
                }

                Map<String, ArrayList<Object>> outcomesMap = new HashMap<>();
                if (map.containsKey("rates")) {
                    Map<String, Object> rates = (Map<String, Object>) map.get("rates");
                    if (rates.containsKey("add")) {
                        ArrayList<Object> add = (ArrayList<Object>) rates.get("add");

                        for (Object ad : add) {
                            Map<String, Object> adAsMap = (Map<String, Object>) ad;
                            if (adAsMap.containsKey("o")) {
                                String outcomeId = String.format("%10.0f", Double.parseDouble(adAsMap.get("o").toString()));
                                if (!outcomesMap.containsKey(outcomeId)) {
                                    outcomesMap.put(outcomeId, new ArrayList<>());
                                }
                                outcomesMap.get(outcomeId).add(ad);
                            }
                        }
                    }
                }

                if (map.containsKey("events")) {

                    Map<String, Object> events = (Map<String, Object>) map.get("events");
                    if (events.containsKey("add")) {
                        ArrayList<Object> add = (ArrayList<Object>) events.get("add");

                        Map<String, ArrayList<String>> helper = new HashMap<>();

                        for (Object obj : add) {
                            Map<String, Object> eventMap = (Map<String, Object>) obj;

                            String idAsStr = "";

                            if (eventMap.containsKey("id")) {

                                idAsStr = String.format("%10.0f", Double.parseDouble(eventMap.get("id").toString()));
                                helper.put(idAsStr, new ArrayList<>());


                            }
                            if (eventMap.containsKey("ft")) {
                                String firstPlayerName = eventMap.get("ft").toString();

                                String[] split = firstPlayerName.split("\\s+");
                                Collections.reverse(Arrays.asList(split));

                                helper.get(idAsStr).add(String.join(" ", split));
                            }
                            if (eventMap.containsKey("st")) {
                                String secondPlayer = eventMap.get("st").toString();

                                String[] split = secondPlayer.split("\\s+");
                                Collections.reverse(Arrays.asList(split));

                                helper.get(idAsStr).add(String.join(" ", split));
                            }

                            if (usefulDates.containsKey(idAsStr)) {
                                String dateAsString = usefulDates.get(idAsStr);
                                helper.get(idAsStr).add(dateAsString);
                            }

                            if (eventIdToOutcomeIdMap.containsKey(idAsStr)) {
                                String outcomeId = eventIdToOutcomeIdMap.get(idAsStr);
                                if (outcomesMap.containsKey(outcomeId)) {
                                    ArrayList<Object> outcomes = outcomesMap.get(outcomeId);

                                    if (outcomes.size() == 2) {
                                        Map<String, Object> firstOdd = (Map<String, Object>) outcomes.get(0);
                                        Map<String, Object> secOdd = (Map<String, Object>) outcomes.get(1);
                                        if (firstOdd.containsKey("t") && firstOdd.get("t").toString().contains("1")) {
                                            helper.get(idAsStr).add(firstOdd.get("v").toString());
                                            helper.get(idAsStr).add(secOdd.get("v").toString());
                                        } else {
                                            helper.get(idAsStr).add(secOdd.get("v").toString());
                                            helper.get(idAsStr).add(firstOdd.get("v").toString());
                                        }

                                    }
                                }
                            }
                        }

                        for (Map.Entry<String, ArrayList<String>> entry : helper.entrySet()) {

                            if (entry.getValue().size() == 5) {
                                TableTennisEventEntityDTO tableTennisEventEntityDTO = new TableTennisEventEntityDTO();

                                tableTennisEventEntityDTO.setFirstPlayerName(entry.getValue().get(0));

                                tableTennisEventEntityDTO.setSecondPlayerName(entry.getValue().get(1));

                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                                Date eventDate = formatter.parse(entry.getValue().get(2));
                                Calendar c = Calendar.getInstance();
                                c.setTime(eventDate);
                                c.add(Calendar.HOUR, -4);
                                tableTennisEventEntityDTO.setEventDate(c.getTime());

                                tableTennisEventEntityDTO.setFirstPlayerWinningOdd(Double.parseDouble(entry.getValue().get(3)));

                                tableTennisEventEntityDTO.setSecondPlayerWinningOdd(Double.parseDouble(entry.getValue().get(4)));

                                resultList.add(tableTennisEventEntityDTO);
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
