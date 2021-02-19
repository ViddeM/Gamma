package it.chalmers.gamma.domain.group.data;

import it.chalmers.gamma.domain.IDsNotMatchingException;
import it.chalmers.gamma.domain.GEntity;

import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fkit_group")
public class Group implements GEntity<GroupDTO> {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "avatar_url")
    private String avatarURL;

    @Column(name = "name")
    private String name;

    @Column(name = "pretty_name")
    private String prettyName;

    @Column(name = "becomes_active")
    private Calendar becomesActive;

    @Column(name = "becomes_inactive")
    private Calendar becomesInactive;

    @Column(name = "email")
    private String email;

    @Column(name = "fkit_super_group")
    private UUID superGroupId;

    protected Group() {}

    public Group(GroupDTO g) {
        this.id = g.getId();
        try {
            apply(g);
        } catch (IDsNotMatchingException ignored) { }

        if(this.id == null) {
            this.id = UUID.randomUUID();
        }

    }

    public void apply(GroupDTO g) throws IDsNotMatchingException {
        if(this.id != null && this.id != g.getId()) {
            throw new IDsNotMatchingException();
        }

        this.id = g.getId();
        this.avatarURL = g.getAvatarURL();
        this.name = g.getName();
        this.prettyName = g.getPrettyName();
        this.becomesActive = g.getBecomesActive();
        this.becomesInactive = g.getBecomesInactive();
        this.email = g.getEmail().get();
        this.superGroupId = g.getSuperGroup().getId();
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public String getAvatarURL() {
        return this.avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getPrettyName() {
        return this.prettyName;
    }

    public void setPrettyName(String prettyName) {
        this.prettyName = prettyName;
    }

    public Calendar getBecomesActive() {
        return this.becomesActive;
    }

    public void setBecomesActive(Calendar becomesActive) {
        this.becomesActive = becomesActive;
    }

    public Calendar getBecomesInactive() {
        return this.becomesInactive;
    }

    public void setBecomesInactive(Calendar becomesInactive) {
        this.becomesInactive = becomesInactive;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public UUID getSuperGroupId() {
        return superGroupId;
    }

    public void setSuperGroupId(UUID superGroupId) {
        this.superGroupId = superGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(avatarURL, group.avatarURL) && Objects.equals(name, group.name) && Objects.equals(prettyName, group.prettyName) && Objects.equals(becomesActive, group.becomesActive) && Objects.equals(becomesInactive, group.becomesInactive) && Objects.equals(email, group.email) && Objects.equals(superGroupId, group.superGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, avatarURL, name, prettyName, becomesActive, becomesInactive, email, superGroupId);
    }

    @Override
    public String toString() {
        return "Group{"
                + "id=" + this.id
                + ", avatarURL='" + this.avatarURL + '\''
                + ", name='" + this.name + '\''
                + ", prettyName='" + this.prettyName + '\''
                + ", becomesActive=" + this.becomesActive
                + ", becomesInactive=" + this.becomesInactive
                + ", email=" + this.email + '\''
                + ", superGroup='" + this.superGroupId
                + '}';
    }

}