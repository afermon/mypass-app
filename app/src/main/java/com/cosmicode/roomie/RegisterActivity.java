package com.cosmicode.roomie;

import android.os.Bundle;
import android.widget.Button;

public class RegisterActivity extends BaseActivity {
    private static final int REQUEST_READ_CONTACTS = 0;
    private Button email_sign_up_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


    }


    private void populateAutoComplete() {
        if (mayRequestContacts()) {
            getLoaderManager().initLoader(0, null, (android.app.LoaderManager.LoaderCallbacks) this);
        }
    }

    private boolean mayRequestContacts() {
        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        if (checkSelfPermission("android.permission.READ_CONTACTS") == PackageManager.PERMISSION_GRANTED) return true;
        if (shouldShowRequestPermissionRationale("android.permission.READ_CONTACTS"))
            Snackbar.make(emailAutoComplteView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, v -> requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS));
        else
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);*/

        return false;
    }
}
