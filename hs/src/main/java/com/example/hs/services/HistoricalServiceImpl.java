package com.example.hs.services;

import com.example.hs.data.HistoricalTableTennisEventWrapperDAO;
import com.example.hs.data.HistoricalTableTennisEventWrapperRepository;
import com.google.gson.reflect.TypeToken;
import dtos.TableTennisEventWrapperDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class HistoricalServiceImpl implements HistoricalService {

    private final HistoricalTableTennisEventWrapperRepository historicalTableTennisEventWrapperRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public HistoricalServiceImpl(HistoricalTableTennisEventWrapperRepository historicalTableTennisEventWrapperRepository
            , ModelMapper modelMapper) {
        this.historicalTableTennisEventWrapperRepository = historicalTableTennisEventWrapperRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void persistPositiveArbitrageRecords(List<TableTennisEventWrapperDTO> eventWrapperList) {
        Type listType = new TypeToken<List<HistoricalTableTennisEventWrapperDAO>>() {
        }.getType();
        List<HistoricalTableTennisEventWrapperDAO> resList = modelMapper.map(eventWrapperList, listType);
        this.historicalTableTennisEventWrapperRepository.saveAllAndFlush(resList);
    }

    @Override
    public List<HistoricalTableTennisEventWrapperDAO> getAll() {
        return this.historicalTableTennisEventWrapperRepository.findAll();
    }
}
