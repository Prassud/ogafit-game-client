package com.ogofit.game.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ogofit.game.handler.ClientRegisteredHandler;
import com.ogofit.game.handler.ClientTerminationHandler;
import com.ogofit.game.handler.OgoFitSessionHandler;
import com.ogofit.game.models.Client;
import com.ogofit.game.models.State;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class StompClient {

    private static String URL = "ws://localhost:8080/ogofit-game";

    public static final ObjectMapper objectMapper = objectMapper();
    private String serverUrl;

    private String clientName;

    public StompClient(String serverUrl, String clientName) {
        this.serverUrl = serverUrl;
        this.clientName = clientName;
    }

    public void start() throws ExecutionException, InterruptedException {
        updteInfo();
        Client client = Client.builder().name(clientName).id(UUID.randomUUID()).build();
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(objectMapper());
        stompClient.setMessageConverter(messageConverter);
        StompSessionHandler sessionHandler = new OgoFitSessionHandler(client);
        StompSession stompSession = stompClient.connect(serverUrl, sessionHandler).get();
        System.out.println(stompSession);
        stompSession.subscribe("/topic/clients/" + client.getId(), new ClientRegisteredHandler(client, stompSession));
        stompSession.subscribe("/topic/clients/" + client.getId() + "/close", new ClientTerminationHandler(client, stompSession));
        // register the client to Server
        stompSession.send("/app/clients/" + client.getId(), client);
        while (!State.TERMINATED.equals(client.getState())) {
        }
    }

    private void updteInfo() {
        this.clientName = StringUtils.isEmpty(this.clientName) ? "Dummy_client" : this.clientName;
        this.serverUrl = StringUtils.isEmpty(this.serverUrl) ? "ws://localhost:8080/ogofit-game" : this.serverUrl;
    }

    private static ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
