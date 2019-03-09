package com.cosmicode.mypass;

import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;

import com.cosmicode.mypass.service.UserInterface;
import com.cosmicode.mypass.util.Core;

public class BaseActivity extends AppCompatActivity {

    public UserInterface getJhiUsers() {
        return ((MyPassApplication) this.getApplication()).getUserInterface();
    }

    public final Core getCore() {
        return ((MyPassApplication) this.getApplication()).getCore();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onContextItemSelected(item);
    }
}
