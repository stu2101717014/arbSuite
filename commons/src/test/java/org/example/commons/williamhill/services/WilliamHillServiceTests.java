package org.example.commons.williamhill.services;

import org.example.commons.repos.ResultEntityRepository;
import org.example.commons.repos.TableTennisEventEntityRepository;
import org.example.commons.services.HttpService;
import org.example.commons.services.HttpServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = org.example.commons.williamhill.services.WilliamHillServiceTestsTestConfig.class)
@Import(org.example.commons.williamhill.services.WilliamHillServiceTestsTestConfig.class)
public class WilliamHillServiceTests {



    @Autowired
    private WilliamHillService williamHillService;

    @Test
    public void williamHills_IntegrationTest1()  {
        williamHillService.createAndExecuteTableTennisUserStory();
    }
}


@SpringBootConfiguration
@ComponentScan(basePackages ={"org.example.commons.services", "org.example.commons.repos", "org.example.commons.williamhill.services"})
class WilliamHillServiceTestsTestConfig {



}