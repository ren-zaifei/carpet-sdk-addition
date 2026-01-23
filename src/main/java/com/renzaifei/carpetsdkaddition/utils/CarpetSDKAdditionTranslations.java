package com.renzaifei.carpetsdkaddition.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class CarpetSDKAdditionTranslations {
    public static Map<String, String> getTranslations(String lang) {
        InputStream langFile = CarpetSDKAdditionTranslations.class
                .getClassLoader()
                .getResourceAsStream("assets/carpet-sdk-addition/lang/%s.json".formatted(lang));
        if (langFile == null) {
            return Collections.emptyMap();
        }
        String jsonData;
        try {
            jsonData = IOUtils.toString(langFile, StandardCharsets.UTF_8);

        } catch (IOException e) {
            return Collections.emptyMap();
        }
        Gson gson = new GsonBuilder().setLenient().create();
        return gson.fromJson(jsonData,new TypeToken<Map<String, String>>(){}.getType());
    }
}
