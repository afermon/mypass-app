package com.cosmicode.roomie.util;

public class CoreConfiguration {

    private final String serverUrl;

    public final String getServerUrl() {
        return this.serverUrl;
    }

    public CoreConfiguration(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
