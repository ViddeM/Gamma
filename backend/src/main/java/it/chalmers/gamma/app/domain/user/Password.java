package it.chalmers.gamma.app.domain.user;

import java.util.regex.Pattern;

public record Password(String value) {

    private static final Pattern passwordStartPattern = Pattern.compile("^\\{.+}.+");

    public Password {
        if (value == null) {
            throw new IllegalArgumentException();
        } else if (!passwordStartPattern.matcher(value).matches()) {
            throw new IllegalArgumentException("Password must start with what type the encoding of the password is," +
                    "such as {bcrypt} or {noop}");
        }
    }

    @Override
    public String toString() {
        return "<password redacted>";
    }
}

