package com.cosmicode.mypass.util.listeners;

import com.cosmicode.mypass.domain.MyPassUser;

public interface OnUpdateUserListener {
    void onUpdateUserSuccess(MyPassUser mypassUser);

    void onUpdateUserError(String error);
}
