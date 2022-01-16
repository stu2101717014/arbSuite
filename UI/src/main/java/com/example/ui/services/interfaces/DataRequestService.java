package com.example.ui.services.interfaces;

import com.example.ui.entities.RequestDataResult;

import java.io.IOException;

public interface DataRequestService {
    RequestDataResult requestData(RequestDataResult requestDataResult) throws InterruptedException, IOException;
}
