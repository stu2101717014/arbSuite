package com.example.ui.services.interfaces;

import com.example.ui.entities.helpers.RequestDataResultDTO;
import com.example.ui.entities.jpa.PlatformDataRequestWrapperEntityDAO;

import java.io.IOException;
import java.util.List;

public interface DataRequestService {
    public RequestDataResultDTO requestData(RequestDataResultDTO requestDataResultDTO, List<PlatformDataRequestWrapperEntityDAO> platformsList) throws InterruptedException, IOException;
}
