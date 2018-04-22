package kz.rbots.bekertugan.telegrambot;

import lombok.Getter;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import javax.annotation.PostConstruct;


@Component
public class ProxyConfiguration {

    @Value("${telegram-bot-proxy-host}")
    private String PROXY_HOST;

    @Value("${telegram-bot-proxy-port}")
    private int    PROXY_PORT;

    @Getter
    private DefaultBotOptions botOptions;

    @PostConstruct
    public void init(){
        // Set up Http proxy
        botOptions = ApiContext.getInstance(DefaultBotOptions.class);

        HttpHost httpHost = new HttpHost(PROXY_HOST, PROXY_PORT);

        RequestConfig requestConfig = RequestConfig.custom().setProxy(httpHost).setAuthenticationEnabled(false).build();

        botOptions.setRequestConfig(requestConfig);

        botOptions.setHttpProxy(httpHost);
    }
}
