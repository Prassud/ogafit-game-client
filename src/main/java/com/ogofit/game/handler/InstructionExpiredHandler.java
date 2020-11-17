package com.ogofit.game.handler;

import com.ogofit.game.client.io.InterruptibleReader;
import com.ogofit.game.models.Client;
import com.ogofit.game.models.Response;
import com.ogofit.game.models.State;
import com.ogofit.game.utils.InstructionUtils;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.reflect.Type;

public class InstructionExpiredHandler implements StompFrameHandler {
    private final Client client;
    private final StompSession session;

    public InstructionExpiredHandler(Client client, StompSession session) {
        this.client = client;
        this.session = session;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Response.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("**** Instruction Expired ******");
        System.out.println(payload);
        if (State.INSTRUCTION_IN_VERIFICATION.equals(client.getState())) {
            InterruptibleReader.getInstance().shouldNotRead();
        }
        InstructionUtils.createAndSendInstruction(client,session);
    }
}
