package com.cosmicode.roomie.util.listeners;

import com.cosmicode.roomie.domain.RoomieUser;

public interface OnUpdateUserListener {
    void onUpdateUserSuccess(RoomieUser roomieUser);

    void onUpdateUserError(String error);
}
