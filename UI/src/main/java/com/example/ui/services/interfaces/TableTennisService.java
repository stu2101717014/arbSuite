package com.example.ui.services.interfaces;



import dtos.MetricsDTO;
import dtos.PostProcessTableTennisWrapperDTO;
import dtos.TableTennisEventWrapperDTO;

import java.util.*;

public interface TableTennisService {

    List<TableTennisEventWrapperDTO> sortReshapedData(List<TableTennisEventWrapperDTO> tableTennisEventWrapperDTOList);

    PostProcessTableTennisWrapperDTO getProcessedData();

    List<String> getPlatformNames();

    MetricsDTO getMetrics();
}
