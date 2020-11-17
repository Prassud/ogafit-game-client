package com.ogofit.game.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Instruction {
    private String key;
    private UUID id;
    private Client client;
    private boolean isExpired;
    private boolean isActive;
    private ZonedDateTime createdAt;

    public static Instruction build(String key, Client client) {
        Instant now = Instant.now();
        ZonedDateTime createdAt = now.atZone(ZoneId.of("UTC"));
        return Instruction.builder()
                .client(client)
                .key(key)
                .isActive(true)
                .createdAt(createdAt)
                .id(UUID.randomUUID())
                .build();
    }
}
