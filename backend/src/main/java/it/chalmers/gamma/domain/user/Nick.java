package it.chalmers.gamma.domain.user;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Serializable;

public record Nick(String value) implements Serializable {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public Nick {
        if (value == null) {
            throw new NullPointerException("Nick cannot be null");
        } else if (value.length() < 1 || value.length() > 30) {
            throw new IllegalArgumentException("Nick length must be between 1 and 30");
        }
    }

}
