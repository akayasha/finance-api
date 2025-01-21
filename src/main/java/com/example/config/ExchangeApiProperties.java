package com.example.config;

import jakarta.inject.Singleton;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class ExchangeApiProperties {

    @ConfigProperty(name = "exchange.api.url")
    String url;

    @ConfigProperty(name = "exchange.api.key")
    String key;

    public String getUrl() {
        return url;
    }

    public String getKey() {
        return key;
    }
}
