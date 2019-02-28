package com.cosmicode.roomie.util.validation;

import android.text.TextUtils;

public class UserValidation {
    public static final boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email);
    }

    public static final boolean isValidFirstName(String firstName) {
        return !TextUtils.isEmpty(firstName);
    }

    public static final boolean isValidLastName(String lastName) {
        return !TextUtils.isEmpty(lastName);
    }

    public static final boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password);
    }

}
