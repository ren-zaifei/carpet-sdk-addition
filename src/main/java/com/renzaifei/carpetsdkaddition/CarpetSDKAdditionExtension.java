package com.renzaifei.carpetsdkaddition;

import carpet.CarpetExtension;
import com.renzaifei.carpetsdkaddition.utils.CarpetSDKAdditionTranslations;


import java.util.Map;

import static carpet.CarpetServer.settingsManager;


public class CarpetSDKAdditionExtension implements CarpetExtension {

    @Override
    public void onGameStarted() {
        settingsManager.parseSettingsClass(CarpetSDKAdditionSettings.class);
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return CarpetSDKAdditionTranslations.getTranslations(lang);
    }
}
