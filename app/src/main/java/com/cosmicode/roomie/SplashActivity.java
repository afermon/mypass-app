package com.cosmicode.roomie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.cosmicode.roomie.service.UserInterface;
import com.cosmicode.roomie.util.listeners.OnLoginListener;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class SplashActivity extends BaseActivity implements OnLoginListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UserInterface users = getJhiUsers();

        if (users.isLoginSaved()) {
            users.autoLogin(this);
        } else if (users.isGoogleLoginSaved()) {
            this.googleSignInSetup();
        } else if (users.isFacebookLoginSaved()) {
            this.facebookSignInSetup();
        } else {
            this.toLoginActivity();
        }

    }

    private void facebookSignInSetup() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            getJhiUsers().loginWithFacebook(accessToken.getToken(), this);
        }
    }

    private void googleSignInSetup() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.silentSignIn().addOnSuccessListener(this, account -> handleSignInResult(account));
        mGoogleSignInClient.silentSignIn().addOnFailureListener(this, exception -> toLoginActivity());
    }

    private final void handleSignInResult(GoogleSignInAccount account) {
        getJhiUsers().loginWithGoogle(account.getServerAuthCode(), this);
    }

    public void onLoginSuccess() {
        toMainActivity();
    }

    private final void toMainActivity() {
        startActivity(MainActivity.clearTopIntent(this));
    }

    public void onLoginError(@Nullable String error) {
        Toast.makeText(this, getString(R.string.loginErrorMsg), Toast.LENGTH_SHORT).show();
        toLoginActivity();
    }

    private final void toLoginActivity() {
        this.startActivity(LoginActivity.clearTopIntent(this));
    }
}
