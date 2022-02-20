package com.example.betwinnerexperimental.services;

import com.example.betwinnerexperimental.data.ResultEntity;
import com.example.betwinnerexperimental.data.ResultEntityRepository;
import com.example.betwinnerexperimental.data.TableTennisEventEntity;
import com.example.betwinnerexperimental.data.TableTennisEventEntityRepository;
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
    private static String BET_WINNER_TABLE_TENNIS_REQUEST_URL = "https://betwinner-ua.com/line/Table-Tennis/";

    private static final String PLATFORM_NAME = "BetWinner";

    public static final int DELAY = 30000;

    private ResultEntityRepository resultEntityRepository;

    private TableTennisEventEntityRepository tableTennisEventEntityRepository;

    private HttpService httpService;

    private RabbitTemplate rabbitTemplate;

    private Binding binding;

    @Autowired
    public BetwinnerService(ResultEntityRepository resultEntityRepository,
                            TableTennisEventEntityRepository tableTennisEventEntityRepository,
                            HttpService httpService,
                            RabbitTemplate rabbitTemplate,
                            Binding binding) {
        this.resultEntityRepository = resultEntityRepository;
        this.tableTennisEventEntityRepository = tableTennisEventEntityRepository;
        this.httpService = httpService;
        this.rabbitTemplate = rabbitTemplate;
        this.binding = binding;
    }

    @Scheduled(fixedDelay = DELAY)
    public void createAndExecuteTableTennisUserStory() {
        ResultEntity resultEntity = new ResultEntity();

        try {
            Date date = new Date(System.currentTimeMillis());
            resultEntity.setTime(date);

            HashSet<TableTennisEventEntity> tableTennisEventEntities = new HashSet<>();
            HashSet<ResultEntity> resultEntityHashSet = new HashSet<>();
            resultEntityHashSet.add(resultEntity);

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

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.getDefault());
                        eventDate = formatter.parse(finalDateAsString);

                        eventDate = new Date(eventDate.getTime() - TimeUnit.HOURS.toMillis(1));
                    }


                    List<Element> teamsEls = el.queryAll("span.c-events__teams");
                    List<Element> betsEls = el.queryAll("span.c-bets__bet.c-bets__bet_coef");

                    String playerNames = teamsEls.get(0).getText();
                    String playerNamesAsArray[] = playerNames.split("\\r?\\n", -1);


                    final String regex = "(\\d+.\\d+)-(\\d+.\\d+)\\d.";

                    if (betsEls.size() >= 1) {
                        Double firstPlayerBet = Double.parseDouble(betsEls.get(0).getText());
                        Double secondPlayerBet = Double.parseDouble(betsEls.get(1).getText());

                        if ((firstPlayerBet + secondPlayerBet) == 0.0d) {
                            continue;
                        }

                        TableTennisEventEntity tableTennisEvent = new TableTennisEventEntity();
                        tableTennisEvent.setFirstPlayerName(playerNamesAsArray[1].trim());
                        tableTennisEvent.setSecondPlayerName(playerNamesAsArray[3].trim());
                        tableTennisEvent.setFirstPlayerWinningOdd(firstPlayerBet);
                        tableTennisEvent.setSecondPlayerWinningOdd(secondPlayerBet);
                        tableTennisEvent.setEventDate(eventDate);

                        tableTennisEvent.setResultEntity(resultEntityHashSet);

                        tableTennisEventEntities.add(tableTennisEvent);
                    }

                }
            }

            resultEntity.setTableTennisEventEntitySet(tableTennisEventEntities);

            this.tableTennisEventEntityRepository.saveAllAndFlush(resultEntity.getTableTennisEventEntitySet());

            this.resultEntityRepository.saveAndFlush(resultEntity);

            String message = httpService.serializeResultEnt(resultEntity);

            Map.Entry<String, String> platformNameResultMessagePair = new AbstractMap.SimpleEntry<String, String>(PLATFORM_NAME, message);

            rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), platformNameResultMessagePair);

        } catch (Exception e) {
            resultEntity.setException(e);
        }
    }
}
