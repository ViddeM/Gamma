package it.chalmers.gamma.internal.useragreement.service;

import it.chalmers.gamma.internal.appsettings.service.AppSettingsService;
import it.chalmers.gamma.internal.user.service.MeService;
import it.chalmers.gamma.internal.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserAgreementService {

    private final UserService userService;
    private final AppSettingsService appSettingsService;
    private final MeService meService;

    public UserAgreementService(UserService userService,
                                AppSettingsService appSettingsService,
                                MeService meService) {
        this.userService = userService;
        this.appSettingsService = appSettingsService;
        this.meService = meService;
    }

    @Transactional
    public void resetUserAgreement(String signedInPassword) throws IncorrectPasswordException {
        if (this.meService.checkPassword(signedInPassword)) {
            this.appSettingsService.resetUserAgreement();
            this.userService.resetAllUserAgreements();
        } else {
            throw new IncorrectPasswordException();
        }
    }

    public static class IncorrectPasswordException extends Exception { }

}
