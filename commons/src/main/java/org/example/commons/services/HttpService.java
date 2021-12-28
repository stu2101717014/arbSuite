package org.example.commons.services;


import org.example.commons.entities.ResultEntity;

import java.io.IOException;
import java.util.Map;

public interface HttpService {
    Map mapJSONToMap(String json);

    String getResponseAsString(String url) throws IOException;

    String serializeResultEnt(ResultEntity resultEntity);
}
