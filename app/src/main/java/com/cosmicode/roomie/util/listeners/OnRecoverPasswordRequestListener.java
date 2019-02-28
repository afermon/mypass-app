package com.cosmicode.roomie.util.listeners;

public interface OnRecoverPasswordRequestListener {
    void onRecoverPasswordSuccess();

    void onRecoverPasswordError(String error);
}
