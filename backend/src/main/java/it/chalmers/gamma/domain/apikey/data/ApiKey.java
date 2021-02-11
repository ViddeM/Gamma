package it.chalmers.gamma.domain.apikey.data;

import it.chalmers.gamma.domain.GEntity;
import it.chalmers.gamma.domain.IDsNotMatchingException;
import it.chalmers.gamma.domain.text.Text;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "apikey")
public class ApiKey implements GEntity<ApiKeyDTO> {
    @Id
    @Column(updatable = false)
    private UUID id;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @JoinColumn(name = "description")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Text description;

    @Column(name = "key", length = 150, nullable = false)
    private String key;

    public ApiKey() { }

    public ApiKey(ApiKeyDTO apiKey) {
        try {
            apply(apiKey);
            if(this.id == null) {
                this.id = apiKey.getId();
            }
        } catch (IDsNotMatchingException ignored) { }
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
        this.name = name;
    }

    public Text getDescription() {
        return this.description;
    }

    public void setDescription(Text description) {
        this.description = description;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApiKey apiKey = (ApiKey) o;
        return Objects.equals(this.id, apiKey.id)
                && Objects.equals(this.name, apiKey.name)
                && Objects.equals(this.description, apiKey.description)
                && Objects.equals(this.key, apiKey.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.id,
                this.name,
                this.description,
                this.key);
    }

    @Override
    public String toString() {
        return "ApiKey{"
                + "id=" + this.id
                + ", name='" + this.name + '\''
                + ", description=" + this.description
                + ", key='" + this.key + '\''
                + '}';
    }

    @Override
    public void apply(ApiKeyDTO ak) throws IDsNotMatchingException {
        if(this.id != ak.getId()) {
            throw new IDsNotMatchingException();
        }

        this.name = ak.getName();
        this.key = ak.getKey();
        this.description = ak.getDescription();
    }
}
