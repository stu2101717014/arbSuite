package org.example.commons.evaluation.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = org.example.commons.evaluation.services.TestConfig.class)
@Import(org.example.commons.evaluation.services.TestConfig.class)
public class EvaluationServiceTests {

    @Test
    public void EvaluateTableTennisDataStrategyExactMatchNoSwitchingPositions_whenEvaluate_thenNotThrowException()  {

    }
}


@SpringBootConfiguration
@ComponentScan(basePackages ={"org.example.commons.services", "org.example.commons.evaluation.services"})
class TestConfig {
}
