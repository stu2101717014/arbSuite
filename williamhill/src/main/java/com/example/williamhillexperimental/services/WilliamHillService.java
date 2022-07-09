package com.example.williamhillexperimental.services;

import dtos.ResultEntityDTO;
import dtos.TableTennisEventEntityDTO;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WilliamHillService implements ApplicationRunner {

    private static final String WILLIAM_HILL_TABLE_TENNIS_REQUEST_URL = "https://sports.williamhill.com/betting/en-gb/table-tennis/matches";

    private static final String PLATFORM_NAME = "WilliamHill";

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

    @Override
    public void run(ApplicationArguments args) {
        getTableTennisData();
    }

    public void getTableTennisData() {

        ResultEntityDTO resultEntityDTO = new ResultEntityDTO();
        HashSet<TableTennisEventEntityDTO> tableTennisEventEntities = new HashSet<>();

        Date date = new Date(System.currentTimeMillis());
        resultEntityDTO.setTime(date);

        String responseAsString = httpService.getResponseAsString(WILLIAM_HILL_TABLE_TENNIS_REQUEST_URL);

        this.dataExtraction(resultEntityDTO, tableTennisEventEntities, responseAsString);

        resultEntityDTO.setTableTennisEventEntitySet(tableTennisEventEntities);
        Set<ResultEntityDTO> resSet =  new HashSet<>();
        resSet.add(resultEntityDTO);
        tableTennisEventEntities.forEach(e -> e.setResultEntity(resSet));

        resultEntityDTO.setFinishedTime(new Date(System.currentTimeMillis()));

        String message = this.httpService.serializeResultEnt(resultEntityDTO);

        rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);

        System.exit(0);
    }

    public static double round(double value, int places) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void dataExtraction(ResultEntityDTO resultEntityDTO,
                                HashSet<TableTennisEventEntityDTO> tableTennisEventEntities,
                                String pageAsString
    ) {
        resultEntityDTO.setPlatformName(PLATFORM_NAME);

        try {

            final String regex = "datetime=\"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2})\"([\\s|\\S]*?)<div class=\"btmarket__link-name btmarket__link-name--2-rows\">[\\s|\\S]*?<span>([.|,|\\w\\s]*)<\\/span>[\\s|\\S]*?<span>([.|,|\\w\\s]*)<\\/span>[\\s|\\S]*?<\\/div>[\\s|\\S]*?((\\d+\\/\\d+)|EVS)\" data-name=\"[\\s|\\S]*?<\\/span>[\\s|\\S]*?((\\d+\\/\\d+)|EVS)";

            final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            final Matcher matcher = pattern.matcher(pageAsString);

            while (matcher.find()) {
                TableTennisEventEntityDTO tableTennisEventEntityDTO = new TableTennisEventEntityDTO();

                String dateAsString = matcher.group(1);
                String firstPlayer = matcher.group(3);
                String secondPlayer = matcher.group(4);

                String firstPlayerOdd = matcher.group(6);
                String secondPlayerOdd = matcher.group(8);

                if (firstPlayerOdd == null){
                    firstPlayerOdd = "1/1";
                }
                if (secondPlayerOdd == null){
                    secondPlayerOdd = "1/1";
                }


                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                Date eventDate = formatter.parse(dateAsString);
                Calendar c = Calendar.getInstance();
                c.setTime(eventDate);
                c.add(Calendar.HOUR, 2);
                tableTennisEventEntityDTO.setEventDate(c.getTime());
                tableTennisEventEntityDTO.setFirstPlayerName(firstPlayer);
                tableTennisEventEntityDTO.setSecondPlayerName(secondPlayer);
                tableTennisEventEntityDTO.setFirstPlayerWinningOdd(round(1 + this.parseFraction(firstPlayerOdd), 2));
                tableTennisEventEntityDTO.setSecondPlayerWinningOdd(round(1 + this.parseFraction(secondPlayerOdd), 2));

                tableTennisEventEntities.add(tableTennisEventEntityDTO);

            }

        } catch (Exception e) {
            resultEntityDTO.setException(e);
            e.printStackTrace();
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
}
