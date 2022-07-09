package com.example.dp.services;

import com.example.dp.services.interfaces.ArbitrageService;
import dtos.TableTennisEventEntityDTO;
import dtos.TableTennisEventWrapperDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ArbitrageServiceImplTest {

    @Autowired
    public ArbitrageService arbitrageService;


    @Test
    public void invokeCheckForArbitrageShouldNotThrowException() {
        List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOS = new ArrayList<>();

        assertDoesNotThrow(() -> arbitrageService.checkForArbitrage(tableTennisEventWrapperDTOS));
    }

    @Test
    public void invokeCheckForArbitrageShouldCalculateArbitrageCorrectly() throws Exception {
        List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOS = new ArrayList<>();
        getTestData(tableTennisEventWrapperDTOS);

        arbitrageService.checkForArbitrage(tableTennisEventWrapperDTOS);

        TableTennisEventWrapperDTO tableTennisEventWrapperDTO1 = tableTennisEventWrapperDTOS.get(0);
        Double arbitragePercentage = tableTennisEventWrapperDTO1.getArbitragePercentage();
        double roundDbl = Math.round(arbitragePercentage * 100.0) / 100.0;

        assertEquals(0.91, roundDbl);
    }

    @Test
    public void invokeCheckForArbitrageShouldCalculateArbitrageCorrectlyForSecondPlayer() throws Exception {
        List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOS = new ArrayList<>();
        getTestData(tableTennisEventWrapperDTOS, 1.6, 2.2, 2.2, 1.6);

        arbitrageService.checkForArbitrage(tableTennisEventWrapperDTOS);

        TableTennisEventWrapperDTO tableTennisEventWrapperDTO1 = tableTennisEventWrapperDTOS.get(0);
        Double arbitragePercentage = tableTennisEventWrapperDTO1.getArbitragePercentage();
        double roundDbl = Math.round(arbitragePercentage * 100.0) / 100.0;

        assertEquals(0.91, roundDbl);
    }

    @Test
    public void invokeCalculateBetsShouldCalculateBetsCorrectly() throws Exception {
        List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOS = new ArrayList<>();
        getTestData(tableTennisEventWrapperDTOS);
        arbitrageService.checkForArbitrage(tableTennisEventWrapperDTOS);
        TableTennisEventWrapperDTO tableTennisEventWrapperDTO1 = tableTennisEventWrapperDTOS.get(0);
        Double arbitragePercentage = tableTennisEventWrapperDTO1.getArbitragePercentage();

        List<Double> stakes = arbitrageService.calculateBets(100, Arrays.asList(2.2, 2.2), arbitragePercentage);

        assertEquals(50, stakes.get(0));
        assertEquals(50, stakes.get(1));

    }

    @Test
    public void invokeCalculateBetsShouldCalculateBetsCorrectly2() throws Exception {
        List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOS = new ArrayList<>();
        getTestData(tableTennisEventWrapperDTOS, 1, 3, 2, 1.8);
        arbitrageService.checkForArbitrage(tableTennisEventWrapperDTOS);
        TableTennisEventWrapperDTO tableTennisEventWrapperDTO1 = tableTennisEventWrapperDTOS.get(0);
        Double arbitragePercentage = tableTennisEventWrapperDTO1.getArbitragePercentage();

        List<Double> stakes = arbitrageService.calculateBets(100, Arrays.asList(2.0, 3.0), arbitragePercentage);

        assertEquals(60, Math.round(stakes.get(0)));
        assertEquals(40, Math.round(stakes.get(1)));
    }

    private void getTestData(List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOS
            , double fefo, double feso, double sefo, double seso) throws ParseException {

        TableTennisEventWrapperDTO tableTennisEventWrapperDTO = new TableTennisEventWrapperDTO();
        tableTennisEventWrapperDTO.setEventEntityMap(new HashMap<>());

        TableTennisEventEntityDTO tableTennisEventEntityDTO = new TableTennisEventEntityDTO();
        tableTennisEventEntityDTO.setFirstPlayerName("First Player");
        tableTennisEventEntityDTO.setSecondPlayerName("Second Player");
        String dateAsString = "30-May-2022 23:37:50";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        tableTennisEventEntityDTO.setEventDate(formatter.parse(dateAsString));
        tableTennisEventEntityDTO.setFirstPlayerWinningOdd(fefo);
        tableTennisEventEntityDTO.setSecondPlayerWinningOdd(feso);
        tableTennisEventWrapperDTO.getEventEntityMap().put("Test Platform", tableTennisEventEntityDTO);

        TableTennisEventEntityDTO tableTennisEventEntityDTO2 = new TableTennisEventEntityDTO();
        tableTennisEventEntityDTO2.setFirstPlayerName("First Player");
        tableTennisEventEntityDTO2.setSecondPlayerName("Second Player");
        tableTennisEventEntityDTO2.setEventDate(formatter.parse(dateAsString));
        tableTennisEventEntityDTO2.setFirstPlayerWinningOdd(sefo);
        tableTennisEventEntityDTO2.setSecondPlayerWinningOdd(seso);
        tableTennisEventWrapperDTO.getEventEntityMap().put("Test Platform 2", tableTennisEventEntityDTO2);

        tableTennisEventWrapperDTOS.add(tableTennisEventWrapperDTO);
    }

    private void getTestData(List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOS) throws ParseException {
        getTestData(tableTennisEventWrapperDTOS, 2.2, 1.6, 1.6, 2.2);
    }
}
