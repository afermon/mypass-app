package com.cosmicode.roomie;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cosmicode.roomie.util.listeners.OnLoginListener;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends BaseActivity implements LoaderCallbacks, OnLoginListener {

    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int RC_SIGN_IN = 175;
    private static final String[] contactsPermisions = {READ_CONTACTS};
    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    private AutoCompleteTextView emailAutoComplteView;
    private EditText passwordEditText;
    private Button emailSignInButton, emailSignInWithFacebookButton, emailSignInWithGoogleButton;
    private TextView recoverPasswordButton, emailSignUpButton;
    private ConstraintLayout loginForm;
    private ProgressBar loginProgress;

    public static final Intent clearTopIntent(Context from) {
        Intent intent = new Intent(from, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        populateAutoComplete();

        emailAutoComplteView = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        emailSignInButton = findViewById(R.id.email_sign_in_button);
        emailSignUpButton = findViewById(R.id.email_sign_up_button);
        recoverPasswordButton = findViewById(R.id.recover_password_button);
        emailSignInWithFacebookButton = findViewById(R.id.email_sign_in_with_facebook_button);
        emailSignInWithGoogleButton = findViewById(R.id.email_sign_in_with_google_button);
        loginForm = findViewById(R.id.login_form);
        loginProgress = findViewById(R.id.login_progress);

        passwordEditText.setOnEditorActionListener((($noName_0, id, $noName_2) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                return false;
            } else {
                attemptLogin();
                return true;
            }
        }));

        emailSignInButton.setOnClickListener((it -> attemptLogin()));
        emailSignUpButton.setOnClickListener((it -> register()));
        recoverPasswordButton.setOnClickListener((it -> startActivity(RecoverPasswordActivity.openIntent(this))));
        emailSignInWithFacebookButton.setOnClickListener((it -> loginWithFacebook()));

        facebookSignInSetup();

        emailSignInWithGoogleButton.setOnClickListener((it -> loginWithGoogle()));

        googleSignInSetup();

        if (getJhiUsers().isLoginSaved()) getJhiUsers().autoLogin(this);
    }


    private void register() {
        startActivity(new Intent(this, RegisterActivity.class));
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

    private void loginWithFacebook() {
        showProgress(true);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    private void googleSignInSetup() {
        GoogleSignInOptions gso = (new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)).requestServerAuthCode(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void loginWithGoogle() {
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

    private void populateAutoComplete() {
        if (mayRequestContacts()) {
            getLoaderManager().initLoader(0, null, (android.app.LoaderManager.LoaderCallbacks) this);
        }
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        if (checkSelfPermission("android.permission.READ_CONTACTS") == PackageManager.PERMISSION_GRANTED)
            return true;
        if (shouldShowRequestPermissionRationale("android.permission.READ_CONTACTS"))
            Snackbar.make(emailAutoComplteView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, v -> requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS));
        else
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);

        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            populateAutoComplete();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private final void attemptLogin() {
        // Reset errors.
        emailAutoComplteView.setError(null);
        passwordEditText.setError(null);

        // Store values at the time of the login attempt.
        String emailStr = emailAutoComplteView.getText().toString();
        String passwordStr = passwordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            emailAutoComplteView.setError(getString(R.string.error_field_required));
            focusView = emailAutoComplteView;
            cancel = true;
        } else if (!isEmailValid(emailStr)) {
            emailAutoComplteView.setError(getString(R.string.error_invalid_email));
            focusView = emailAutoComplteView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            getJhiUsers().login(emailStr, passwordStr, this);
        }
    }

    public void onLoginSuccess() {
        this.navigateToMainActivity();
    }

    public void onLoginError(String error) {
        if (error != null) {
            if (error.contains("was not activated")) {
                emailAutoComplteView.setError(getString(R.string.activate_account_first));
            } else {
                Toast.makeText(this, R.string.loginErrorMsg, Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(this, R.string.loginErrorMsg, Toast.LENGTH_SHORT).show();

        showProgress(false);
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email);
    }

    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password);
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

    public Loader onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", (new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE}),

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader loader, Object pCursor) {
        Cursor cursor = (Cursor) pCursor;
        ArrayList emails = new ArrayList();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.INSTANCE.getADDRESS()));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader cursorLoader) {
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        emailAutoComplteView.setAdapter(adapter);
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

    private static final class ProfileQuery {

        private static final String[] PROJECTION;
        private static final int ADDRESS = 0;
        private static final int IS_PRIMARY = 1;

        public static final LoginActivity.ProfileQuery INSTANCE;

        public final String[] getPROJECTION() {
            return PROJECTION;
        }

        public final int getADDRESS() {
            return ADDRESS;
        }

        public final int getIS_PRIMARY() {
            return IS_PRIMARY;
        }

        static {
            INSTANCE = new ProfileQuery();
            PROJECTION = new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.IS_PRIMARY};
        }
    }


}
