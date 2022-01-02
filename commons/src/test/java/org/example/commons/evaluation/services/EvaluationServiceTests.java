package org.example.commons.evaluation.services;


import org.example.commons.entities.ResultEntity;
import org.example.commons.entities.TableTennisEventEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = org.example.commons.evaluation.services.TestConfig.class)
@Import(org.example.commons.evaluation.services.TestConfig.class)
public class EvaluationServiceTests {

    @Autowired
    private EvaluationService evaluationService;

    @Test
    public void EvaluateTableTennisDataStrategyExactMatchNoSwitchingPositions_whenEvaluate_thenNotThrowException()  {
        assertDoesNotThrow(() -> evaluationService.EvaluateTableTennisDataStrategyExactMatchNoSwitchingPositions(new ArrayList<>()));

    }

    @Test
    public void EvaluateTableTennisDataStrategyExactMatchNoSwitchingPositions_whenEvaluateWithPreparedData_thenShouldReturnExpectedResult()  {
        List<ResultEntity> preparedData = prepareData();

        HashMap<String, HashMap<String, List<TableTennisEventEntity>>> resultData =
                evaluationService.EvaluateTableTennisDataStrategyExactMatchNoSwitchingPositions(preparedData);

        assertEquals(1, resultData.size());
        assertEquals( 1, resultData.get("First Player Name").size());
        assertEquals( 2, resultData.get("First Player Name").get("Second Player Name").size());
    }

    @Test
    public void EvaluateTableTennisDataStrategyExactMatchNoSwitchingPositions_whenEvaluateWithPreparedData_thenShouldReturnExpectedResult2()  {
        List<ResultEntity> preparedData = prepareData2();

        HashMap<String, HashMap<String, List<TableTennisEventEntity>>> resultData =
                evaluationService.EvaluateTableTennisDataStrategyExactMatchNoSwitchingPositions(preparedData);

        assertEquals(1, resultData.size());
        assertEquals( 2, resultData.get("First Player Name").size());
        assertEquals( 1, resultData.get("First Player Name").get("Second Player Name").size());
        assertEquals( 1, resultData.get("First Player Name").get("Third Player Name").size());
    }

    @Test
    public void EvaluateTableTennisDataStrategyExactMatchNoSwitchingPositions_whenEvaluateWithPreparedData_thenShouldReturnExpectedResult3()  {
        List<ResultEntity> preparedData = prepareData3();

        HashMap<String, HashMap<String, List<TableTennisEventEntity>>> resultData =
                evaluationService.EvaluateTableTennisDataStrategyExactMatchNoSwitchingPositions(preparedData);

        assertEquals(1, resultData.size());
        assertEquals( 2, resultData.get("First Player Name").size());
        assertEquals( 2, resultData.get("First Player Name").get("Second Player Name").size());
        assertEquals( 1, resultData.get("First Player Name").get("Third Player Name").size());
    }

    private List<ResultEntity> prepareData3() {
        List<ResultEntity> data = new ArrayList<>();
        ResultEntity resultEntity1 = new ResultEntity();
        TableTennisEventEntity ttee1 = new TableTennisEventEntity();
        ttee1.setFirstPlayerName("First Player Name");
        ttee1.setSecondPlayerName("Second Player Name");
        ResultEntity resultEntity2 = new ResultEntity();
        HashSet<TableTennisEventEntity> ttset = new HashSet<TableTennisEventEntity>();
        ttset.add(ttee1);
        resultEntity1.setTableTennisEventEntitySet(ttset);

        TableTennisEventEntity ttee2 = new TableTennisEventEntity();
        ttee2.setFirstPlayerName("First Player Name");
        ttee2.setSecondPlayerName("Third Player Name");

        HashSet<TableTennisEventEntity> ttset2 = new HashSet<>();
        ttset2.add(ttee2);

        resultEntity2.setTableTennisEventEntitySet(ttset2);

        data.add(resultEntity1);
        data.add(resultEntity1);
        data.add(resultEntity2);
        return data;
    }

    private List<ResultEntity> prepareData2() {
        List<ResultEntity> data = new ArrayList<>();
        ResultEntity resultEntity1 = new ResultEntity();
        TableTennisEventEntity ttee1 = new TableTennisEventEntity();
        ttee1.setFirstPlayerName("First Player Name");
        ttee1.setSecondPlayerName("Second Player Name");
        ResultEntity resultEntity2 = new ResultEntity();
        HashSet<TableTennisEventEntity> ttset = new HashSet<TableTennisEventEntity>();
        ttset.add(ttee1);
        resultEntity1.setTableTennisEventEntitySet(ttset);

        TableTennisEventEntity ttee2 = new TableTennisEventEntity();
        ttee2.setFirstPlayerName("First Player Name");
        ttee2.setSecondPlayerName("Third Player Name");

        HashSet<TableTennisEventEntity> ttset2 = new HashSet<>();
        ttset2.add(ttee2);

        resultEntity2.setTableTennisEventEntitySet(ttset2);
        data.add(resultEntity1);
        data.add(resultEntity2);
        return data;
    }

    private List<ResultEntity> prepareData() {
        List<ResultEntity> data = new ArrayList<>();
        ResultEntity resultEntity1 = new ResultEntity();
        TableTennisEventEntity ttee1 = new TableTennisEventEntity();
        ttee1.setFirstPlayerName("First Player Name");
        ttee1.setSecondPlayerName("Second Player Name");
        ResultEntity resultEntity2 = new ResultEntity();
        HashSet<TableTennisEventEntity> ttset = new HashSet<TableTennisEventEntity>();
        ttset.add(ttee1);
        resultEntity1.setTableTennisEventEntitySet(ttset);
        resultEntity2.setTableTennisEventEntitySet(ttset);
        data.add(resultEntity1);
        data.add(resultEntity2);
        return data;
    }
}


@SpringBootConfiguration
@ComponentScan(basePackages ={"org.example.commons.services", "org.example.commons.evaluation.services"})
class TestConfig {
}
