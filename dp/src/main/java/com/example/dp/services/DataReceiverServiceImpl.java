package com.example.dp.services;


import com.example.dp.data.entities.ResultEntityDAO;
import com.example.dp.data.repositories.ResultEntityRepository;
import com.example.dp.data.repositories.TableTennisEventEntityRepository;
import com.example.dp.services.interfaces.DataReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataReceiverServiceImpl implements DataReceiverService {

    private final ResultEntityRepository resultEntityRepository;

    private final TableTennisEventEntityRepository tableTennisEventEntityRepository;

    @Autowired
    public DataReceiverServiceImpl(ResultEntityRepository resultEntityRepository, TableTennisEventEntityRepository tableTennisEventEntityRepository) {
        this.resultEntityRepository = resultEntityRepository;
        this.tableTennisEventEntityRepository = tableTennisEventEntityRepository;
    }

    public void persistResultEntity(ResultEntityDAO resultEntity) {
        this.tableTennisEventEntityRepository.saveAllAndFlush(resultEntity.getTableTennisEventEntitySet());
        this.resultEntityRepository.saveAndFlush(resultEntity);
    }

}