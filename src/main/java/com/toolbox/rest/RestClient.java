package com.toolbox.rest;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * Created by aviat on 11/24/2018.
 */
public class RestClient {
    private static final Logger log = LoggerFactory.getLogger(RestClient.class);
    private static final Integer DEFAULT_CONNECTION_TIMEOUT = 5000000; // 5000;
    private static final Integer DEFAULT_READ_TIMEOUT = 5000000;

    protected static Client client  = null;

    static {
        ClientConfig configuration = new ClientConfig();
        configuration.property(ClientProperties.CONNECT_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT);
        configuration.property(ClientProperties.READ_TIMEOUT, DEFAULT_READ_TIMEOUT);
        client = ClientBuilder.newClient(configuration);
    }

    public RestClient(){
        if (client != null){
            ClientConfig configuration = new ClientConfig();
            configuration.property(ClientProperties.CONNECT_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT);
            configuration.property(ClientProperties.READ_TIMEOUT, DEFAULT_READ_TIMEOUT);
            client = ClientBuilder.newClient(configuration);
        }
    }

    public RestClient(Integer connTimeout, Integer readTimeout) {
        ClientConfig configuration = new ClientConfig();
        configuration.property(ClientProperties.CONNECT_TIMEOUT, connTimeout);
        configuration.property(ClientProperties.READ_TIMEOUT, readTimeout);
        client = ClientBuilder.newClient(configuration);
    }

}
