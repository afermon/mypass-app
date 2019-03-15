package com.cosmicode.mypass;

import android.app.Application;
import android.content.Context;

import com.cosmicode.mypass.service.UserInterface;
import com.cosmicode.mypass.service.UserService;
import com.cosmicode.mypass.util.Core;
import com.cosmicode.mypass.util.CoreConfiguration;

public class MyPassApplication extends Application {
    private UserInterface userInterface;
    private Core core;
    private CoreConfiguration config;

    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            this.config = new CoreConfiguration("https://dev-mypass-web.herokuapp.com/api/");
        } else {
            this.config = new CoreConfiguration("https://prod-mypass-web.herokuapp.com/api/");
        }

        userInterface = UserService.with(this, config.getServerUrl(), true, this.getSharedPreferences("UserInterface", 0));
        core = new Core(userInterface, config, getSharedPreferences("Core", Context.MODE_PRIVATE));

    }

    public UserInterface getUserInterface() {
        return userInterface;
    }

    public Core getCore() {
        return core;
    }
}
