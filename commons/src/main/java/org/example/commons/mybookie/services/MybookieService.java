package org.example.commons.mybookie.services;

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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@Service
public class MybookieService {
    private static String MY_BOOKIE_TABLE_TENNIS_REQUEST_URL = "https://www.mybookie.ag/sportsbook/table-tennis/";

    private ResultEntityRepository resultEntityRepository;

    private TableTennisEventEntityRepository tableTennisEventEntityRepository;

    private HttpService httpService;

    @Autowired
    public MybookieService(ResultEntityRepository resultEntityRepository, TableTennisEventEntityRepository tableTennisEventEntityRepository, HttpService httpService) {
        this.resultEntityRepository = resultEntityRepository;
        this.tableTennisEventEntityRepository = tableTennisEventEntityRepository;
        this.httpService = httpService;
    }

    public String createAndExecuteTableTennisUserStory() {
        ResultEntity resultEntity = new ResultEntity();

        try {
            Date date = new Date(System.currentTimeMillis());
            resultEntity.setTime(date);

            HashSet<TableTennisEventEntity> tableTennisEventEntities = new HashSet<>();
            HashSet<ResultEntity> resultEntityHashSet = new HashSet<>();
            resultEntityHashSet.add(resultEntity);

            BrowserEngine browser = BrowserFactory.getWebKit();

            try (Page page = browser.navigate(MY_BOOKIE_TABLE_TENNIS_REQUEST_URL)) {
                List<Element> elements = page.getDocument().queryAll("div.game-line");

                for (Element element : elements) {
                    String firstPlayerName = element.query("div.game-line__visitor-team").getText();
                    String secondPlayerName = element.query("div.game-line__home-team").getText();

                    Element dateElement = element.query("span.game-line__time__date__hour");
                    if (dateElement != null){

                        String dateAsString = dateElement.getAttribute("data-time");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        Date eventDate = formatter.parse(dateAsString);
                        //eventDate = addHoursToJavaUtilDate(eventDate, 2);

                        List<Element> odds = element.queryAll("button.lines-odds");

                        if (odds.size() > 4) {
                            String firstPlayerWinningOdd = odds.get(1).getText();
                            String secondPlayerWinningOdd = odds.get(4).getText();

                            if (firstPlayerWinningOdd.trim().matches("-?\\d+(\\.\\d+)?")
                                    && secondPlayerWinningOdd.trim().matches("-?\\d+(\\.\\d+)?")) {

                                TableTennisEventEntity tableTennisEvent =  new TableTennisEventEntity();
                                tableTennisEvent.setFirstPlayerName(firstPlayerName);
                                tableTennisEvent.setSecondPlayerName(secondPlayerName);
                                tableTennisEvent.setFirstPlayerWinningOdd(Double.parseDouble(firstPlayerWinningOdd.trim()));
                                tableTennisEvent.setSecondPlayerWinningOdd(Double.parseDouble(secondPlayerWinningOdd.trim()));

                                tableTennisEvent.setEventDate(eventDate);

                                tableTennisEvent.setResultEntity(resultEntityHashSet);

                                tableTennisEventEntities.add(tableTennisEvent);

                            }
                        }
                    }
                }
            }
            resultEntity.setTableTennisEventEntitySet(tableTennisEventEntities);

            this.tableTennisEventEntityRepository.saveAllAndFlush(resultEntity.getTableTennisEventEntitySet());

            this.resultEntityRepository.saveAndFlush(resultEntity);
        } catch (Exception e) {
            resultEntity.setException(e);
        }

        return httpService.serializeResultEnt(resultEntity);
    }
}
