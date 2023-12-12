package it.chalmers.gamma.util.mock;

import java.time.Year;
import java.util.UUID;

public class MockITUser {

    private UUID id;
    private String cid;
    private String nick;
    private String firstName;
    private String lastName;
    private Year acceptanceYear;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCid() {
        return this.cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getNick() {
        return this.nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Year getAcceptanceYear() {
        return this.acceptanceYear;
    }

    public void setAcceptanceYear(Year acceptanceYear) {
        this.acceptanceYear = acceptanceYear;
    }
}
