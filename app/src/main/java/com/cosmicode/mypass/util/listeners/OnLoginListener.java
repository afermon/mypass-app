package com.cosmicode.mypass.util.listeners;

public interface OnLoginListener {
    void onLoginSuccess();

    void onLoginError(String error);
}
