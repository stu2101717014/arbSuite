package com.example.betwinnerexperimental.services;

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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@EnableScheduling
public class BetwinnerService {
    private static final String BET_WINNER_TABLE_TENNIS_REQUEST_URL = "https://betwinner-ua.com/line/Table-Tennis/";

    private static final String PLATFORM_NAME = "BetWinner";

    public static final int DELAY = 30000;

    private final RabbitTemplate rabbitTemplate;

    private final Binding binding;

    private final HttpService httpService;

    @Autowired
    public BetwinnerService(HttpService httpService,
                            RabbitTemplate rabbitTemplate,
                            Binding binding) {
        this.httpService = httpService;
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
    }

    @Scheduled(fixedDelay = DELAY)
    public void getTableTennisData() {
        ResultEntityDTO resultEntityDTO = new ResultEntityDTO();

        try {
            Date date = new Date(System.currentTimeMillis());
            resultEntityDTO.setTime(date);

            HashSet<TableTennisEventEntityDTO> tableTennisEventEntities = new HashSet<>();
            HashSet<ResultEntityDTO> resultEntityDTOHashSet = new HashSet<>();
            resultEntityDTOHashSet.add(resultEntityDTO);

            BrowserEngine browser = BrowserFactory.getWebKit();

            try (Page page = browser.navigate(BET_WINNER_TABLE_TENNIS_REQUEST_URL)) {

                List<Element> elements = page.getDocument().queryAll("div.c-events__item.c-events__item_game");

                for (Element el : elements) {

                    Date eventDate = null;
                    List<Element> dateOfEventEls = el.queryAll("div.c-events__time.min");
                    if (!dateOfEventEls.isEmpty()) {
                        String eventDateEls = dateOfEventEls.get(0).getText();

                        eventDateEls = eventDateEls.replace("\n", "");
                        String[] eventDateParts = eventDateEls.split(" ", -1);
                        List<String> eventDatePartsAsList = Arrays.stream(eventDateParts).filter(e -> !e.equals("")).collect(Collectors.toList());
                        int year = Calendar.getInstance().get(Calendar.YEAR);

                        String finalDateAsString = eventDatePartsAsList.get(0).replace(".", "-") + "-" + year + " " + eventDatePartsAsList.get(1);

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                        eventDate = formatter.parse(finalDateAsString);

                        eventDate = new Date(eventDate.getTime() - TimeUnit.HOURS.toMillis(1));
                    }


                    List<Element> teamsEls = el.queryAll("span.c-events__teams");
                    List<Element> betsEls = el.queryAll("span.c-bets__bet.c-bets__bet_coef");

                    String playerNames = teamsEls.get(0).getText();
                    String[] playerNamesAsArray = playerNames.split("\\r?\\n", -1);

                    if (betsEls.size() >= 1) {
                        Double firstPlayerBet = Double.parseDouble(betsEls.get(0).getText());
                        Double secondPlayerBet = Double.parseDouble(betsEls.get(1).getText());

                        if ((firstPlayerBet + secondPlayerBet) == 0.0d) {
                            continue;
                        }

                        TableTennisEventEntityDTO tableTennisEvent = new TableTennisEventEntityDTO();
                        tableTennisEvent.setFirstPlayerName(playerNamesAsArray[1].trim());
                        tableTennisEvent.setSecondPlayerName(playerNamesAsArray[3].trim());
                        tableTennisEvent.setFirstPlayerWinningOdd(firstPlayerBet);
                        tableTennisEvent.setSecondPlayerWinningOdd(secondPlayerBet);
                        tableTennisEvent.setEventDate(eventDate);

                        tableTennisEvent.setResultEntity(resultEntityDTOHashSet);

                        tableTennisEventEntities.add(tableTennisEvent);
                    }
                }
            }

            resultEntityDTO.setTableTennisEventEntitySet(tableTennisEventEntities);

            resultEntityDTO.setPlatformName(PLATFORM_NAME);

            String message = this.httpService.serializeResultEnt(resultEntityDTO);

            rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);

        } catch (Exception e) {
            resultEntityDTO.setException(e);
        }
    }
}
