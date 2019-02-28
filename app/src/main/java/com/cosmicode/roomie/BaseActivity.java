package com.cosmicode.roomie;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cosmicode.roomie.service.UserInterface;
import com.cosmicode.roomie.util.Core;

public class BaseActivity extends AppCompatActivity {

    public UserInterface getJhiUsers() {
        return ((RoomieApplication) this.getApplication()).getUserInterface();
    }

    public final Core getCore() {
        return ((RoomieApplication) this.getApplication()).getCore();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onContextItemSelected(item);
    }
}
