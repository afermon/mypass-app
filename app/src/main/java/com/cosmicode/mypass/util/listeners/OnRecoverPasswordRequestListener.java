package com.cosmicode.mypass.util.listeners;

public interface OnRecoverPasswordRequestListener {
    void onRecoverPasswordSuccess();

    void onRecoverPasswordError(String error);
}
