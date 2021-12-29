package org.example.commons.bwin.services;


import org.example.commons.entities.TableTennisEventEntity;
import org.example.commons.services.DataNormalizationService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BwinDataNormalizationServiceImpl implements DataNormalizationService {
    @Override
    public Set<TableTennisEventEntity> normalise(Map map) {
        Set<TableTennisEventEntity> resultList = new HashSet<TableTennisEventEntity>();

        if (map != null) {
            Map.Entry<String, Object> value = ((Map<String, Object>) map)
                    .entrySet().stream().filter(en -> en.getKey().equals("fixtures")).findFirst().get();
            ArrayList<Object> valueAsList = (ArrayList<Object>) value.getValue();
            final String regex = "(\\(\\w+\\))";
            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);

            for (Object v : valueAsList) {

                try{
                    Map<String, Object> fixtureEntMap = (Map<String, Object>) v;

                    ArrayList<Object> participants = (ArrayList<Object>) fixtureEntMap.get("participants");
                    Map<String, Object> player1AsMap =(Map<String, Object>) participants.get(0);
                    Map<String, String> player1NameInnerMap = (Map<String, String>) player1AsMap.get("name");
                    String player1NameAsString = player1NameInnerMap.get("value");
                    String player1ShortNameAsString = player1NameInnerMap.get("short");

                    Map<String, Object> player2AsMap =(Map<String, Object>) participants.get(1);
                    Map<String, String> player2NameInnerMap = (Map<String, String>) player2AsMap.get("name");
                    String player2NameAsString = player2NameInnerMap.get("value");

                    ArrayList<Map<String, Object>> games = (ArrayList<Map<String, Object>>) fixtureEntMap.get("games");

                    Date eventDate = null;
                    String startDate =  fixtureEntMap.get("startDate").toString();

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                    eventDate = formatter.parse(startDate);

                    if (games.size() > 0){
                        Object game = games.stream().filter(g -> ((Map<String, Object>) g.get("name")).get("value").toString().equals("2Way - Who will win?")).findFirst().get();

                        ArrayList<Object> results = (ArrayList<Object>) ((Map<String, Object>) game).get("results");

                        Map<String, Object> oneOfThePlayersResultsMap = (Map<String, Object>) results.get(0);
                        Double oneOfThePlayersOdd = (Double) oneOfThePlayersResultsMap.get("odds");
                        String oneOfThePlayersShortName = ((Map<String, String>) oneOfThePlayersResultsMap.get("name")).get("value");

                        Map<String, Object> otherOneOfThePlayersResultsMap = (Map<String, Object>) results.get(1);
                        Double otherOneOfThePlayersOdd = (Double) otherOneOfThePlayersResultsMap.get("odds");

                        final Matcher matcher = pattern.matcher(player1NameAsString);
                        if (matcher.find()){
                            String abbreviation = matcher.group(1);
                            player1NameAsString = player1NameAsString.replace(abbreviation, "").trim();
                        }

                        final Matcher matcher2 = pattern.matcher(player2NameAsString);
                        if (matcher2.find()){
                            String abbreviation = matcher2.group(1);
                            player2NameAsString = player2NameAsString.replace(abbreviation, "").trim();
                        }

                        if (oneOfThePlayersShortName.equals(player1ShortNameAsString)){

                            TableTennisEventEntity tte = new TableTennisEventEntity();
                            tte.setFirstPlayerName(player1NameAsString);
                            tte.setSecondPlayerName(player2NameAsString);
                            tte.setFirstPlayerWinningOdd(oneOfThePlayersOdd);
                            tte.setSecondPlayerWinningOdd(otherOneOfThePlayersOdd);
                            tte.setEventDate(eventDate);
                            resultList.add(tte);

                        }else {
                            TableTennisEventEntity tte = new TableTennisEventEntity();
                            tte.setFirstPlayerName(player2NameAsString);
                            tte.setSecondPlayerName(player1NameAsString);
                            tte.setFirstPlayerWinningOdd(oneOfThePlayersOdd);
                            tte.setSecondPlayerWinningOdd(otherOneOfThePlayersOdd);
                            tte.setEventDate(eventDate);
                            resultList.add(tte);
                        }
                    }
                }catch (Exception e){

                }
            }
        }
        return resultList;
    }
}
