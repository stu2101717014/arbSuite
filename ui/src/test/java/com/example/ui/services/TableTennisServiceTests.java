package com.example.ui.services;

import com.example.ui.entities.helpers.RequestDataResultDTO;
import com.example.ui.entities.helpers.ResultEntity;
import com.example.ui.entities.helpers.TableTennisEventEntity;
import com.example.ui.services.interfaces.DataRequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TableTennisServiceTests {


    @Test
    void uiService_integration_test() {
//        UIService uiService = new UIService(new DataRequestServiceImpl());
//
//        List<FourPlatformsEventWrapper>  eventWrapperTreeSet = uiService.reshapeTableTennisEventsData(testData());
//        int size = eventWrapperTreeSet.size();
//
//        assertTrue("Size must equals one", size == 1);

    }


    @Configuration
    public class NameServiceTestConfiguration {
        @Bean
        @Primary
        public DataRequestService nameService() {
            return Mockito.mock(DataRequestService.class);
        }
    }

    private RequestDataResultDTO testData(){
        Date time = Calendar.getInstance().getTime();
        RequestDataResultDTO requestDataResultDTO = new RequestDataResultDTO();

        ResultEntity bwinEntity = new ResultEntity();
        HashSet<TableTennisEventEntity> bwinTableTennisEventEntities = new HashSet<>();

        ResultEntity bets22ResultEnt = new ResultEntity();
        HashSet<TableTennisEventEntity> bets22TableTennisEventEntities = new HashSet<>();

        TableTennisEventEntity tableTennisEventEntity = new TableTennisEventEntity();
        tableTennisEventEntity.setEventDate(time);
        tableTennisEventEntity.setFirstPlayerName("Milen M. Hinkov");
        tableTennisEventEntity.setSecondPlayerName("Georgi M Georgiev");
        tableTennisEventEntity.setFirstPlayerWinningOdd(1.35D);
        tableTennisEventEntity.setSecondPlayerWinningOdd(3.2d);

        TableTennisEventEntity tableTennisEventEntity2 = new TableTennisEventEntity();
        tableTennisEventEntity2.setEventDate(time);
        tableTennisEventEntity2.setFirstPlayerName("Milen M. Hinkov");
        tableTennisEventEntity2.setSecondPlayerName("Georgi M Georgiev");
        tableTennisEventEntity2.setFirstPlayerWinningOdd(1.35D);
        tableTennisEventEntity2.setSecondPlayerWinningOdd(3.2d);

        bwinTableTennisEventEntities.add(tableTennisEventEntity);
        bwinTableTennisEventEntities.add(tableTennisEventEntity);
        bets22TableTennisEventEntities.add(tableTennisEventEntity2);

        bwinEntity.setTableTennisEventEntitySet(bwinTableTennisEventEntities);
        bets22ResultEnt.setTableTennisEventEntitySet(bets22TableTennisEventEntities);


        return requestDataResultDTO;
    }
}
