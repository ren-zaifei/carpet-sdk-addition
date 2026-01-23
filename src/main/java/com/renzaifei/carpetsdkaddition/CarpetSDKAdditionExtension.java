package com.renzaifei.carpetsdkaddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.renzaifei.carpetsdkaddition.utils.CarpetSDKAdditionTranslations;

import java.util.Map;

public class CarpetSDKAdditionExtension implements CarpetExtension {

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(CarpetSDKAdditionSettings.class);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return CarpetSDKAdditionTranslations.getTranslations(lang);
    }
}
