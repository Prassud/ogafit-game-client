package com.ogofit.game.handler;

import com.ogofit.game.client.StompClient;
import com.ogofit.game.models.Client;
import com.ogofit.game.models.Instruction;
import com.ogofit.game.models.Response;
import com.ogofit.game.utils.InstructionUtils;
import lombok.SneakyThrows;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.reflect.Type;

public class InstructionVerifiedHandler implements StompFrameHandler {
    private final Client client;
    private final StompSession session;

    public InstructionVerifiedHandler(Client client, StompSession session) {

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
        System.out.println("**** Instruction Verified ******");
        System.out.println("**** Great Buddy ****");
        System.out.println(StompClient.objectMapper.writeValueAsString(payload));
        System.out.println("**** Let's Continue****");
        InstructionUtils.createAndSendInstruction(client, session);
    }
}
