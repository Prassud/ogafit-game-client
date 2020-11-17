package com.ogofit.game.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

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
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * Stand alone WebSocketStompClient.
 */
public class StompClient {

    private static String URL = "ws://localhost:8080/ogofit-game";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String line;
        String clientName = "Dummy-client";
        if (args.length != 0) {
            clientName = args[0];
        }
        Client client = Client.builder().name(clientName).id(UUID.randomUUID()).build();
        System.out.println("************ Welcome " + clientName + " ************");
        System.out.println("Let's Start the game");
        registerClient(client);
        while (!State.TERMINATED.equals(client.getState())) {
        }
    }

    private static void registerClient(Client client) throws InterruptedException, ExecutionException {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(objectMapper());
        stompClient.setMessageConverter(messageConverter);
        StompSessionHandler sessionHandler = new OgoFitSessionHandler(client);
        StompSession stompSession = stompClient.connect(URL, sessionHandler).get();
        System.out.println(stompSession);
        stompSession.subscribe("/topic/clients/" + client.getId(), new ClientRegisteredHandler(client, stompSession));
        stompSession.subscribe("/topic/clients/" + client.getId() + "/close", new ClientTerminationHandler(client, stompSession));

        // register the client to Server
        stompSession.send("/app/clients/" + client.getId(), client);
    }

    public static ObjectMapper objectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
