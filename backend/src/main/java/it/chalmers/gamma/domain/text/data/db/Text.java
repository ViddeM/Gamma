package it.chalmers.gamma.domain.text.data.db;

import it.chalmers.gamma.util.domain.abstraction.MutableEntity;
import it.chalmers.gamma.domain.text.TextId;
import it.chalmers.gamma.domain.text.data.dto.TextDTO;

import javax.persistence.*;

@Entity
@Table(name = "internal_text")
public class Text extends MutableEntity<TextDTO> {

    @EmbeddedId
    private final TextId textId;

    @Column(name = "sv")
    private String sv;

    @Column(name = "en")
    private String en;

    public Text() {
        this(new TextId(), null, null);
    }

    public Text(TextId textId, String sv, String en) {
        assert(textId != null);

        this.textId = textId;
        this.sv = sv;
        this.en = en;
    }

    public Text(TextDTO text) {
        this(new TextId(), text.getSv(), text.getEn());
    }

    @Override
    public String toString() {
        return "Text{"
                + "id=" + this.textId
                + ", sv='" + this.sv + '\''
                + ", en='" + this.en + '\''
                + '}';
    }

    @Override
    public TextDTO toDTO() {
        return new TextDTO(
                this.sv,
                this.en
        );
    }

    @Override
    public void apply(TextDTO newText) {
        this.sv = newText.getSv();
        this.en = newText.getEn();
    }
}
