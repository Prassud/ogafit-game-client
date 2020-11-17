package com.ogofit.game.handler;

import com.ogofit.game.client.StompClient;
import com.ogofit.game.models.Client;

import lombok.SneakyThrows;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.reflect.Type;

import static com.ogofit.game.utils.InstructionUtils.createAndSendInstruction;


public class ClientRegisteredHandler implements StompFrameHandler {
    private final StompSession session;
    private Client client;

    public ClientRegisteredHandler(Client client, StompSession session) {
        this.client = client;
        this.session = session;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Client.class;
    }

    @SneakyThrows
    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println(StompClient.objectMapper.writeValueAsString(payload));
        Client client = (Client) payload;
        System.out.println("****** Client Registered ******");
        System.out.println("****** Let's Start the game ******");
        String destination = "/clients/" + client.getId() + "/instructions";
        session.subscribe("/topic" + destination, new InstructionCreatedHandler(client, session));
        session.subscribe("/topic" + destination + "/verified", new InstructionVerifiedHandler(client, session));
        String destination1 = "/topic" + destination + "/expired";
        System.out.println(destination1);
        session.subscribe(destination1, new InstructionExpiredHandler(client, session));
        createAndSendInstruction(client, session);
    }
}
