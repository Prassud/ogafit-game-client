package com.ogofit.game.utils;

import com.ogofit.game.client.io.InterruptibleReader;
import com.ogofit.game.handler.InstructionCreatedHandler;
import com.ogofit.game.handler.InstructionExpiredHandler;
import com.ogofit.game.handler.InstructionVerifiedHandler;
import com.ogofit.game.models.Client;
import com.ogofit.game.models.Instruction;
import com.ogofit.game.models.State;
import org.springframework.messaging.simp.stomp.StompSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InstructionUtils {

    public static void createAndSendInstruction(Client client, StompSession session) {
        System.out.println("**** Let's Create the Instruction that you like Buddy ******");
        new Thread(getCreateInstructionTask(client, session)).start();
    }

    private static Runnable getCreateInstructionTask(Client client, StompSession session) {
        return () -> {
            InterruptibleReader bufferedReader = InterruptibleReader.getInstance();
            bufferedReader.shouldRead();
            String line;
            line = bufferedReader.call();
            if (line != null) {
                Instruction instruction = Instruction.build(line, client);
                String destination = "/clients/" + client.getId() + "/instructions";
                session.send("/app" + destination, instruction);
                client.updateState(State.INSTRUCTION_CREATED);
            }
        };
    }

    public static void verifyInstruction(Client client, StompSession session) {
        System.out.println("**** Created Instruction ******");
        System.out.println("**** Let's Type the same instruction to win it ******");
        client.updateState(State.INSTRUCTION_IN_VERIFICATION);

        new Thread(getVerifyInstructionTask(client, session)).start();
    }

    private static Runnable getVerifyInstructionTask(Client client, StompSession session) {
        return () -> {
            InterruptibleReader bufferedReader = InterruptibleReader.getInstance();
            String line;
            line = bufferedReader.call();
            if (line != null) {
                Instruction instruction = Instruction.build(line, client);
                String destination = "/clients/" + client.getId() + "/instructions/verify";
                session.send("/app" + destination, instruction);
            }
        };
    }
}
