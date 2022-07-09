package com.example.williamhillexperimental.services;

import com.google.gson.*;
import dtos.ResultEntityDTO;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class HttpServiceImpl {

    public Map mapJSONToMap(String json) {
        Gson gson = new Gson();
        Map map = gson.fromJson(json, Map.class);
        return map;
    }

    public String getResponseAsString(String url) {
        try {

            Registry<org.apache.http.conn.socket.ConnectionSocketFactory> reg = RegistryBuilder.<org.apache.http.conn.socket.ConnectionSocketFactory> create()
                    .register("http", new ConnectionSocketFactory())
                    .register("https", new SSLConnectionSocketFactory(SSLContexts.createSystemDefault())).build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(reg, new FakeDnsResolver());
            CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build();
            try {
                InetSocketAddress socksaddr = new InetSocketAddress("127.0.0.1", 9150);
                HttpClientContext context = HttpClientContext.create();
                context.setAttribute("socks.address", socksaddr);

                HttpGet request = new HttpGet(url);

                CloseableHttpResponse response = httpclient.execute(request, context);
                try {
                    if (response.getStatusLine().getStatusCode() == 200){
                        HttpEntity entity = response.getEntity();
                        return EntityUtils.toString(entity, "UTF-8");
                    }
                    EntityUtils.consume(response.getEntity());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                httpclient.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String serializeResultEnt(ResultEntityDTO resultEntityDTO) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();

        return gson.toJson(resultEntityDTO);
    }
}

class LocalDateAdapter implements JsonSerializer<LocalDate> {
    @Override
    public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}

