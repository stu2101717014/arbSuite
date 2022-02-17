package org.example.commons.williamhill.services;

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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WilliamHillService {

    private static String WILLIAM_HILL_TABLE_TENNIS_REQUEST_URL = "https://sports.williamhill.com/betting/en-gb/table-tennis/matches";

    private HttpService httpService;

    private ResultEntityRepository resultEntityRepository;

    private TableTennisEventEntityRepository tableTennisEventEntityRepository;

    @Autowired
    public WilliamHillService(HttpService httpService, ResultEntityRepository resultEntityRepository, TableTennisEventEntityRepository tableTennisEventEntityRepository) {
        this.httpService = httpService;
        this.resultEntityRepository = resultEntityRepository;
        this.tableTennisEventEntityRepository = tableTennisEventEntityRepository;
    }

    public String createAndExecuteTableTennisUserStory() {
        ResultEntity resultEntity = new ResultEntity();
        HashSet<TableTennisEventEntity> tableTennisEventEntities =  new HashSet<>();

        HashSet<ResultEntity> resultEntityHashSet = new HashSet<>();
        resultEntityHashSet.add(resultEntity);

        Date date = new Date(System.currentTimeMillis());
        resultEntity.setTime(date);

        BrowserEngine browser = BrowserFactory.getWebKit();

        try (Page page = browser.navigate(WILLIAM_HILL_TABLE_TENNIS_REQUEST_URL)) {

            List<Element> elements = page.getDocument().queryAll("div.btmarket");

            for(Element el : elements){
                try{
                    List<Element> dateEls = el.queryAll("span.btmarket__name.btmarket__name--disabled");
                    if (dateEls.size() > 0){

                        Element element = dateEls.get(0);
                        String text = element.getText();

                        Date eventDate = parseEventDate(text);

                        List<Element> playerNamesAsList = el.queryAll("div.btmarket__link-name");
                        if (playerNamesAsList.size() > 0){

                            Element playerNames = playerNamesAsList.get(0);
                            String playerNamesAsString = playerNames.getText();

                            String[] playersParts = playerNamesAsString.split("\\s+");

                            if (playersParts.length == 4){
                                String firstPlayerName = playersParts[0] + " " + playersParts[1];
                                String secondPlayerName = playersParts[2] + " " + playersParts[3];

                                List<Element> oddsList = el.queryAll("div.btmarket__selection");

                                if (oddsList.size() == 2){
                                    Double firstPlayerOdd = round(parseFraction(oddsList.get(0).getText().trim()) + 1, 2);
                                    Double secondPlayerOdd = round(parseFraction(oddsList.get(1).getText().trim()) + 1, 2);

                                    TableTennisEventEntity tableTennisEventEntity = new TableTennisEventEntity();
                                    tableTennisEventEntity.setFirstPlayerName(firstPlayerName);
                                    tableTennisEventEntity.setFirstPlayerWinningOdd(firstPlayerOdd);
                                    tableTennisEventEntity.setSecondPlayerName(secondPlayerName);
                                    tableTennisEventEntity.setSecondPlayerWinningOdd(secondPlayerOdd);
                                    tableTennisEventEntity.setEventDate(eventDate);

                                    tableTennisEventEntity.setResultEntity(resultEntityHashSet);

                                    tableTennisEventEntities.add(tableTennisEventEntity);
                                }
                            }
                        }
                    }

                    resultEntity.setTableTennisEventEntitySet(tableTennisEventEntities);


                }catch (Exception e){
                    resultEntity.setException(e);
                }

            }

        } catch (Exception e) {
            resultEntity.setException(e);
        }
        this.tableTennisEventEntityRepository.saveAllAndFlush(resultEntity.getTableTennisEventEntitySet());

        this.resultEntityRepository.saveAndFlush(resultEntity);

        return httpService.serializeResultEnt(resultEntity);
    }


    public static double round(double value, int places) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private double parseFraction(String ratio) {
        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        } if (ratio.equals("EVS")){
            return 1d;
        }
        return 0d;
    }

    private Date parseEventDate(String text) throws ParseException {
        Date eventDate;
        if (text.toLowerCase(Locale.ROOT).startsWith("live")){
            eventDate = Calendar.getInstance().getTime();
        }else {
            if (text.toLowerCase(Locale.ROOT).endsWith("live")){
                text = text.replace("Live", "");
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM HH:mm");
            eventDate = formatter.parse(text);

            Calendar instance = Calendar.getInstance();
            instance.setTime(eventDate);
            instance.set(Calendar.YEAR,Calendar.getInstance().get(Calendar.YEAR));
            eventDate = instance.getTime();
            if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER
                    && instance.get(Calendar.MONTH) == Calendar.JANUARY){
                instance.set(Calendar.YEAR,(Calendar.getInstance().get(Calendar.YEAR) + 1));
                eventDate = instance.getTime();
            }
        }
        return eventDate;
    }
}
