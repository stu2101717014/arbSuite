package com.example.hs.services;

import com.example.hs.data.HistoricalTableTennisEventWrapperDAO;
import dtos.TableTennisEventWrapperDTO;

import java.util.List;

public interface HistoricalService {
    void persistPositiveArbitrageRecords(List<TableTennisEventWrapperDTO> eventWrapperList);

    List<HistoricalTableTennisEventWrapperDAO> getAll();
}
