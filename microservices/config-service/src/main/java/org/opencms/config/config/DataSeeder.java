package org.opencms.config.config;

import org.opencms.config.entity.*;
import org.opencms.config.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    
    private final SystemSettingsRepository systemSettingsRepository;
    private final LocaleSettingsRepository localeSettingsRepository;
    private final MailHostRepository mailHostRepository;
    private final CacheSettingsRepository cacheSettingsRepository;
    private final LoginSettingsRepository loginSettingsRepository;
    private final HandlerConfigRepository handlerConfigRepository;
    
    public DataSeeder(SystemSettingsRepository systemSettingsRepository,
                     LocaleSettingsRepository localeSettingsRepository,
                     MailHostRepository mailHostRepository,
                     CacheSettingsRepository cacheSettingsRepository,
                     LoginSettingsRepository loginSettingsRepository,
                     HandlerConfigRepository handlerConfigRepository) {
        this.systemSettingsRepository = systemSettingsRepository;
        this.localeSettingsRepository = localeSettingsRepository;
        this.mailHostRepository = mailHostRepository;
        this.cacheSettingsRepository = cacheSettingsRepository;
        this.loginSettingsRepository = loginSettingsRepository;
        this.handlerConfigRepository = handlerConfigRepository;
    }
    
    @Override
    public void run(String... args) {
        if (systemSettingsRepository.count() == 0) {
            seedSystemSettings();
            seedLocaleSettings();
            seedMailHosts();
            seedCacheSettings();
            seedLoginSettings();
            seedHandlerConfigs();
        }
    }
    
    private void seedSystemSettings() {
        SystemSettings settings = new SystemSettings();
        settings.setDefaultContentEncoding("UTF-8");
        settings.setTimezone("GMT+01:00");
        settings.setHistoryEnabled(true);
        settings.setHistoryVersions(10);
        settings.setHistoryVersionsAfterDeletion(4);
        settings.setSaxImplSystemProperties(false);
        settings.setNotificationProject("Offline");
        settings.setNotificationTime(365);
        settings.setUserSessionMode("standard");
        systemSettingsRepository.save(settings);
    }
    
    private void seedLocaleSettings() {
        LocaleSettings localeSettings = new LocaleSettings();
        localeSettings.setLocaleHandlerClass("org.opencms.i18n.CmsDefaultLocaleHandler");
        localeSettings.getConfiguredLocales().add("en");
        localeSettings.getConfiguredLocales().add("de");
        localeSettings.getDefaultLocales().add("en");
        localeSettings.getDefaultLocales().add("de");
        localeSettingsRepository.save(localeSettings);
    }
    
    private void seedMailHosts() {
        MailHost mailHost = new MailHost();
        mailHost.setName("localhost");
        mailHost.setPort(25);
        mailHost.setProtocol("smtp");
        mailHost.setUsername(null);
        mailHost.setPassword(null);
        mailHost.setOrderIndex(1);
        mailHost.setMailFrom("config-service@opencms.local");
        mailHostRepository.save(mailHost);
    }
    
    private void seedCacheSettings() {
        CacheSettings cacheSettings = new CacheSettings();
        cacheSettings.setFlexCacheEnabled(true);
        cacheSettings.setFlexCacheOffline(false);
        cacheSettings.setMaxCacheBytes(80000000L);
        cacheSettings.setAvgCacheBytes(60000000L);
        cacheSettings.setMaxEntryBytes(4000000L);
        cacheSettings.setMaxKeys(5000);
        cacheSettings.setSizeUsers(64);
        cacheSettings.setSizeGroups(64);
        cacheSettings.setSizeOrgUnits(64);
        cacheSettings.setSizeResources(8192);
        cacheSettings.setSizeProperties(1024);
        cacheSettingsRepository.save(cacheSettings);
    }
    
    private void seedLoginSettings() {
        LoginSettings loginSettings = new LoginSettings();
        loginSettings.setDisableMinutes(15);
        loginSettings.setMaxBadAttempts(5);
        loginSettingsRepository.save(loginSettings);
    }
    
    private void seedHandlerConfigs() {
        HandlerConfig passwordHandler = new HandlerConfig();
        passwordHandler.setType(HandlerConfig.HandlerType.PASSWORD);
        passwordHandler.setClassName("org.opencms.security.CmsDefaultPasswordHandler");
        passwordHandler.setOrderIndex(1);
        passwordHandler.getParameters().put("encoding", "UTF-8");
        passwordHandler.getParameters().put("digest-type", "scrypt");
        passwordHandler.getParameters().put("scrypt.settings", "16384,8,1");
        handlerConfigRepository.save(passwordHandler);
        
        HandlerConfig validationHandler = new HandlerConfig();
        validationHandler.setType(HandlerConfig.HandlerType.VALIDATION);
        validationHandler.setClassName("org.opencms.security.CmsDefaultValidationHandler");
        validationHandler.setOrderIndex(1);
        handlerConfigRepository.save(validationHandler);
        
        HandlerConfig authorizationHandler = new HandlerConfig();
        authorizationHandler.setType(HandlerConfig.HandlerType.AUTHORIZATION);
        authorizationHandler.setClassName("org.opencms.security.CmsDefaultAuthorizationHandler");
        authorizationHandler.setOrderIndex(1);
        handlerConfigRepository.save(authorizationHandler);
    }
}
