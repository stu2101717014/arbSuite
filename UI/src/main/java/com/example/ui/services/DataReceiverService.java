package com.example.ui.services;

import com.example.ui.entities.jpa.ResultEntityDAO;

import com.example.ui.repos.ResultEntityRepository;
import com.example.ui.repos.TableTennisEventEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DataReceiverService {

    private final ResultEntityRepository resultEntityRepository;

    private final TableTennisEventEntityRepository tableTennisEventEntityRepository;

    @Autowired
    public DataReceiverService(ResultEntityRepository resultEntityRepository, TableTennisEventEntityRepository tableTennisEventEntityRepository) {
        this.resultEntityRepository = resultEntityRepository;
        this.tableTennisEventEntityRepository = tableTennisEventEntityRepository;
    }

    public void persistResultEntity(ResultEntityDAO resultEntity) {
        this.tableTennisEventEntityRepository.saveAllAndFlush(resultEntity.getTableTennisEventEntitySet());
        this.resultEntityRepository.saveAndFlush(resultEntity);
    }

}
