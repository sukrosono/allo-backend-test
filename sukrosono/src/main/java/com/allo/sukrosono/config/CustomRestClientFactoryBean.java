package com.allo.sukrosono.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  .RestClient;

@Configuration
public class CustomRestClientFactoryBean implements FactoryBean<RestClient> {
    @Value("${frankfurter.baseUrl}")
    private String baseUrl;

    @Override
    public RestClient getObject() throws Exception {
        return RestClient.builder()
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    System.err.println("did you malform your request?"+ request);
                    throw new IllegalStateException("our error "+ response.getStatusCode());
                })
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
                    System.err.println("It looks the 3rd party server is misbehave");
                    throw new IllegalStateException("Their error "+ response.getStatusCode());
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