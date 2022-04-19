package com.example.dp.services.interfaces;

import com.example.dp.data.entities.ResultEntityDAO;

public interface RawDataReceiverService {

    void persistResultEntity(ResultEntityDAO resultEntity);
}
