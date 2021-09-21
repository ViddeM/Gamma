package it.chalmers.gamma.domain;

import java.util.UUID;

public record TextId(UUID value) implements Id<UUID> {

    public static TextId generate() {
        return new TextId(UUID.randomUUID());
    }

    @Override
    public UUID getValue() {
        return this.value;
    }
}
