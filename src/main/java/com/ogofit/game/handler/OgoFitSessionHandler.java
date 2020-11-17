package com.ogofit.game.handler;

import com.ogofit.game.models.Client;
import com.ogofit.game.utils.InstructionUtils;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class OgoFitSessionHandler extends StompSessionHandlerAdapter {

    private Client client;

    public OgoFitSessionHandler(Client client) {
        this.client = client;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
//        session.subscribe("/topic/clients/" + client.getId(), new ClientRegisteredHandler(client, session));
//        session.subscribe("/topic/clients/" + client.getId() + "/close", new ClientTerminationHandler(client, session));
//
//        // register the client to Server
//        session.send("/app/clients/" + client.getId(), client);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.err.println("Failed To Connect");
        System.exit(1);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Client.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("**** Client Registered ******");
        System.out.println("**** Great Buddy ****");
        System.out.println(payload);
    }
}

