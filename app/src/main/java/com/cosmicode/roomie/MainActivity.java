package com.cosmicode.roomie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.cosmicode.roomie.view.MainHomeFragment;
import com.cosmicode.roomie.view.MainOptionsFragment;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainHomeFragment.OnFragmentInteractionListener, MainOptionsFragment.OnFragmentInteractionListener {

    // Variables
    private BottomNavigationView navigationView;

    public static final Intent clearTopIntent(Context from) {
        Intent intent = new Intent(from, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.navigation_view);
        navigationView.setOnNavigationItemSelectedListener(this);
        openFragment(MainHomeFragment.newInstance("", ""));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_view_home:
                MainHomeFragment homeFragment = MainHomeFragment.newInstance("", "");
                openFragment(homeFragment);
                return true;
            case R.id.navigation_view_account:
                performLogout();
                return true;
            case R.id.navigation_view_notifications:

                return true;
            case R.id.navigation_view_options:
                MainOptionsFragment optionsFragment = MainOptionsFragment.newInstance("", "");
                openFragment(optionsFragment);
                return true;
            default:
                MainHomeFragment defaultFragment = MainHomeFragment.newInstance("", "");
                openFragment(defaultFragment);
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public final void performLogout() {
        try {
            GoogleSignInOptions gso = (new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)).requestServerAuthCode(getString(R.string.default_web_client_id)).requestEmail().build();
            GoogleSignIn.getClient(this, gso).signOut();
        } catch (Exception e) {
            //Ignore TODO: LOG
        }

        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            //Ignore TODO: LOG
        }

        getJhiUsers().logout();
        startActivity(LoginActivity.clearTopIntent(this));
    }

    public BaseActivity getBaseActivity() {
        return this;
    }

}
