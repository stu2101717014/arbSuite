package com.example.dp.services.interfaces;

import dtos.HistoricalTableTennisEventWrapperDTO;
import dtos.TableTennisEventWrapperDTO;

import java.util.List;

public interface HistoricalService {

    List<HistoricalTableTennisEventWrapperDTO> getAllHistoricalRecords();

    void persistPositiveArbitrageRecords(List<TableTennisEventWrapperDTO> eventWrapperList);

}
