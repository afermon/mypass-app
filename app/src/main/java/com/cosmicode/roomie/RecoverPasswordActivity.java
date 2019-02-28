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
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cosmicode.roomie.util.listeners.OnRecoverPasswordRequestListener;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class RecoverPasswordActivity extends BaseActivity implements LoaderCallbacks, OnRecoverPasswordRequestListener {

    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int RC_SIGN_IN = 175;
    private static final String[] contactsPermisions = {READ_CONTACTS};

    private AutoCompleteTextView emailAutoCompeteView;
    private Button emailRecoverButton;
    private ImageButton backButton;
    private ConstraintLayout recoverForm;
    private ProgressBar recoverProgress;

    public static final Intent openIntent(Context from) {
        return new Intent(from, RecoverPasswordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        emailAutoCompeteView = findViewById(R.id.email);
        emailRecoverButton = findViewById(R.id.email_recover_button);
        recoverForm = findViewById(R.id.recover_form);
        recoverProgress = findViewById(R.id.recover_progress);
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        emailAutoCompeteView.setOnEditorActionListener(((a, id, b) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                return false;
            } else {
                attemptRecover();
                return true;
            }
        }));

        emailRecoverButton.setOnClickListener(v -> attemptRecover());

    }

    private final void attemptRecover() {
        // Reset errors.
        emailAutoCompeteView.setError(null);

        // Store values at the time of the login attempt.
        String emailStr = emailAutoCompeteView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            emailAutoCompeteView.setError(getString(R.string.error_field_required));
            focusView = emailAutoCompeteView;
            cancel = true;
        } else if (!isEmailValid(emailStr)) {
            emailAutoCompeteView.setError(getString(R.string.error_invalid_email));
            focusView = emailAutoCompeteView;
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
            getJhiUsers().recoverPassword(emailStr, this);
        }
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email);
    }

    public void onRecoverPasswordSuccess() {
        (new AlertDialog.Builder(this))
                .setMessage(R.string.recover_password_success)
                .setPositiveButton(R.string.ok, ((a, b) -> finish()))
                .show();
    }

    public void onRecoverPasswordError(String error) {
        Toast.makeText(this, R.string.recoverErrorMsg, Toast.LENGTH_SHORT).show();
        this.showProgress(false);
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
            Snackbar.make(emailAutoCompeteView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
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

    @Override
    public void onLoaderReset(Loader cursorLoader) {
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        emailAutoCompeteView.setAdapter(adapter);
    }

    public Loader onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), RecoverPasswordActivity.ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", (new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE}),

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    private void showProgress(boolean show) {
        Long shortAnimTime = (long) getResources().getInteger(android.R.integer.config_shortAnimTime);

        recoverForm.setVisibility(((show) ? View.GONE : View.VISIBLE));

        recoverForm.animate()
                .setDuration(shortAnimTime)
                .alpha((float) ((show) ? 0 : 1))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        recoverForm.setVisibility(((show) ? View.GONE : View.VISIBLE));
                    }
                });

        recoverProgress.setVisibility(((show) ? View.VISIBLE : View.GONE));
        recoverProgress.animate()
                .setDuration(shortAnimTime)
                .alpha((float) ((show) ? 1 : 0))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        recoverProgress.setVisibility(((show) ? View.VISIBLE : View.GONE));
                    }
                });
    }

    @Override
    public void onLoadFinished(Loader loader, Object pCursor) {
        Cursor cursor = (Cursor) pCursor;
        ArrayList emails = new ArrayList();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(RecoverPasswordActivity.ProfileQuery.INSTANCE.getADDRESS()));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    private static final class ProfileQuery {

        private static final String[] PROJECTION;
        private static final int ADDRESS = 0;
        private static final int IS_PRIMARY = 1;

        public static final RecoverPasswordActivity.ProfileQuery INSTANCE;

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
            INSTANCE = new RecoverPasswordActivity.ProfileQuery();
            PROJECTION = new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS, ContactsContract.CommonDataKinds.Email.IS_PRIMARY};
        }
    }
}
