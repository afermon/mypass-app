package com.cosmicode.mypass;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cosmicode.mypass.util.listeners.OnLoginListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements OnLoginListener {

    private static final int RC_SIGN_IN = 175;
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    @BindView(R.id.login_form) ConstraintLayout loginForm;
    @BindView(R.id.login_progress) ProgressBar loginProgress;

    public static final Intent clearTopIntent(Context from) {
        Intent intent = new Intent(from, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        facebookSignInSetup();

        googleSignInSetup();

        if (getJhiUsers().isLoginSaved()) getJhiUsers().autoLogin(this);
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void facebookSignInSetup() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback() {
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null && !accessToken.isExpired()) {
                    LoginActivity.this.getJhiUsers().loginWithFacebook(accessToken.getToken(), LoginActivity.this);
                }
            }

            public void onSuccess(Object var1) {
                this.onSuccess((LoginResult) var1);
            }

            public void onCancel() {
                showProgress(false);
            }

            public void onError(FacebookException exception) {
                showProgress(false);
            }
        });
    }

    @OnClick(R.id.email_sign_in_with_facebook_button)
    public void loginWithFacebook() {
        showProgress(true);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    private void googleSignInSetup() {
        GoogleSignInOptions gso = (new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)).requestServerAuthCode(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @OnClick(R.id.email_sign_in_with_google_button)
    public void loginWithGoogle() {
        showProgress(true);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task task) {
        try {
            GoogleSignInAccount account = (GoogleSignInAccount) task.getResult(ApiException.class);
            handleSignInResult(account);
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Toast.makeText(this, "Google login error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void handleSignInResult(GoogleSignInAccount account) {
        handleSignInResult(account.getServerAuthCode());
    }

    private void handleSignInResult(String token) {
        getJhiUsers().loginWithGoogle(token, this);
    }

    public void onLoginSuccess() {
        this.navigateToMainActivity();
    }

    public void onLoginError(String error) {
        if (error != null) {
            Toast.makeText(this, R.string.loginErrorMsg, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, R.string.loginErrorMsg, Toast.LENGTH_SHORT).show();

        showProgress(false);
    }

    private void showProgress(boolean show) {
        Long shortAnimTime = (long) getResources().getInteger(android.R.integer.config_shortAnimTime);

        loginForm.setVisibility(((show) ? View.GONE : View.VISIBLE));

        loginForm.animate()
                .setDuration(shortAnimTime)
                .alpha((float) ((show) ? 0 : 1))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loginForm.setVisibility(((show) ? View.GONE : View.VISIBLE));
                    }
                });

        loginProgress.setVisibility(((show) ? View.VISIBLE : View.GONE));
        loginProgress.animate()
                .setDuration(shortAnimTime)
                .alpha((float) ((show) ? 1 : 0))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loginProgress.setVisibility(((show) ? View.VISIBLE : View.GONE));
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
}
