package com.example.ui.services;

import com.example.ui.entities.helpers.RequestDataResult;
import com.example.ui.entities.helpers.ResultEntity;
import com.example.ui.entities.jpa.PlatformDataRequestWrapperEntity;
import com.example.ui.services.helpers.ClientMultiThreaded;
import com.example.ui.services.interfaces.DataRequestService;
import com.google.gson.Gson;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataRequestServiceImpl implements DataRequestService {


    public RequestDataResult requestData(RequestDataResult requestDataResult, List<PlatformDataRequestWrapperEntity> platformsList) throws InterruptedException, IOException {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(100);
        HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(connManager);
        CloseableHttpClient httpclient = clientBuilder.build();

        List<ClientMultiThreaded> threadList = new ArrayList<>();
        for(PlatformDataRequestWrapperEntity pe : platformsList){
            if (pe.getAccessible()){
                HttpGet httpGet = new HttpGet(pe.getUrl());
                ClientMultiThreaded httpGetThread = new ClientMultiThreaded(httpclient, httpGet, pe.getPlatformName());

                httpGetThread.start();
                threadList.add(httpGetThread);
            }
        }

        for(ClientMultiThreaded thread : threadList){
            thread.join();
        }

        for(ClientMultiThreaded thread : threadList){
            String resultAsString  = EntityUtils.toString(thread.entity, "UTF-8");
            if (requestDataResult.getEntityList() == null){
                requestDataResult.setEntityList(new ArrayList<>());
            }
            ResultEntity resultEntity = new Gson().fromJson(resultAsString, ResultEntity.class);
            resultEntity.setPlatformName(thread.platformName);
            int statusCode = thread.statusCode;
            if (statusCode == 200 &&  resultEntity.getTableTennisEventEntitySet().size() > 0){
                requestDataResult.getEntityList().add(resultEntity);
            }
        }

        return requestDataResult;
    }
}
