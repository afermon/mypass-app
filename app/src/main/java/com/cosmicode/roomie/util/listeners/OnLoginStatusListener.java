package com.cosmicode.roomie.util.listeners;

public interface OnLoginStatusListener {
    void onLogin(String authToken);

    void onLogout();
}
