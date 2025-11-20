package com.allo.sukrosono.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  .RestClient;

import java.net.MalformedURLException;
import java.net.UnknownHostException;


public class CustomRestClientFactoryBean implements FactoryBean<RestClient> {
    @Value("frankfurter.baseUrl")
    private String baseUrl;

    private RestClient restClient;


    @Override
    public RestClient getObject() throws Exception {
        restClient= RestClient.builder()
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    System.err.println("did you malform your request?");
                    throw new MalformedURLException();
                })
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
                    System.err.println("It looks the 3rd party server is misbehave");
                    throw new UnknownHostException();
                })
                .baseUrl(baseUrl) // customizable timeout header etc
                .build();
    }

    @Override
    public Class<?> getObjectType() {
        return RestClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}