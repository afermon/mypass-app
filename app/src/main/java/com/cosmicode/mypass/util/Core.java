package com.cosmicode.mypass.util;

import android.content.SharedPreferences;

import com.cosmicode.mypass.service.UserInterface;

public class Core {
    private final UserInterface userInterface;
    private final CoreConfiguration configuration;
    private final SharedPreferences preferences;

    public Core(UserInterface userInterface, CoreConfiguration configuration, SharedPreferences preferences) {
        this.userInterface = userInterface;
        this.configuration = configuration;
        this.preferences = preferences;
    }
}


