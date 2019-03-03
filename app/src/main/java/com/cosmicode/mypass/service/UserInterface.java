package com.cosmicode.mypass.service;

import com.cosmicode.mypass.domain.MyPassUser;
import com.cosmicode.mypass.util.listeners.OnChangePasswordListener;
import com.cosmicode.mypass.util.listeners.OnLoginListener;
import com.cosmicode.mypass.util.listeners.OnLoginStatusListener;
import com.cosmicode.mypass.util.listeners.OnRecoverPasswordRequestListener;
import com.cosmicode.mypass.util.listeners.OnRegisterListener;
import com.cosmicode.mypass.util.listeners.OnUpdateUserListener;
import com.cosmicode.mypass.util.listeners.OnUserAvailableListener;

public interface UserInterface {

    boolean isLoginSaved();

    boolean isGoogleLoginSaved();

    boolean isFacebookLoginSaved();

    void autoLogin(OnLoginListener listener);

    void login(String login, String password, OnLoginListener listener);

    void register(String email, String firstName, String lastName, String password, OnRegisterListener listener);

    void logout();

    void update(MyPassUser mypassUser, OnUpdateUserListener listener);

    void changePassword(String newPassword, OnChangePasswordListener listener);

    void recoverPassword(String mail, OnRecoverPasswordRequestListener listener);

    void getLogedUser(OnUserAvailableListener listener);

    String getAuthToken();

    void loginWithFacebook(String token, OnLoginListener listener);

    void loginWithGoogle(String token, OnLoginListener listener);

    void setOnLoginStatusListener(OnLoginStatusListener listener);
}
