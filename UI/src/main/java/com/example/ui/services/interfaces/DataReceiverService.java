package com.example.ui.services.interfaces;

import com.example.ui.entities.jpa.ResultEntityDAO;

public interface DataReceiverService {

    void persistResultEntity(ResultEntityDAO resultEntity);
}
