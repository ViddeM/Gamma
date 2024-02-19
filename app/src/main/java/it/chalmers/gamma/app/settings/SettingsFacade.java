package it.chalmers.gamma.app.settings;

import it.chalmers.gamma.app.Facade;
import it.chalmers.gamma.app.authentication.AccessGuard;
import it.chalmers.gamma.app.settings.domain.SettingsRepository;
import it.chalmers.gamma.app.supergroup.domain.SuperGroupType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.chalmers.gamma.app.authentication.AccessGuard.isAdmin;

@Service
public class SettingsFacade extends Facade {

    private final SettingsRepository settingsRepository;

    public SettingsFacade(AccessGuard accessGuard, SettingsRepository settingsRepository) {
        super(accessGuard);
        this.settingsRepository = settingsRepository;
    }

    @Transactional
    public void setInfoSuperGroupTypes(List<String> superGroupTypes) {
        this.accessGuard.require(isAdmin());

        this.settingsRepository.setSettings(
                settings -> settings.withInfoSuperGroupTypes(
                        superGroupTypes
                                .stream()
                                .map(SuperGroupType::new)
                                .toList()
                )
        );
    }

    public List<String> getInfoApiSuperGroupTypes() {
        this.accessGuard.require(isAdmin());

        return this.settingsRepository.getSettings()
                .infoSuperGroupTypes()
                .stream()
                .map(SuperGroupType::value)
                .toList();
    }
}
