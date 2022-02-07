package com.example.ui.services.interfaces;

import com.example.ui.entities.helpers.RequestDataResult;
import com.example.ui.entities.jpa.PlatformDataRequestWrapperEntity;

import java.io.IOException;
import java.util.List;

public interface DataRequestService {
    public RequestDataResult requestData(RequestDataResult requestDataResult, List<PlatformDataRequestWrapperEntity> platformsList) throws InterruptedException, IOException;
}
