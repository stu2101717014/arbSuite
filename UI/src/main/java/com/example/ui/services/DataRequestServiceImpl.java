package com.example.ui.services;

import com.example.ui.entities.RequestDataResult;
import com.example.ui.entities.ResultEntity;
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

@Service
public class DataRequestServiceImpl implements DataRequestService {

    private static String BWIN_TABLE_TENNIS_REQUEST_URL = "http://localhost:8084/api/bwin";

    private static String BETS22_TABLE_TENNIS_REQUEST_URL = "http://localhost:8082/api/22bets";

    private static String BETWINNER_TABLE_TENNIS_REQUEST_URL = "http://localhost:8085/api/betwinner";


    public RequestDataResult requestData(RequestDataResult requestDataResult) throws InterruptedException, IOException {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(100);
        HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManager(connManager);
        CloseableHttpClient httpclient = clientBuilder.build();

        HttpGet bWinHttpGet = new HttpGet(BWIN_TABLE_TENNIS_REQUEST_URL);
        HttpGet bets22HttpGet = new HttpGet(BETS22_TABLE_TENNIS_REQUEST_URL);
        HttpGet betWinnerHttpGet = new HttpGet(BETWINNER_TABLE_TENNIS_REQUEST_URL);

        ClientMultiThreaded bWinThread = new ClientMultiThreaded(httpclient, bWinHttpGet);
        ClientMultiThreaded bets22Thread = new ClientMultiThreaded(httpclient, bets22HttpGet);
        ClientMultiThreaded betWinnerThread = new ClientMultiThreaded(httpclient, betWinnerHttpGet);

        bWinThread.start();
        bets22Thread.start();
        betWinnerThread.start();

        bWinThread.join();
        bets22Thread.join();
        betWinnerThread.join();

        String bWinResultAsString = EntityUtils.toString(bWinThread.entity, "UTF-8");
        requestDataResult.setBwinResult(new Gson().fromJson(bWinResultAsString, ResultEntity.class));

        String bets22ResultAsString = EntityUtils.toString(bets22Thread.entity, "UTF-8");
        requestDataResult.setBets22Result(new Gson().fromJson(bets22ResultAsString, ResultEntity.class));

        String betWinnerResultAsString = EntityUtils.toString(betWinnerThread.entity, "UTF-8");
        requestDataResult.setBetwinnerResult(new Gson().fromJson(betWinnerResultAsString, ResultEntity.class));

        return requestDataResult;
    }
}
