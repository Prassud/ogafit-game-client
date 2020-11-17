package com.ogofit.game.handler;

import com.ogofit.game.models.Client;
import com.ogofit.game.models.Instruction;
import com.ogofit.game.models.State;
import com.ogofit.game.utils.InstructionUtils;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;

import java.lang.reflect.Type;

public class InstructionCreatedHandler implements StompFrameHandler {
    private final Client client;
    private final StompSession session;

    public InstructionCreatedHandler(Client client, StompSession session) {
        this.client = client;
        this.session = session;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Instruction.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println(payload);
        InstructionUtils.verifyInstruction(client, session);
    }
}
