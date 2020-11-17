package com.ogofit.game.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private String status;
    private Client client;

    @Override
    public String toString() {
        return "Response{" +
                "status='" + status + '\'' +
                ", client=" + client +
                '}';
    }
}
