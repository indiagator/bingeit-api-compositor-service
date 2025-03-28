package com.secor.apicompositor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {


    @Bean(name = "auth-service-validate")
    public WebClient webClientAuthService(WebClient.Builder webClientBuilder)
    {
        return webClientBuilder
                .baseUrl("http://localhost:8085/api/v1/validate")
                .filter(new LoggingWebClientFilter())
                .build();
    }

    @Bean(name = "plain-old-user-client")
    public WebClient webClientUserService(WebClient.Builder webClientBuilder)
    {
        return webClientBuilder
                .baseUrl("http://localhost:8087/api/v1")
                .filter(new LoggingWebClientFilter())
                .build();
    }

    @Bean(name = "plain-old-sub-client")
    public WebClient webClientSubService(WebClient.Builder webClientBuilder)
    {
        return webClientBuilder
                .baseUrl("http://localhost:8102/api/v1")
                .filter(new LoggingWebClientFilter())
                .build();
    }

    @Bean(name = "plain-old-payment-client")
    public WebClient webClientPaymentService(WebClient.Builder webClientBuilder)
    {
        return webClientBuilder
                .baseUrl("http://localhost:8101/api/v1")
                .filter(new LoggingWebClientFilter())
                .build();
    }



}
