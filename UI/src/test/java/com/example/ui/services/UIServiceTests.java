package com.example.ui.services;

import com.example.ui.entities.FourPlatformsEventWrapper;
import com.example.ui.entities.RequestDataResult;
import com.example.ui.entities.ResultEntity;
import com.example.ui.entities.TableTennisEventEntity;
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

import static org.springframework.test.util.AssertionErrors.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UIServiceTests {


    @Test
    void uiService_integration_test() {
        UIService uiService = new UIService(new DataRequestServiceImpl());

        List<FourPlatformsEventWrapper>  eventWrapperTreeSet = uiService.reshapeTableTennisEventsData(testData());
        int size = eventWrapperTreeSet.size();

        assertTrue("Size must equals one", size == 1);

    }


    @Configuration
    public class NameServiceTestConfiguration {
        @Bean
        @Primary
        public DataRequestService nameService() {
            return Mockito.mock(DataRequestService.class);
        }
    }

    private RequestDataResult testData(){
        Date time = Calendar.getInstance().getTime();
        RequestDataResult requestDataResult = new RequestDataResult();

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

        requestDataResult.setBwinResult(bwinEntity);
        requestDataResult.setBets22Result(bets22ResultEnt);

        return requestDataResult;
    }
}
