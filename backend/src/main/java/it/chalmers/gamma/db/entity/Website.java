package it.chalmers.gamma.db.entity;

import it.chalmers.gamma.domain.dto.website.WebsiteDTO;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "website")
public class Website {
    @Id
    @Column(updatable = false)
    private UUID id;
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "pretty_name")
    private String prettyName;

    public Website() {
        this.id = UUID.randomUUID();
    }

    public Website(String name) {
        this.name = name;
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

    public String getPrettyName() {
        return this.prettyName;
    }

    public void setPrettyName(String prettyName) {
        this.prettyName = prettyName;
    }

    public WebsiteDTO toDTO() {
        return new WebsiteDTO(this.id, this.name, this.prettyName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Website website = (Website) o;
        return Objects.equals(this.id, website.id)
            && Objects.equals(this.name, website.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }

    @Override
    public String toString() {
        return "Website{"
            + "id=" + this.id
            + ", name='" + this.name + '\''
            + '}';
    }
}