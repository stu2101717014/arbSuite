package com.example.ui.services.helpers;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

public class ClientMultiThreaded extends Thread {
    CloseableHttpClient httpClient;
    HttpGet httpget;
    public HttpEntity entity;

    public ClientMultiThreaded(CloseableHttpClient httpClient, HttpGet httpget) {
        this.httpClient = httpClient;
        this.httpget = httpget;
    }

    @Override
    public void run() {
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpget);
            this.entity = httpResponse.getEntity();

        } catch (Exception ignored) {
        }
    }
}
