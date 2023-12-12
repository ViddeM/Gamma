package it.chalmers.gamma.db.entity;

import it.chalmers.gamma.domain.dto.user.ActivationCodeDTO;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "activation_code")
public class ActivationCode {

    @Id
    @Column(updatable = false)
    private UUID id;

    @JoinColumn(name = "cid", insertable = true, updatable = false, unique = true)
    @OneToOne(fetch = FetchType.EAGER)
    private Whitelist cid;    // Has a foreign key referencing the Whitelist GROUP_ID

    @Column(name = "code", length = 30)
    private String code;

    @Column(name = "created_at")
    private Instant createdAt;

    @Transient
    @Value("${password-expiration-time}")
    private static final int PASSWORD_EXPIRATION_TIME = 3600;

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    protected ActivationCode() {
        this.id = UUID.randomUUID();
    }

    public ActivationCode(Whitelist cid) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.cid = cid;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCid() {
        return this.cid.getCid();
    }

    public Whitelist getWhitelist() {
        return this.cid;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setWhitelist(Whitelist cid) {
        this.cid = cid;
    }

    public void setCid(Whitelist cid) {
        this.cid = cid;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isValid() {
        return Instant.now().isBefore(this.createdAt.plus(Duration.ofSeconds(PASSWORD_EXPIRATION_TIME)));
    }

    public ActivationCodeDTO toDTO() {
        return new ActivationCodeDTO(this.id,
                this.cid.toDTO(),
                this.code,
                this.createdAt,
                PASSWORD_EXPIRATION_TIME);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActivationCode that = (ActivationCode) o;
        return Objects.equals(this.id, that.id)
            && Objects.equals(this.cid, that.cid)
            && Objects.equals(this.code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.cid, this.code);
    }

    @Override
    public String toString() {
        return "ActivationCode{"
            + "id=" + this.id
            + ", whitelistedCid=" + this.cid
            + ", code='" + this.code + '\''
            + '}';
    }
}
