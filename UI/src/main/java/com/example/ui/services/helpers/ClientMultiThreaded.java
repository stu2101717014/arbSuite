package com.example.ui.services.helpers;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

public class ClientMultiThreaded extends Thread {
    public String platformName;
    public CloseableHttpClient httpClient;
    public HttpGet httpget;
    public HttpEntity entity;
    public int statusCode;

    public ClientMultiThreaded(CloseableHttpClient httpClient, HttpGet httpget, String platformName) {
        this.httpClient = httpClient;
        this.httpget = httpget;
        this.platformName = platformName;
    }

    @Override
    public void run() {
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpget);
            StatusLine statusLine = httpResponse.getStatusLine();
            this.entity = httpResponse.getEntity();
            this.statusCode = statusLine.getStatusCode();

        } catch (Exception ignored) {
        }
    }
}
