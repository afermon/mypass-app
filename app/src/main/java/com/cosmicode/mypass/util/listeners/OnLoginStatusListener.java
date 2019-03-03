package com.cosmicode.mypass.util.listeners;

public interface OnLoginStatusListener {
    void onLogin(String authToken);

    void onLogout();
}
