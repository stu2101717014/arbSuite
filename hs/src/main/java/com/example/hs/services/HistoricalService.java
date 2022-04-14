package com.example.hs.services;

import dtos.TableTennisEventWrapperDTO;

import java.util.List;

public interface HistoricalService {
    void persistPositiveArbitrageRecords(List<TableTennisEventWrapperDTO> eventWrapperList);
}
