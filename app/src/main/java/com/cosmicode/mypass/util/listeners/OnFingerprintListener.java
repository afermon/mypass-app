package com.cosmicode.mypass.util.listeners;

public interface OnFingerprintListener {
    void onFingerprintSuccess();

    void onFingerprintError(String error);
}
