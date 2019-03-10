package com.cosmicode.mypass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.MenuItem;

import com.cosmicode.mypass.domain.Notification;
import com.cosmicode.mypass.view.MainHomeFragment;
import com.cosmicode.mypass.view.MainNotificationFragment;
import com.cosmicode.mypass.view.MainOptionsFragment;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainHomeFragment.OnFragmentInteractionListener, MainOptionsFragment.OnFragmentInteractionListener, MainNotificationFragment.OnListFragmentInteractionListener {

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
        openFragment(MainHomeFragment.newInstance(), true);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_view_home:
                MainHomeFragment homeFragment = MainHomeFragment.newInstance();
                openFragment(homeFragment, false);
                return true;
            case R.id.navigation_view_notifications:
                MainNotificationFragment notificationFragment = MainNotificationFragment.newInstance(5);
                openFragment(notificationFragment, true);
                return true;
            case R.id.navigation_view_options:
                MainOptionsFragment optionsFragment = MainOptionsFragment.newInstance("", "");
                openFragment(optionsFragment, true);
                return true;
            default:
                MainHomeFragment defaultFragment = MainHomeFragment.newInstance();
                openFragment(defaultFragment, true);
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void openFragment(Fragment fragment, Boolean toLeft) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(toLeft)
            transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right, 0, 0);
        else
            transaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, 0, 0);
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

    @Override
    public void onListFragmentInteraction(Notification item) {
        //TODO: Remove
    }
}
