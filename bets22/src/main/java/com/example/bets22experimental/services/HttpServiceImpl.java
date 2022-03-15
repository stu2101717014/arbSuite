package com.example.bets22experimental.services;


import com.google.gson.*;
import dtos.ResultEntity;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class HttpServiceImpl  {

    public Map mapJSONToMap(String json) {
        Gson gson = new Gson();
        Map map = gson.fromJson(json, Map.class);
        return map;
    }

    public String getResponseAsString(String url) throws IOException {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(url);

        CloseableHttpResponse response = httpclient.execute(httpget);

        HttpEntity entity = response.getEntity();

        String responseString = EntityUtils.toString(entity, "UTF-8");

        httpget.releaseConnection();
        return responseString;
    }

    public String serializeResultEnt(ResultEntity resultEntity) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        return gson.toJson(resultEntity);
    }
}

class LocalDateAdapter implements JsonSerializer<LocalDate> {
    @Override
    public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
