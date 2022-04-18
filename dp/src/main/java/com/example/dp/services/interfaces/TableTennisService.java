package com.example.dp.services.interfaces;

import com.example.dp.data.entities.PostProcessTableTennisWrapperDAO;
import dtos.ResultEntityDTO;
import dtos.TableTennisEventWrapperDTO;

import java.util.List;

public interface TableTennisService {

    List<TableTennisEventWrapperDTO> reshapeTableTennisEventsData(List<ResultEntityDTO> resultEntityDAOList);

    void persistPostProcessedData(List<TableTennisEventWrapperDTO> eventWrapperList);

    PostProcessTableTennisWrapperDAO getLatestData();

    List<String> getPlatformNames();
}
