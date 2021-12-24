package it.chalmers.gamma.adapter.secondary.jpa.settings;

import it.chalmers.gamma.adapter.secondary.jpa.supergroup.SuperGroupTypeEntity;
import it.chalmers.gamma.adapter.secondary.jpa.util.ImmutableEntity;
import it.chalmers.gamma.app.supergroup.domain.SuperGroupType;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "settings_info_api_super_group_types")
public class SettingsInfoSuperGroupTypeEntity extends ImmutableEntity<SettingsInfoSuperGroupTypePK> {

    @EmbeddedId
    private SettingsInfoSuperGroupTypePK id;

    protected SettingsInfoSuperGroupTypeEntity() {}

    protected SettingsInfoSuperGroupTypeEntity(SettingsEntity settingsEntity, SuperGroupTypeEntity superGroupTypeEntity) {
        this.id = new SettingsInfoSuperGroupTypePK(settingsEntity, superGroupTypeEntity);
    }

    @Override
    public SettingsInfoSuperGroupTypePK getId() {
        return this.id;
    }

    public SuperGroupType getSuperGroupType() {
        return this.id.getValue().superGroupType();
    }

}
