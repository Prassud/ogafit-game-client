package com.ogofit.game.client.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class InterruptibleReader implements Callable {

    private static InterruptibleReader reader = new InterruptibleReader();

    private AtomicBoolean shouldRead = new AtomicBoolean(true);

    public synchronized void shouldRead() {
        shouldRead.set(true);
    }

    public synchronized void shouldNotRead() {
        shouldRead.set(false);
    }

    public static synchronized InterruptibleReader getInstance() {
        return reader;
    }


    public String call() {
        String input;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (!bufferedReader.ready()) {
                if (!shouldRead.get()) {
                    System.out.println("Interruptable Read() stopped");
                    return null;
                }
            }
            input = bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println("Interruptable Read() cancelled");
            return null;
        }
        return input;
    }

}
