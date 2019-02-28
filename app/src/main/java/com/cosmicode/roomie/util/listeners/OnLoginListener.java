package com.cosmicode.roomie.util.listeners;

public interface OnLoginListener {
    void onLoginSuccess();

    void onLoginError(String error);
}
