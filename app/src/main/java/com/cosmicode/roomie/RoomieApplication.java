package com.cosmicode.roomie;

import android.app.Application;
import android.content.Context;

import com.cosmicode.roomie.service.UserInterface;
import com.cosmicode.roomie.service.UserService;
import com.cosmicode.roomie.util.Core;
import com.cosmicode.roomie.util.CoreConfiguration;

public class RoomieApplication extends Application {
    private UserInterface userInterface;
    private Core core;
    private CoreConfiguration config;

    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            this.config = new CoreConfiguration("https://dev-roomie-web.herokuapp.com/api/");
        } else {
            this.config = new CoreConfiguration("https://prod-roomie-web.herokuapp.com/api/");
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
