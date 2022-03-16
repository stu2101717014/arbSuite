package com.example.ui.services.interfaces;

import com.example.ui.entities.helpers.TableTennisEventWrapperDTO;
import com.example.ui.entities.jpa.PostProcessTableTennisWrapperDAO;
import com.example.ui.entities.jpa.ResultEntityDAO;

import java.util.*;

public interface TableTennisService {
    List<TableTennisEventWrapperDTO> sortReshapedData(List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOList);

    List<TableTennisEventWrapperDTO> reshapeTableTennisEventsData(List<ResultEntityDAO> resultEntityDAOList);

    void persistPostProcessedData(List<TableTennisEventWrapperDTO> eventWrapperList);

    PostProcessTableTennisWrapperDAO getProcessedData();

    List<String> getPlatformNames();
}
