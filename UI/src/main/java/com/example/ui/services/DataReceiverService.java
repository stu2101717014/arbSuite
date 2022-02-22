package com.example.ui.services;

import com.example.ui.entities.helpers.ResultEntity;
import com.example.ui.entities.helpers.TableTennisEventEntity;
import com.example.ui.entities.jpa.ResultEntityDAO;
import com.example.ui.entities.jpa.TableTennisEventEntityDAO;
import com.example.ui.repos.ResultEntityRepository;
import com.example.ui.repos.TableTennisEventEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DataReceiverService {

    private final ResultEntityRepository resultEntityRepository;

    private final TableTennisEventEntityRepository tableTennisEventEntityRepository;

    @Autowired
    public DataReceiverService(ResultEntityRepository resultEntityRepository, TableTennisEventEntityRepository tableTennisEventEntityRepository) {
        this.resultEntityRepository = resultEntityRepository;
        this.tableTennisEventEntityRepository = tableTennisEventEntityRepository;
    }

    public void persistResultEntity(ResultEntity resultEntity) {
        Set<TableTennisEventEntityDAO> resultSet = new HashSet<>();

        ResultEntityDAO resultEntityDAO = new ResultEntityDAO();
        resultEntityDAO.setException(resultEntity.getException());
        resultEntityDAO.setTime(resultEntity.getTime());
        resultEntityDAO.setPlatformName(resultEntity.getPlatformName());

        Set<ResultEntityDAO> resultEntityDAOSet = new HashSet<>();
        resultEntityDAOSet.add(resultEntityDAO);

        for (TableTennisEventEntity ttee : resultEntity.getTableTennisEventEntitySet()) {
            TableTennisEventEntityDAO tableTennisEventEntity = new TableTennisEventEntityDAO();
            tableTennisEventEntity.setEventDate(ttee.getEventDate());
            tableTennisEventEntity.setFirstPlayerName(ttee.getFirstPlayerName());
            tableTennisEventEntity.setSecondPlayerName(ttee.getSecondPlayerName());
            tableTennisEventEntity.setFirstPlayerWinningOdd(ttee.getFirstPlayerWinningOdd());
            tableTennisEventEntity.setSecondPlayerWinningOdd(ttee.getSecondPlayerWinningOdd());
            tableTennisEventEntity.setResultEntity(resultEntityDAOSet);
            resultSet.add(tableTennisEventEntity);
        }

        resultEntityDAO.setTableTennisEventEntitySet(resultSet);

        this.tableTennisEventEntityRepository.saveAllAndFlush(resultSet);

        this.resultEntityRepository.saveAndFlush(resultEntityDAO);
    }
}
