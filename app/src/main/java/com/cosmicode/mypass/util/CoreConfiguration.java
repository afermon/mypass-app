package com.cosmicode.mypass.util;

public class CoreConfiguration {

    private final String serverUrl;

    public CoreConfiguration(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public final String getServerUrl() {
        return this.serverUrl;
    }
}
