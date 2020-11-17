package com.ogofit.game.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @JsonIgnore
    private Object lock = new Object();
    private UUID id;
    private String name;
    private Score score;
    private State state;

    public void updateState(State state) {
        synchronized (lock) {
            this.state = state;
        }
    }

    @Override
    public String toString() {
        return "Client{" +
                "lock=" + lock +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", state=" + state +
                '}';
    }
}
