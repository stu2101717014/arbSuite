package com.example.williamhillexperimental.services;

import dtos.ResultEntityDTO;
import dtos.TableTennisEventEntityDTO;
import io.webfolder.ui4j.api.browser.BrowserEngine;
import io.webfolder.ui4j.api.browser.BrowserFactory;
import io.webfolder.ui4j.api.browser.Page;
import io.webfolder.ui4j.api.dom.Element;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@EnableScheduling
public class WilliamHillService {

    private static final String WILLIAM_HILL_TABLE_TENNIS_REQUEST_URL = "https://sports.williamhill.com/betting/en-gb/table-tennis/matches";

    private static final String PLATFORM_NAME = "WilliamHill";

    public static final int DELAY = 30000;

    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private final HttpServiceImpl httpService;

    @Autowired
    public WilliamHillService(HttpServiceImpl httpService,
                              RabbitTemplate rabbitTemplate,
                              Binding binding) {
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
        this.httpService = httpService;
    }

    @Scheduled(fixedDelay = DELAY)
    public void getTableTennisData() {

        ResultEntityDTO resultEntityDTO = new ResultEntityDTO();
        HashSet<TableTennisEventEntityDTO> tableTennisEventEntities = new HashSet<>();


        HashSet<ResultEntityDTO> resultEntityDTOHashSet = new HashSet<>();
        resultEntityDTOHashSet.add(resultEntityDTO);

        Date date = new Date(System.currentTimeMillis());
        resultEntityDTO.setTime(date);

        BrowserEngine browser = BrowserFactory.getWebKit();

        this.dataExtraction(resultEntityDTO, tableTennisEventEntities, resultEntityDTOHashSet, browser);

        String message = this.httpService.serializeResultEnt(resultEntityDTO);

        rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);
    }

    public static double round(double value, int places) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void dataExtraction(ResultEntityDTO resultEntityDTO, HashSet<TableTennisEventEntityDTO> tableTennisEventEntities, HashSet<ResultEntityDTO> resultEntityDTOHashSet, BrowserEngine browser) {
        resultEntityDTO.setPlatformName(PLATFORM_NAME);

        try (Page page = browser.navigate(WILLIAM_HILL_TABLE_TENNIS_REQUEST_URL)) {

            List<Element> elements = page.getDocument().queryAll("div.btmarket");

            for (Element el : elements) {
                try {
                    List<Element> dateEls = el.queryAll("span.btmarket__name.btmarket__name--disabled");
                    if (dateEls.size() > 0) {

                        Element element = dateEls.get(0);
                        String text = element.getText();

                        Date eventDate = parseEventDate(text);

                        List<Element> playerNamesAsList = el.queryAll("div.btmarket__link-name");
                        if (playerNamesAsList.size() > 0) {

                            Element playerNames = playerNamesAsList.get(0);
                            String playerNamesAsString = playerNames.getText();

                            String[] playersParts = playerNamesAsString.split("\\s+");

                            if (playersParts.length == 4) {
                                String firstPlayerName = playersParts[0] + " " + playersParts[1];
                                String secondPlayerName = playersParts[2] + " " + playersParts[3];

                                List<Element> oddsList = el.queryAll("div.btmarket__selection");

                                if (oddsList.size() == 2) {
                                    Double firstPlayerOdd = round(parseFraction(oddsList.get(0).getText().trim()) + 1, 2);
                                    Double secondPlayerOdd = round(parseFraction(oddsList.get(1).getText().trim()) + 1, 2);

                                    TableTennisEventEntityDTO tableTennisEventEntity = new TableTennisEventEntityDTO();
                                    tableTennisEventEntity.setFirstPlayerName(firstPlayerName);
                                    tableTennisEventEntity.setFirstPlayerWinningOdd(firstPlayerOdd);
                                    tableTennisEventEntity.setSecondPlayerName(secondPlayerName);
                                    tableTennisEventEntity.setSecondPlayerWinningOdd(secondPlayerOdd);
                                    tableTennisEventEntity.setEventDate(eventDate);

                                    tableTennisEventEntity.setResultEntity(resultEntityDTOHashSet);

                                    tableTennisEventEntities.add(tableTennisEventEntity);
                                }
                            }
                        }
                    }

                    resultEntityDTO.setTableTennisEventEntitySet(tableTennisEventEntities);


                } catch (Exception e) {

                    resultEntityDTO.setException(e);

                }

            }

        } catch (Exception e) {
            resultEntityDTO.setException(e);
        }
    }


    private double parseFraction(String ratio) {
        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
        }
        if (ratio.equals("EVS")) {
            return 1d;
        }
        return 0d;
    }

    private Date parseEventDate(String text) throws ParseException {
        Date eventDate;
        if (text.toLowerCase(Locale.ROOT).startsWith("live")) {
            eventDate = Calendar.getInstance().getTime();
        } else {
            if (text.toLowerCase(Locale.ROOT).endsWith("live")) {
                text = text.replace("Live", "");
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM HH:mm");
            eventDate = formatter.parse(text);

            Calendar instance = Calendar.getInstance();
            instance.setTime(eventDate);
            instance.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            eventDate = instance.getTime();
            if (Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER
                    && instance.get(Calendar.MONTH) == Calendar.JANUARY) {
                instance.set(Calendar.YEAR, (Calendar.getInstance().get(Calendar.YEAR) + 1));
                eventDate = instance.getTime();
            }
        }

        if (TimeZone.getDefault().inDaylightTime(new Date())) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(eventDate);
            cal.add(Calendar.HOUR, -1);
            return cal.getTime();
        }

        return eventDate;
    }
}
