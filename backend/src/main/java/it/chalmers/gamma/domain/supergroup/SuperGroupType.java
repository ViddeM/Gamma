package it.chalmers.gamma.domain.supergroup;

import com.fasterxml.jackson.annotation.JsonCreator;
import it.chalmers.gamma.domain.Id;

import java.util.Locale;

public record SuperGroupType(String value) implements Id<String> {

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public SuperGroupType {
        if (value == null) {
            throw new NullPointerException("Super group type cannot be null");
        }

        value = value.toLowerCase(Locale.ROOT);

        if (!value.matches("^([a-z]{3,30})$")) {
            throw new IllegalArgumentException("Super group type: [" + value + "] must be made using letters with length between 5 - 30");
        }

    }

    public static SuperGroupType valueOf(String name) {
        return new SuperGroupType(name);
    }

    @Override
    public String getValue() {
        return this.value;
    }
}
