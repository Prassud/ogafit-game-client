package com.ogofit.game.handler;

import com.ogofit.game.client.StompClient;
import com.ogofit.game.client.io.InterruptibleReader;
import com.ogofit.game.models.Client;
import com.ogofit.game.models.Response;
import com.ogofit.game.models.State;
import lombok.SneakyThrows;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.reflect.Type;

public class ClientTerminationHandler implements StompFrameHandler {
    private final Client client;
    private final StompSession session;

    public ClientTerminationHandler(Client client, StompSession session) {
        this.client = client;
        this.session = session;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Response.class;
    }

    @SneakyThrows
    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println(StompClient.objectMapper.writeValueAsString(payload));
        InterruptibleReader.getInstance().shouldNotRead();
        client.setState(State.TERMINATED);
        System.exit(1);
    }
}
