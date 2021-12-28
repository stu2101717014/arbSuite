package org.example.commons.betwinner.services;

import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.dom.Element;
import org.example.commons.entities.ResultEntity;
import org.example.commons.entities.TableTennisEventEntity;
import org.example.commons.repos.ResultEntityRepository;
import org.example.commons.repos.TableTennisEventEntityRepository;
import org.example.commons.services.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class BetwinnerService {
    private static String BET_WINNER_TABLE_TENNIS_REQUEST_URL = "https://betwinner-ua.com/line/Table-Tennis/";

    private ResultEntityRepository resultEntityRepository;

    private TableTennisEventEntityRepository tableTennisEventEntityRepository;

    private HttpService httpService;


    @Autowired
    public BetwinnerService(ResultEntityRepository resultEntityRepository, TableTennisEventEntityRepository tableTennisEventEntityRepository, HttpService httpService) {
        this.resultEntityRepository = resultEntityRepository;
        this.tableTennisEventEntityRepository = tableTennisEventEntityRepository;
        this.httpService = httpService;
    }

    public String createAndExecuteTableTennisUserStory(){
        ResultEntity resultEntity = new ResultEntity();

        try {
            Date date = new Date(System.currentTimeMillis());
            resultEntity.setTime(date);

            HashSet<TableTennisEventEntity> tableTennisEventEntities =  new HashSet<>();
            HashSet<ResultEntity> resultEntityHashSet = new HashSet<>();
            resultEntityHashSet.add(resultEntity);

            BrowserEngine browser = BrowserFactory.getWebKit();

            try (Page page = browser.navigate(BET_WINNER_TABLE_TENNIS_REQUEST_URL)) {

                List<Element> elements = page.getDocument().queryAll("div.c-events__item.c-events__item_game");

                for (Element el: elements) {

                    Date eventDate = null;
                    List<Element> dateOfEventEls = el.queryAll("div.c-events__time.min");
                    if (!dateOfEventEls.isEmpty()){
                        String eventDateEls = dateOfEventEls.get(0).getText();

                        eventDateEls = eventDateEls.replace("\n", "");
                        String[] eventDateParts = eventDateEls.split(" ", -1);
                        List<String> eventDatePartsAsList = Arrays.stream(eventDateParts).filter(e -> !e.equals("")).collect(Collectors.toList());
                        int year = Calendar.getInstance().get(Calendar.YEAR);

                        String finalDateAsString = eventDatePartsAsList.get(0).replace(".", "-") + "-" + year + " " + eventDatePartsAsList.get(1);

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault());
                        eventDate = formatter.parse(finalDateAsString);
                    }

                    List<Element> teamsEls = el.queryAll("span.c-events__teams");
                    List<Element> betsEls = el.queryAll("div.c-bets");

                    String playerNames = teamsEls.get(0).getText();
                    String playerNamesAsArray[] = playerNames.split("\\r?\\n", -1);


                    final String regex = "(\\d+.\\d+)-(\\d+.\\d+)\\d.";

                    String betsAsText = betsEls.get(0).getText();

                    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
                    final Matcher matcher = pattern.matcher(betsAsText);

                    Double firstPlayerBet = 0.0d;
                    Double secondPlayerBet = 0.0d;

                    while (matcher.find()) {
                        firstPlayerBet = Double.parseDouble(matcher.group(1));
                        secondPlayerBet = Double.parseDouble(matcher.group(2));
                    }

                    if ((firstPlayerBet + secondPlayerBet) == 0.0d ){
                        continue;
                    }

                    TableTennisEventEntity tableTennisEvent =  new TableTennisEventEntity();
                    tableTennisEvent.setFirstPlayerName(playerNamesAsArray[1]);
                    tableTennisEvent.setSecondPlayerName(playerNamesAsArray[3]);
                    tableTennisEvent.setFirstPlayerWinningOdd(firstPlayerBet);
                    tableTennisEvent.setSecondPlayerWinningOdd(secondPlayerBet);
                    tableTennisEvent.setEventDate(eventDate);

                    tableTennisEvent.setResultEntity(resultEntityHashSet);

                    tableTennisEventEntities.add(tableTennisEvent);
                }
            }

            resultEntity.setTableTennisEventEntitySet(tableTennisEventEntities);

            this.tableTennisEventEntityRepository.saveAllAndFlush(resultEntity.getTableTennisEventEntitySet());

            this.resultEntityRepository.saveAndFlush(resultEntity);

        }catch (Exception e){
            resultEntity.setException(e);
        }

        return httpService.serializeResultEnt(resultEntity);
    }
}
