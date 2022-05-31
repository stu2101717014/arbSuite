package com.example.dp.services;

import com.example.dp.services.interfaces.TableTennisService;
import dtos.ResultEntityDTO;
import dtos.TableTennisEventEntityDTO;
import dtos.TableTennisEventWrapperDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TableTennisServiceImplTest {

    @Autowired
    public TableTennisService tableTennisService;


    @Test
    public void invokeReshapeTableTennisEventsDataShouldNotThrowException() {

        List<ResultEntityDTO> resultEntityDAOList = new ArrayList<>();

        assertDoesNotThrow(() -> tableTennisService.reshapeTableTennisEventsData(resultEntityDAOList));
    }

    @Test
    public void invokeReshapeTableTennisEventsDataShouldConstructCorrectData() {

        List<ResultEntityDTO> resultEntityDAOList = getTableTennisEventWrapperDTOS("Test Platform");

        List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOS = tableTennisService.reshapeTableTennisEventsData(resultEntityDAOList);

        int tableTennisEventWrapperDTOListSize = tableTennisEventWrapperDTOS.size();
        int innerMapSize = tableTennisEventWrapperDTOS.get(0).getEventEntityMap().size();

        assertEquals(1, tableTennisEventWrapperDTOListSize);
        assertEquals(1, innerMapSize);
    }

    @Test
    public void invokeReshapeTableTennisEventsDataShouldConstructCorrectData2() {

        List<ResultEntityDTO> resultEntityDAOList = getTableTennisEventWrapperDTOS("Test Platform 1");
        resultEntityDAOList.addAll(getTableTennisEventWrapperDTOS("Test Platform 2"));

        List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOS = tableTennisService.reshapeTableTennisEventsData(resultEntityDAOList);

        int tableTennisEventWrapperDTOListSize = tableTennisEventWrapperDTOS.size();
        int innerMapSize = tableTennisEventWrapperDTOS.get(0).getEventEntityMap().size();

        assertEquals(1, tableTennisEventWrapperDTOListSize);
        assertEquals(2, innerMapSize);
    }

    @Test
    public void reshapeTableTennisEventsDataPerformanceTest() {

        double executionTime1 = getExecutionTime(10, 1000);
        double executionTime2 = getExecutionTime(10, 1000);
        double executionTime3 = getExecutionTime(10, 1000);

        double executionTime4 = getExecutionTime(100, 1000);
        double executionTime5 = getExecutionTime(100, 1000);
        double executionTime6 = getExecutionTime(100, 1000);

        double average1 = (executionTime1 + executionTime2 + executionTime3) / 3;

        double average2 = (executionTime4 + executionTime5 + executionTime6) / 3;

        double res1 = average2 / average1;

        assertTrue(res1 < 10);

    }


    @Test
    public void reshapeTableTennisEventsDataPerformanceTest2() {

        double executionTime1 = getExecutionTime(1000, 10);
        double executionTime2 = getExecutionTime(1000, 10);
        double executionTime3 = getExecutionTime(1000, 10);

        double executionTime4 = getExecutionTime(1000, 100);
        double executionTime5 = getExecutionTime(1000, 100);
        double executionTime6 = getExecutionTime(1000, 100);

        double average1 = (executionTime1 + executionTime2 + executionTime3) / 3;

        double average2 = (executionTime4 + executionTime5 + executionTime6) / 3;

        double res1 = average2 / average1;

        assertTrue(res1 < 10);

    }

    private long getExecutionTime(int countOfEvents, int countOfPlatforms) {
        List<ResultEntityDTO> resultEntityDAOList = getTableTennisEventWrapperDTOSet("Test Platform 1", countOfEvents);
        for (int i = 0; i < countOfPlatforms; i++) {
            resultEntityDAOList.addAll(getTableTennisEventWrapperDTOSet("Test Platform " + i, countOfEvents));
        }

        long startTime = System.currentTimeMillis();

        tableTennisService.reshapeTableTennisEventsData(resultEntityDAOList);

        return System.currentTimeMillis() - startTime;
    }


    private List<ResultEntityDTO> getTableTennisEventWrapperDTOSet(String platformName, int count) {
        List<ResultEntityDTO> resultEntityDTOList = new ArrayList<>();
        ResultEntityDTO resultEntityDTO = new ResultEntityDTO();
        resultEntityDTO.setPlatformName(platformName);

        Set<TableTennisEventEntityDTO> tableTennisEventEntityDTOSet = new HashSet<>();

        for (int i = 0; i < count; i++) {
            TableTennisEventEntityDTO tableTennisEventEntityDTO = new TableTennisEventEntityDTO();

            tableTennisEventEntityDTO.setFirstPlayerName("First Player " + i);
            tableTennisEventEntityDTO.setSecondPlayerName("Second Player " + i);
            try {
                String dateAsString = "30-May-2022 23:37:50";
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                tableTennisEventEntityDTO.setEventDate(formatter.parse(dateAsString));
            } catch (Exception e) {
                e.printStackTrace();
            }

            Set<ResultEntityDTO> resultEntityDTOSet = new HashSet<>();

            tableTennisEventEntityDTOSet.add(tableTennisEventEntityDTO);


            resultEntityDTOSet.add(resultEntityDTO);
            tableTennisEventEntityDTO.setResultEntity(resultEntityDTOSet);
        }

        resultEntityDTO.setTableTennisEventEntitySet(tableTennisEventEntityDTOSet);
        resultEntityDTOList.add(resultEntityDTO);

        return resultEntityDTOList;
    }

    private List<ResultEntityDTO> getTableTennisEventWrapperDTOS(String platformName) {
        List<ResultEntityDTO> resultEntityDTOList = new ArrayList<>();
        ResultEntityDTO resultEntityDTO = new ResultEntityDTO();
        resultEntityDTO.setPlatformName(platformName);

        Set<TableTennisEventEntityDTO> tableTennisEventEntityDTOSet = new HashSet<>();

        TableTennisEventEntityDTO tableTennisEventEntityDTO = new TableTennisEventEntityDTO();

        tableTennisEventEntityDTO.setFirstPlayerName("First Player");
        tableTennisEventEntityDTO.setSecondPlayerName("Second Player");
        try {
            String dateAsString = "30-May-2022 23:37:50";
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            tableTennisEventEntityDTO.setEventDate(formatter.parse(dateAsString));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Set<ResultEntityDTO> resultEntityDTOSet = new HashSet<>();

        tableTennisEventEntityDTOSet.add(tableTennisEventEntityDTO);
        resultEntityDTOSet.add(resultEntityDTO);
        tableTennisEventEntityDTO.setResultEntity(resultEntityDTOSet);
        resultEntityDTO.setTableTennisEventEntitySet(tableTennisEventEntityDTOSet);
        resultEntityDTOList.add(resultEntityDTO);

        return resultEntityDTOList;
    }
}

